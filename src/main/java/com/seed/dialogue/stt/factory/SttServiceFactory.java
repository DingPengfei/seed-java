package com.seed.dialogue.stt.factory;

import com.seed.dialogue.stt.SttService;
import com.seed.dialogue.stt.providers.*;
import com.seed.entity.SysConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SttServiceFactory {

    private static final Logger logger = LoggerFactory.getLogger(SttServiceFactory.class);

    // 缓存已初始化的服务：对于API服务，键为"provider:configId"格式；对于本地服务，键为provider名称
    private final Map<String, SttService> serviceCache = new ConcurrentHashMap<>();

    // 默认服务提供商名称
    private static final String DEFAULT_PROVIDER = "vosk";

    // 标记Vosk是否初始化成功
    private boolean voskInitialized = false;

    // 备选默认提供商（当Vosk初始化失败时使用）
    private String fallbackProvider = null;

    /**
     * 应用启动时自动初始化Vosk服务
     */
    @PostConstruct
    public void initializeDefaultSttService() {
        logger.info("正在初始化默认语音识别服务(Vosk)...");
        initializeVosk();
        if (voskInitialized) {
            logger.info("默认语音识别服务(Vosk)初始化成功，可直接使用");
        } else {
            logger.warn("默认语音识别服务(Vosk)初始化失败，将在需要时尝试使用备选服务");
        }
    }

    /**
     * 初始化Vosk服务
     */
    private synchronized void initializeVosk() {
        if (serviceCache.containsKey(DEFAULT_PROVIDER)) {
            return;
        }

        try {
            // 检查是否是可能导致崩溃的环境
            String osName = System.getProperty("os.name").toLowerCase();
            String osArch = System.getProperty("os.arch").toLowerCase();
            
            if (osName.contains("mac") && osArch.contains("aarch64")) {
                logger.warn("检测到 macOS ARM64 环境，跳过 Vosk 初始化以避免 native library 崩溃");
                voskInitialized = false;
                return;
            }
            
            VoskSttService voskService = new VoskSttService();
            voskService.initialize();
            
            // 只有在模型确实加载成功时才认为初始化成功
            if (voskService.isModelInitialized()) {
                serviceCache.put(DEFAULT_PROVIDER, voskService);
                voskInitialized = true;
                logger.info("Vosk STT服务初始化成功");
            } else {
                logger.warn("Vosk 模型加载失败，服务不可用");
                voskInitialized = false;
            }
        } catch (Exception e) {
            logger.error("Vosk STT服务初始化失败", e);
            voskInitialized = false;
        }
    }

    /**
     * 获取默认STT服务
     * 如果Vosk可用则返回Vosk，否则返回备选服务
     */
    public SttService getDefaultSttService() {
        // 如果Vosk已初始化成功，直接返回
        if (voskInitialized && serviceCache.containsKey(DEFAULT_PROVIDER)) {
            return serviceCache.get(DEFAULT_PROVIDER);
        }

        // 否则返回备选服务
        if (fallbackProvider != null && serviceCache.containsKey(fallbackProvider)) {
            return serviceCache.get(fallbackProvider);
        }

        // 如果没有备选服务，尝试创建一个API类型的服务作为备选
        if (serviceCache.isEmpty()) {
            logger.warn("没有可用的STT服务，将尝试创建默认API服务");
            try {
                return null;
            } catch (Exception e) {
                logger.error("创建默认API服务失败", e);
                return null;
            }
        }

        return null;
    }

    /**
     * 根据配置获取STT服务
     */
    public SttService getSttService(SysConfig config) {
        if (config == null) {
            return getDefaultSttService();
        }

        String provider = config.getProvider();

        // 如果是Vosk，直接使用全局共享的实例
        if (DEFAULT_PROVIDER.equals(provider)) {
            // 如果Vosk还未初始化，尝试初始化
            if (!voskInitialized && !serviceCache.containsKey(DEFAULT_PROVIDER)) {
                initializeVosk();
            }

            // Vosk初始化失败的情况
            if (!voskInitialized) {
                return null;
            }
            return serviceCache.get(DEFAULT_PROVIDER);
        }

        // 对于API服务，使用"provider:configId"作为缓存键，确保每个配置使用独立的服务实例
        Integer configId = config.getConfigId();
        String cacheKey = provider + ":" + (configId != null ? configId : "default");

        // 检查是否已有该配置的服务实例
        if (serviceCache.containsKey(cacheKey)) {
            return serviceCache.get(cacheKey);
        }

        // 创建新的API服务实例
        try {
            SttService service = createApiService(config);
            if (service != null) {
                serviceCache.put(cacheKey, service);

                // 如果没有备选默认服务，将此服务设为备选
                if (fallbackProvider == null) {
                    fallbackProvider = cacheKey;
                }
                return service;
            }
        } catch (Exception e) {
            logger.error("创建{}服务失败, configId={}", provider, configId, e);
        }

        return null;
    }

    /**
     * 根据配置创建API类型的STT服务
     */
    private SttService createApiService(SysConfig config) {
        if (config == null) {
            return null;
        }

        String provider = config.getProvider();

        // 根据提供商类型创建对应的服务实例
        if ("tencent".equals(provider)) {
            return new TencentSttService(config);
        } else if ("aliyun".equals(provider)) {
            return new AliyunSttService(config);
        } else if ("funasr".equals(provider)) {
            return new FunASRSttService(config);
        } else if ("xfyun".equals(provider)) {
            return new XfyunSttService(config);
        }
        // 可以添加其他服务提供商的支持

        logger.warn("不支持的STT服务提供商: {}", provider);
        return null;
    }

    public void removeCache(SysConfig config) {
        // 对于API服务，使用"provider:configId"作为缓存键，确保每个配置使用独立的服务实例
        Integer configId = config.getConfigId();
        String provider = config.getProvider();
        String cacheKey = provider + ":" + (configId != null ? configId : "default");
        serviceCache.remove(cacheKey);
    }
}