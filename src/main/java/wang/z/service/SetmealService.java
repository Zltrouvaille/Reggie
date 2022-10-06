package wang.z.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;
import wang.z.dto.SetmealDto;
import wang.z.entity.Setmeal;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {
    /**
     * 新增套餐，同时需要保存套餐和菜品的关联关系
     * @param setmealDto
     */
    @Transactional
    public void saveWithDish(SetmealDto setmealDto);

    /**
     * 删除套餐和对应的菜品的信息
     * @param ids
     */
    public void removeWithDish(List<Long> ids);

    //回显逃套餐信息
    public SetmealDto getByIdWithDish(Long id);

    //修改套餐信息
    public void updateWithDish(SetmealDto setmealDto);
}
