package wang.z.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import wang.z.entity.ShoppingCart;
import wang.z.mapper.ShoppingCartMapper;
import wang.z.service.ShoppingCartService;

/**
 * @author like
 * @date 2022/10/5 20:33
 * @Description TODO
 */
@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {
}
