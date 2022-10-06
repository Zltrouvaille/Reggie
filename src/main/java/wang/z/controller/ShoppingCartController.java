package wang.z.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import wang.z.common.BaseContest;
import wang.z.common.R;
import wang.z.entity.ShoppingCart;
import wang.z.service.ShoppingCartService;

import java.util.List;

/**
 * @author like
 * @date 2022/10/5 20:37
 * @Description TODO
 */
@Slf4j
@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    /**
     * 添加公务车
     * @return
     */
    @PostMapping("/add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart){
        log.info("购物车数据:{}",shoppingCart);
        //设置用户id，指定当前是哪个用户的购物车数据
        Long currentId = BaseContest.getCurrentId();
        shoppingCart.setUserId(currentId);
        //查询当前菜品或者套餐是否在购物车中
        Long dishid = shoppingCart.getDishId();

        LambdaQueryWrapper<ShoppingCart> queryWrap = new LambdaQueryWrapper<>();
        queryWrap.eq(ShoppingCart::getUserId,currentId);

        if(dishid != null){
            //添加到购物车是菜品
            queryWrap.eq(ShoppingCart::getDishId,dishid);
        }else {
            //添加购物车是套餐
            queryWrap.eq(ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
        }

        //SQL:Select * from shopping_cart where uer_id = ? and dish_id/setmeal_id = ?
        ShoppingCart shoppingone = shoppingCartService.getOne(queryWrap);
        if(shoppingone != null){
            Integer number = shoppingone.getNumber();
            shoppingone.setNumber(number+1);
            shoppingCartService.updateById(shoppingone);
        }else {
            //如果不再，则添加到购物车，数量默认是1
            shoppingCart.setNumber(1);
            shoppingCartService.save(shoppingCart);
            shoppingone = shoppingCart;
        }

        return R.success(shoppingone);
    }

    /**
     * 查看购物车
     */
    @GetMapping("/list")
    public R<List<ShoppingCart>> list(){
        log.info("查看购物车");
        Long currentId = BaseContest.getCurrentId();
        LambdaQueryWrapper<ShoppingCart> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(ShoppingCart::getUserId,currentId);
        lambdaQueryWrapper.orderByAsc(ShoppingCart::getCreateTime);
        List<ShoppingCart> list = shoppingCartService.list(lambdaQueryWrapper);
        return R.success(list);
    }
    /**
        减少订
     */
    @PostMapping("/sub")
    public R<ShoppingCart> sub(@RequestBody ShoppingCart shoppingCart){
        log.info("购物车数据:{}",shoppingCart);
        //设置用户id，指定当前是哪个用户的购物车数据
        Long currentId = BaseContest.getCurrentId();
        shoppingCart.setUserId(currentId);

        Long dishid = shoppingCart.getDishId();

        LambdaQueryWrapper<ShoppingCart> queryWrap = new LambdaQueryWrapper<>();
        queryWrap.eq(ShoppingCart::getUserId,currentId);

        if(dishid != null){
            //添加到购物车是菜品
            queryWrap.eq(ShoppingCart::getDishId,dishid);
        }else {
            //添加购物车是套餐
            queryWrap.eq(ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
        }
        ShoppingCart shoppingone = shoppingCartService.getOne(queryWrap);
        shoppingone.setNumber(shoppingone.getNumber()-1);
        if(shoppingone.getNumber() != 0){
            shoppingCartService.updateById(shoppingone);
        }else {
            shoppingCartService.remove(queryWrap);
        }

        return R.success(shoppingone);
    }

    /**
     * 清空购物车
     * @return
     */
    @DeleteMapping("/clean")
    public R<String> clean(){
        Long currentId = BaseContest.getCurrentId();
        LambdaQueryWrapper<ShoppingCart> queryWrap = new LambdaQueryWrapper<>();
        queryWrap.eq(ShoppingCart::getUserId,currentId);
        shoppingCartService.remove(queryWrap);
        return R.success("清空成功");
    }
}
