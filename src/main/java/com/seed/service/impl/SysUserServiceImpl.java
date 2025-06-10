package com.seed.service.impl;

import com.github.pagehelper.PageHelper;
import com.seed.common.exception.UserPasswordNotMatchException;
import com.seed.common.exception.UsernameNotFoundException;
import com.seed.common.web.PageFilter;
import com.seed.dao.ConfigMapper;
import com.seed.dao.RoleMapper;
import com.seed.dao.SysUserMapper;
import com.seed.entity.SysUser;
import com.seed.security.AuthenticationService;
import com.seed.service.SysUserService;
import com.seed.utils.DateUtils;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.List;


/**
 * 用户操作
 * 
 * @author Joey
 * 
 */

@Service
public class SysUserServiceImpl extends BaseServiceImpl implements SysUserService {

    private static final String dayOfMonthStart = DateUtils.dayOfMonthStart();
    private static final String dayOfMonthEnd = DateUtils.dayOfMonthEnd();

    @Resource
    private SysUserMapper sysUserMapper;

    @Resource
    private ConfigMapper configMapper;

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private AuthenticationService authenticationService;

    /**
     * 
     * @param username
     * @param password
     * @return 用户登录信息
     * @throws UsernameNotFoundException
     * @throws UserPasswordNotMatchException
     */
    @Override
    public SysUser login(String username, String password)
            throws UsernameNotFoundException, UserPasswordNotMatchException {
        SysUser user = sysUserMapper.selectUserByUsername(username);
        if (ObjectUtils.isEmpty(user)) {
            throw new UsernameNotFoundException();
        } else if (!authenticationService.isPasswordValid(password, user.getPassword())) {
            throw new UserPasswordNotMatchException();
        }
        return user;
    }

    /**
     * 用户信息查询
     * 
     * @param username
     * @return 用户信息
     */
    @Override
    public SysUser query(String username) {
        return sysUserMapper.query(username, dayOfMonthStart, dayOfMonthEnd);
    }

    /**
     * 用户列表查询
     * 
     * @param user
     * @return 用户列表
     */
    @Override
    public List<SysUser> queryUsers(SysUser user, PageFilter pageFilter) {
        if(pageFilter != null){
            PageHelper.startPage(pageFilter.getStart(), pageFilter.getLimit());
        }
        return sysUserMapper.queryUsers(user);
    }

    @Override
    public SysUser selectUserByUserId(Integer userId) {
        return sysUserMapper.selectUserByUserId(userId);
    }

    @Override
    public SysUser selectUserByUsername(String username) {
        return sysUserMapper.selectUserByUsername(username);
    }

    @Override
    public SysUser selectUserByEmail(String email) {
        return sysUserMapper.selectUserByEmail(email);
    }

    /**
     * 新增用户
     * 
     * @param user
     * @return
     */
    @Override
    @Transactional(transactionManager = "transactionManager")
    public int add(SysUser user) {
        return sysUserMapper.add(user);
    }

    /**
     * 用户信息更改
     * 
     * @param user
     * @return
     */
    @Override
    @Transactional(transactionManager = "transactionManager")
    public int update(SysUser user) {
        return sysUserMapper.update(user);
    }

    /**
     * 生成验证码
     * 
     */
    @Override
    public SysUser generateCode(SysUser user) {
        SysUser result = new SysUser();
        sysUserMapper.generateCode(user);
        result.setCode(user.getCode());
        return result;
    }

    /**
     * 查询验证码是否有效
     * 
     * @param code
     * @param email
     * @return
     */
    @Override
    public int queryCaptcha(String code, String email) {
        return sysUserMapper.queryCaptcha(code, email);
    }

}