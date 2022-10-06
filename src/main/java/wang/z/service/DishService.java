package wang.z.service;

import com.baomidou.mybatisplus.extension.service.IService;
import wang.z.dto.DishDto;
import wang.z.entity.Dish;

public interface DishService extends IService<Dish> {
    /**
     * 新增菜品，同时保存对应的菜品数据
     * @param dishDto
     */
    public void saveWithFlavor(DishDto dishDto);

    //根据id查询菜品
    public DishDto getByIdWithFlavr(Long id);
    //更新菜品信息和口味信息
    public void  updatWithFlavor(DishDto dishDto);
}
