package wang.z.dto;


import lombok.Data;
import wang.z.entity.Setmeal;
import wang.z.entity.SetmealDish;

import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
