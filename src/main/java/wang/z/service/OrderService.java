package wang.z.service;

import com.baomidou.mybatisplus.extension.service.IService;
import wang.z.entity.Orders;


public interface OrderService extends IService<Orders> {
    /**
     * 用户下单
     * @param orders
     */
    void submit(Orders orders);
}
