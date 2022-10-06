package wang.z.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wang.z.common.R;
import wang.z.dto.DishDto;
import wang.z.entity.Dish;
import wang.z.entity.DishFlavor;
import wang.z.mapper.DishMapper;
import wang.z.service.DishFlavorService;
import wang.z.service.DishService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author like
 * @date 2022/9/30 15:46
 * @Description TODO
 */
@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Autowired
    private DishFlavorService dishService;

    @Autowired
    private DishFlavorService dishFlavor;

    @Override
    @Transactional
    public void saveWithFlavor(DishDto dishDto) {
        //保存菜品的基本信息到菜品表
        this.save(dishDto);

        Long dishId = dishDto.getId();

        //菜品口味
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors = flavors.stream().map((item) ->{
            item.setDishId(dishId);
            return item;
        }).collect(Collectors.toList());

        //保存菜品口味数据到才品口哦为表dish——flavor
        dishService.saveBatch(flavors);
    }
/**
 * 根据id查询菜品信息和对应的口味信息
 */
    @Override
    public DishDto getByIdWithFlavr(Long id) {
        //查询菜品信息，从dish表查询
        Dish dish = this.getById(id);

        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish, dishDto);

        //查询当前菜品对应的口味信息，dish_flavor表查询
        LambdaQueryWrapper<DishFlavor> wr = new LambdaQueryWrapper<>();
        wr.eq(DishFlavor::getDishId,dish.getId());
        List<DishFlavor> list = dishService.list(wr);
        dishDto.setFlavors(list);
        return dishDto;
    }

    @Override
    @Transactional
    public void updatWithFlavor(DishDto dishDto) {
        //更新dish表菜品信息
        this.updateById(dishDto);

        //更新口味信息
        LambdaQueryWrapper<DishFlavor> queryWrap = new LambdaQueryWrapper<>();
        queryWrap.eq(DishFlavor::getDishId,dishDto.getId());
        dishFlavor.remove(queryWrap);
        Long dishId = dishDto.getId();
        //添加当前提交过来的口味数据--dish_flavor表的insert操作
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors = flavors.stream().map((item) ->{
            item.setDishId(dishId);
            return item;
        }).collect(Collectors.toList());

        dishFlavor.saveBatch(flavors);
    }
}



