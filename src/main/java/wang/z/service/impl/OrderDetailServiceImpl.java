package wang.z.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import wang.z.entity.OrderDetail;
import wang.z.mapper.OrderDetailMapper;
import wang.z.service.OrderDetailService;

/**
 * @author like
 * @date 2022/10/6 10:09
 * @Description TODO
 */
@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {
}
