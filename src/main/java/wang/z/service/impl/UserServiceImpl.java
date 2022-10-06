package wang.z.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.stereotype.Service;
import wang.z.entity.User;
import wang.z.mapper.UserMapper;
import wang.z.service.UserService;

/**
 * @author like
 * @date 2022/10/4 18:23
 * @Description TODO
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper,User> implements UserService {
}
