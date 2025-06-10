package com.seed.service.impl;

import com.github.pagehelper.PageHelper;
import com.seed.common.web.PageFilter;
import com.seed.dao.RoleMapper;
import com.seed.entity.SysRole;
import com.seed.service.SysRoleService;
import jakarta.annotation.Resource;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 角色操作
 *
 * @author Joey
 *
 */

@Service
public class SysRoleServiceImpl extends BaseServiceImpl implements SysRoleService {
    private final static String CACHE_NAME = "Seed:SysRole";

    @Resource
    private RoleMapper roleMapper;

    /**
     * 添加角色
     *
     * @param role
     * @return
     */
    @Override
    @Transactional(transactionManager = "transactionManager")
    public int add(SysRole role) {
        // 如果当前配置被设置为默认，则将同类型同用户的其他配置设置为非默认
        if (role.getIsDefault() != null && role.getIsDefault().equals("1")) {
            roleMapper.resetDefault(role);
        }
        // 添加角色
        return roleMapper.add(role);
    }

    /**
     * 查询角色信息
     * 指定分页信息
     * @param role
     * @param pageFilter
     * @return
     */
    @Override
    public List<SysRole> query(SysRole role, PageFilter pageFilter) {
        if(pageFilter != null){
            PageHelper.startPage(pageFilter.getStart(), pageFilter.getLimit());
        }
        return roleMapper.query(role);
    }

    /**
     * 更新角色信息
     *
     * @param role
     * @return
     */
    @Override
    @Transactional(transactionManager = "transactionManager")
    @CacheEvict(value = CACHE_NAME, key = "#role.roleId", condition = "#role.roleId != null")
    public int update(SysRole role) {
        // 如果当前配置被设置为默认，则将同类型同用户的其他配置设置为非默认
        if (role.getIsDefault() != null && role.getIsDefault().equals("1")) {
            roleMapper.resetDefault(role);
        }
        return roleMapper.update(role);
    }

    @Override
    @Cacheable(value = CACHE_NAME, key = "#roleId", unless = "#result == null")
    public SysRole selectRoleById(Integer roleId) {
        return roleMapper.selectRoleById(roleId);
    }
}