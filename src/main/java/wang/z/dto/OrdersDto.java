package wang.z.dto;

import lombok.Data;
import wang.z.entity.OrderDetail;
import wang.z.entity.Orders;

import java.util.List;

/**
 * @author like
 * @date 2022/10/6 13:56
 * @Description TODO
 */
@Data
public class OrdersDto extends Orders {

        private String userName;

        private String phone;

        private String address;

        private String consignee;

        private List<OrderDetail> orderDetails;

        private int sumNum;

}
