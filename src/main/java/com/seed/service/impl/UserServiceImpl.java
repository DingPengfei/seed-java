package com.seed.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seed.entity.User;
import com.seed.dao.UserMapper;
import com.seed.service.UserService;
import org.springframework.stereotype.Service;

/**
* @author ding
* @description 针对表【user(用户)】的数据库操作Service实现
* @createDate 2025-05-09 15:52:48
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

}




