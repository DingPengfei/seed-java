package com.seed.dao;

import com.seed.entity.SysDevice;

import java.util.List;

/**
 * 设备信息 数据层
 * 
 * @author Joey
 * 
 */
public interface DeviceMapper {
  List<SysDevice> query(SysDevice device);

  SysDevice selectDeviceById(String deviceId, boolean checkConfig, boolean countMessage);

  int generateCode(SysDevice device);

  SysDevice queryVerifyCode(SysDevice device);

  int updateCode(SysDevice device);

  int update(SysDevice device);

  int add(SysDevice device);

  int delete(SysDevice device);

  int insertCode(String deviceId, String code);
}