package wang.z.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import org.apache.ibatis.annotations.Mapper;
import wang.z.entity.User;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
