package wang.z.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wang.z.service.OrderDetailService;

/**
 * @author like
 * @date 2022/10/6 10:12
 * @Description TODO
 */
@Slf4j
@RestController
@RequestMapping("/orderDetail")
public class OrderDetailController {
        @Autowired
        private OrderDetailService orderDetailService;

}
