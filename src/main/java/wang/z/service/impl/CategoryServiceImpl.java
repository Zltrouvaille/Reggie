package wang.z.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wang.z.common.CustomException;
import wang.z.entity.Category;
import wang.z.entity.Dish;
import wang.z.entity.Setmeal;
import wang.z.mapper.CategoryMapper;
import wang.z.service.CategoryService;
import wang.z.service.DishService;
import wang.z.service.SetmealService;

/**
 * @author like
 * @date 2022/9/29 12:42
 * @Description TODO
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private DishService dishService;

    @Autowired
    private SetmealService setmealService;

    /**'
     * 删除之前需要判断
     * @param id
     */
    @Override
    public void remove(Long id){
        LambdaQueryWrapper<Dish> dishLambdaQueryWrappers = new LambdaQueryWrapper<>();
        dishLambdaQueryWrappers.eq(Dish::getCategoryId,id);
        int count = dishService.count(dishLambdaQueryWrappers);

        if (count > 0) {
            //已经关联了抛出异常
            throw new CustomException("当前分类下关联了单品");
        }
        //套餐
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId,id);
        int count2 = setmealService.count(setmealLambdaQueryWrapper);
        if(count2 > 0){
            throw new CustomException("当前分类下关联了套餐");
            //异常
        }
        super.removeById(id);
    }
}
