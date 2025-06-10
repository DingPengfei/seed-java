package com.seed.controller;

import com.github.pagehelper.PageInfo;
import com.seed.common.web.AjaxResult;
import com.seed.common.web.PageFilter;
import com.seed.communication.common.ConfigManager;
import com.seed.dialogue.stt.factory.SttServiceFactory;
import com.seed.dialogue.tts.factory.TtsServiceFactory;
import com.seed.entity.SysConfig;
import com.seed.service.SysConfigService;
import com.seed.utils.CmsUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 配置管理
 * 
 * @author Joey
 * 
 */

@RestController
@RequestMapping("/api/config")
public class ConfigController extends BaseController {

    @Resource
    private SysConfigService configService;

    @Resource
    private ConfigManager configManager;

    @Resource
    private TtsServiceFactory ttsServiceFactory;

    @Resource
    private SttServiceFactory sttServiceFactory;

    /**
     * 配置查询
     * 
     * @param config
     * @return configList
     */
    @GetMapping("/query")
    @ResponseBody
    public AjaxResult query(SysConfig config, HttpServletRequest request) {
        try {
            PageFilter pageFilter = initPageFilter(request);
            List<SysConfig> configList = configService.query(config, pageFilter);
            AjaxResult result = AjaxResult.success();
            result.put("data", new PageInfo<>(configList));
            return result;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return AjaxResult.error();
        }
    }

    /**
     * 配置信息更新
     * 
     * @param config
     * @return
     */
    @PostMapping("/update")
    @ResponseBody
    public AjaxResult update(SysConfig config) {
        try {
            config.setUserId(CmsUtils.getUserId());
            SysConfig oldSysConfig = configService.selectConfigById(config.getConfigId());
            int rows = configService.update(config);
            if (rows > 0) {
                configManager.getConfig(config.getConfigId());// 更新缓存
                if(oldSysConfig != null){
                    if("stt".equals(oldSysConfig.getConfigType()) && !oldSysConfig.getApiKey().equals(config.getApiKey())){
                        sttServiceFactory.removeCache(oldSysConfig);
                    }else if("tts".equals(oldSysConfig.getConfigType()) && !oldSysConfig.getApiKey().equals(config.getApiKey())){
                        ttsServiceFactory.removeCache(oldSysConfig);
                    }
                }
            }
            return AjaxResult.success();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return AjaxResult.error();
        }
    }

    /**
     * 添加配置
     * 
     * @param config
     */
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult add(SysConfig config) {
        try {
            config.setUserId(CmsUtils.getUserId());
            configService.add(config);
            return AjaxResult.success();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return AjaxResult.error();
        }
    }
}