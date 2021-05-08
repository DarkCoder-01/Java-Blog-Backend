package com.darkcoder.service.impl;

import com.darkcoder.entity.User;
import com.darkcoder.mapper.UserMapper;
import com.darkcoder.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Darkcoder
 * @since 2021-05-02
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
