package com.seed.dao;

import java.util.List;

import com.seed.entity.SysRole;

/**
 * 角色管理 数据层
 * 
 * @author Joey
 * 
 */
public interface RoleMapper {
  List<SysRole> query(SysRole role);

  int update(SysRole role);

  int resetDefault(SysRole role);

  int add(SysRole role);

  SysRole selectRoleById(Integer roleId);
}