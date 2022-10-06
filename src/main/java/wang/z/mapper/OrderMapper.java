package wang.z.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import wang.z.entity.Orders;

@Mapper
public interface OrderMapper extends BaseMapper<Orders> {
}
