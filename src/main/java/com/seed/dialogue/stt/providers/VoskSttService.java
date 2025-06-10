package com.seed.dialogue.stt.providers;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import jakarta.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.vosk.LibVosk;
import org.vosk.LogLevel;
import org.vosk.Model;
import org.vosk.Recognizer;

import com.seed.dialogue.stt.SttService;
import com.seed.utils.AudioUtils;

import reactor.core.publisher.Sinks;

import org.json.JSONObject;

/**
 * Vosk STT服务实现
 * 使用JDK 21虚拟线程实现异步处理
 */
public class VoskSttService implements SttService {

    private static final Logger logger = LoggerFactory.getLogger(VoskSttService.class);
    private static final String PROVIDER_NAME = "vosk";
    private static final int QUEUE_TIMEOUT_MS = 5000; // 队列等待超时时间

    // Vosk模型相关对象
    private Model model;
    private String voskModelPath;
    
    @Value("${vosk.enabled:true}")
    private boolean voskEnabled;

    /**
     * 初始化Vosk模型
     */
    @PostConstruct
    public void initialize() throws Exception {
        try {
            // 检查是否启用 Vosk
            // TODO
//            if (!voskEnabled) {
//                logger.info("Vosk 语音识别已通过配置禁用 (vosk.enabled=false)");
//                return;
//            }
            
            // 检查是否是 macOS 操作系统
            String osName = System.getProperty("os.name").toLowerCase();
            // 检查是否是 ARM 架构（用于 M 系列芯片）
            String osArch = System.getProperty("os.arch").toLowerCase();
            
            logger.info("Operating System: {} {}", osName, osArch);

            // 暂时禁用手动库加载以避免segmentation fault
            if (osName.contains("mac") && osArch.contains("aarch64")) {
                logger.warn("检测到 macOS ARM64 架构，暂时跳过 Vosk 初始化以避免 native library 崩溃");
                logger.warn("请考虑使用其他 STT 服务（如：阿里云、腾讯云等）");
                logger.warn("或者设置 vosk.enabled=false 来完全禁用 Vosk");
                return; // 直接返回，不初始化 Vosk
            }
            
            // 禁用Vosk日志输出
            LibVosk.setLogLevel(LogLevel.WARNINGS);

            // 加载模型，路径为配置的模型目录
            voskModelPath = System.getProperty("user.dir") + "/models/vosk-model";
            logger.debug("Vosk model path: {}", voskModelPath);
            
            // 检查模型文件是否存在
            java.io.File modelDir = new java.io.File(voskModelPath);
            if (!modelDir.exists() || !modelDir.isDirectory()) {
                logger.warn("Vosk 模型目录不存在: {}", voskModelPath);
                return;
            }
            
            model = new Model(voskModelPath);
            logger.info("Vosk 模型加载成功！路径: {}", voskModelPath);
        } catch (Exception e) {
            logger.warn("Vosk 模型加载失败！将使用其他STT服务: {}", e.getMessage(), e);
            model = null; // 确保模型为null，防止后续使用
        }
    }

    @Override
    public String getProviderName() {
        return PROVIDER_NAME;
    }

    /**
     * 检查模型是否已初始化
     */
    public boolean isModelInitialized() {
        return model != null;
    }

    @Override
    public String recognition(byte[] audioData) {
        if (model == null) {
            logger.warn("Vosk 模型未初始化，无法进行语音识别");
            return null;
        }
        
        if (audioData == null || audioData.length == 0) {
            logger.warn("音频数据为空！");
            return null;
        }

        // 将原始音频数据转换为WAV格式并保存
        String fileName = AudioUtils.saveAsWav(audioData);

        try (Recognizer recognizer = new Recognizer(model, AudioUtils.SAMPLE_RATE)) {
            ByteArrayInputStream audioStream = new ByteArrayInputStream(audioData);

            byte[] buffer = new byte[4096];
            int bytesRead;

            while ((bytesRead = audioStream.read(buffer)) != -1) {
                if (recognizer.acceptWaveForm(buffer, bytesRead)) {
                    // 如果识别到完整的结果
                    String result = recognizer.getResult();
                    JSONObject jsonResult = new JSONObject(result);
                    return jsonResult.getString("text").replaceAll("\\s+", "");
                }
            }

            // 返回最终的识别结果
            String finalResult = recognizer.getFinalResult();
            JSONObject jsonFinal = new JSONObject(finalResult);
            return jsonFinal.getString("text").replaceAll("\\s+", "");

        } catch (Exception e) {
            logger.error("处理音频时发生错误！", e);
            return null;
        }
    }

    @Override
    public boolean supportsStreaming() {
        return true;
    }

    @Override
    public String streamRecognition(Sinks.Many<byte[]> audioSink) {
        if (model == null) {
            logger.warn("Vosk 模型未初始化，无法进行流式语音识别");
            return "";
        }
        
        // 使用阻塞队列存储音频数据
        BlockingQueue<byte[]> audioQueue = new LinkedBlockingQueue<>();
        AtomicBoolean isCompleted = new AtomicBoolean(false);
        List<String> recognizedText = new ArrayList<>();
        StringBuilder finalResult = new StringBuilder();
        
        // 订阅Sink并将数据放入队列
        audioSink.asFlux().subscribe(
            data -> audioQueue.offer(data),
            error -> {
                logger.error("音频流处理错误", error);
                isCompleted.set(true);
            },
            () -> isCompleted.set(true)
        );
        
        // 使用虚拟线程处理音频识别
        try {
            Thread.startVirtualThread(() -> {
                try (Recognizer recognizer = new Recognizer(model, AudioUtils.SAMPLE_RATE)) {
                    while (!isCompleted.get() || !audioQueue.isEmpty()) {
                        try {
                            byte[] audioChunk = audioQueue.poll(100, TimeUnit.MILLISECONDS);
                            if (audioChunk != null) {
                                boolean hasResult = recognizer.acceptWaveForm(audioChunk, audioChunk.length);
                                if (hasResult) {
                                    // 提取部分识别结果中的文本
                                    String result = recognizer.getResult();
                                    JSONObject jsonResult = new JSONObject(result);
                                    if (jsonResult.has("text") && !jsonResult.getString("text").isEmpty()) {
                                        String text = jsonResult.getString("text").replaceAll("\\s+", "");
                                        recognizedText.add(text);
                                        logger.debug("Vosk识别中间结果: {}", text);
                                    }
                                }
                            }
                            
                            // 如果已完成且队列为空，获取最终结果
                            if (isCompleted.get() && audioQueue.isEmpty()) {
                                String finalText = recognizer.getFinalResult();
                                JSONObject jsonFinal = new JSONObject(finalText);
                                if (jsonFinal.has("text")) {
                                    String text = jsonFinal.getString("text").replaceAll("\\s+", "");
                                    if (!text.isEmpty()) {
                                        recognizedText.add(text);
                                        logger.debug("Vosk识别最终结果: {}", text);
                                    }
                                }
                                break;
                            }
                        } catch (InterruptedException e) {
                            logger.warn("音频数据队列等待被中断", e);
                            Thread.currentThread().interrupt(); // 重新设置中断标志
                            break;
                        }
                    }
                    
                    // 合并所有识别结果
                    for (String text : recognizedText) {
                        finalResult.append(text);
                    }
                    
                } catch (Exception e) {
                    logger.error("Vosk流式识别过程中发生错误", e);
                }
            }).join(); // 等待虚拟线程完成
        } catch (Exception e) {
            logger.error("启动虚拟线程失败", e);
        }
        
        return finalResult.toString();
    }
}