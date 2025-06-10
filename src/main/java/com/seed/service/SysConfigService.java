package com.seed.service;

import com.seed.common.web.PageFilter;
import com.seed.entity.SysConfig;

import java.util.List;

/**
 * 配置
 * 
 * @author Joey
 * 
 */
public interface SysConfigService {

  /**
   * 添加配置
   * 
   * @param config
   * @return
   */
  int add(SysConfig config);

  /**
   * 修改配置
   * 
   * @param config
   * @return
   */
  int update(SysConfig config);

  /**
   * 查询
   * 
   * @param config;
   * @return
   */
  List<SysConfig> query(SysConfig config, PageFilter pageFilter);

  /**
   * 查询配置
   * 
   * @param configId;
   * @return
   */
  SysConfig selectConfigById(Integer configId);
}