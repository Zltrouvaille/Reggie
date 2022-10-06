package wang.z.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wang.z.common.CustomException;
import wang.z.dto.SetmealDto;
import wang.z.entity.Dish;
import wang.z.entity.Setmeal;
import wang.z.entity.SetmealDish;
import wang.z.mapper.SetmealMapper;
import wang.z.service.SetmealDishService;
import wang.z.service.SetmealService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author like
 * @date 2022/9/30 15:50
 * @Description TODO
 */
@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper,Setmeal> implements SetmealService {
    @Autowired
    private SetmealDishService setmealDishService;

    @Override
    public void saveWithDish(SetmealDto setmealDto) {
        //保存套的基本信息，操作setmeal，执行insert操作
        this.save(setmealDto);

        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
         setmealDishes.stream().map((item) -> {
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());
        //保存套餐和菜品关联信息

        setmealDishService.saveBatch(setmealDishes);
    }

    /**
     * 删除套餐和对应的菜品的信息
     * @param ids
     */
    @Override
    @Transactional
    public void removeWithDish(List<Long> ids) {
        //只有停售的时候才能删除
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Setmeal::getId,ids);
        queryWrapper.eq(Setmeal::getStatus,1);

        int count = this.count(queryWrapper);
        if(count > 0){
            throw new CustomException("套餐正在售卖中，不嫩删除");
        }
        //如果可以删除，先删除套餐中的数据
        this.removeByIds(ids);
        //delete from setmeaal dish where setmeal_id in (1,2,3)
        LambdaQueryWrapper<SetmealDish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(SetmealDish::getSetmealId,ids);
        //删除关心表中的数据---setmeal_dish
        setmealDishService.remove(lambdaQueryWrapper);

    }

    @Override
    public SetmealDto getByIdWithDish(Long id) {
        Setmeal setmeal = this.getById(id);
        SetmealDto setmealDto = new SetmealDto();
        BeanUtils.copyProperties(setmeal, setmealDto);

        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId,id);
        List<SetmealDish> list = setmealDishService.list(queryWrapper);

        setmealDto.setSetmealDishes(list);
        return setmealDto;
    }

    @Override
    @Transactional
    public void updateWithDish(SetmealDto setmealDto) {
        this.updateById(setmealDto);

        LambdaQueryWrapper<SetmealDish> queryWrap = new LambdaQueryWrapper<>();
        queryWrap.eq(SetmealDish :: getSetmealId,setmealDto.getId());
        setmealDishService.remove(queryWrap);

        Long setmealId = setmealDto.getId();
        List<SetmealDish> list = setmealDto.getSetmealDishes();

        list = list.stream().map((item)->{
            item.setSetmealId(setmealId);
           return item;
        }).collect(Collectors.toList());
        setmealDishService.saveBatch(list);
    }
}
