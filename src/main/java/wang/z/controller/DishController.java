package wang.z.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import wang.z.common.CustomException;
import wang.z.common.R;
import wang.z.dto.DishDto;
import wang.z.entity.Category;
import wang.z.entity.Dish;
import wang.z.entity.DishFlavor;
import wang.z.entity.Setmeal;
import wang.z.service.CategoryService;
import wang.z.service.DishFlavorService;
import wang.z.service.DishService;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author like
 * @date 2022/10/1 21:33
 * @Description TODO
 */
@RestController
@RequestMapping("/dish")
public class DishController {
    @Autowired
    private DishService dishService;

    @Autowired
    private DishFlavorService dishFlavorService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private RedisTemplate redisTemplate;
    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto){
        dishService.saveWithFlavor(dishDto);
        return R.success("新增菜品成功");
    }

    /**
     * 菜品信息的分页
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String name){
        //构造分页器对象
        Page<Dish> infopage = new Page<>(page,pageSize);
        Page<DishDto> dishDtoPage = new Page<>();
        //构造查询对象
        LambdaQueryWrapper<Dish> queryWrap  = new LambdaQueryWrapper<>();
        //添加过滤条件
        queryWrap.like(name!=null,Dish::getName,name);
        //添加排序条件
        queryWrap.orderByDesc(Dish::getUpdateTime);
        //查询
        dishService.page(infopage,queryWrap);

        //对象拷贝
        BeanUtils.copyProperties(infopage, dishDtoPage,"records");

        List<Dish> records = infopage.getRecords();

        List<DishDto> list = records.stream().map((item) -> {
           DishDto dishDto = new DishDto();

           BeanUtils.copyProperties(item, dishDto);

           Long categoryId = item.getCategoryId();
           //根据id查询分类

            Category category = categoryService.getById(categoryId);

            if(category!=null){
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }


            return dishDto;
        }).collect(Collectors.toList());

        dishDtoPage.setRecords(list);
        return R.success(dishDtoPage);
    }
    /**
     *修改数据时候数据的回西安
     */
    @GetMapping("/{id}")
    public R<DishDto> get(@PathVariable Long id){
        DishDto dishDto = dishService.getByIdWithFlavr(id);
        return R.success(dishDto);
    }
    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto){
        dishService.updatWithFlavor(dishDto);
        return R.success("修改菜品成功");
    }

    /**
     * 根据查询条件查询对应菜品数据
     * @param dish
     * @return
     */
//    @GetMapping("/list")
//    public R<List<Dish>> list(Dish dish)
//    {
//        //构造查询条件
//        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.eq(dish.getCategoryId() != null,Dish::getCategoryId,dish.getCategoryId());
//        //添加排序条件
//        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
//        //添加条件，查询状态为1的
//        queryWrapper.eq(Dish::getStatus,1);
//        List<Dish> list = dishService.list(queryWrapper);
//        return R.success(list);
//    }
    @GetMapping("/list")
    public R<List<DishDto>> list(Dish dish)
    {
        List<DishDto> dishDtoList = null;

        String key = "dish_" + dish.getCategoryId() + "_" +dish.getStatus();
        //先从redis中获取缓存
        dishDtoList = (List<DishDto>) redisTemplate.opsForValue().get(key);
        //如果存在，直接返回，无需查询数据库
        if(dishDtoList != null){
            //如果存在，这季节返回，无需查询数据库
            return R.success(dishDtoList);

        }
        //如果不存在，需要插叙数据库，将查询到的菜品数据缓存到Redis中

        //构造查询条件
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(dish.getCategoryId() != null,Dish::getCategoryId,dish.getCategoryId());
        //添加排序条件
        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
        //添加条件，查询状态为1的
        queryWrapper.eq(Dish::getStatus,1);
        List<Dish> list = dishService.list(queryWrapper);

        dishDtoList = list.stream().map((item) -> {
            DishDto dishDto = new DishDto();

            BeanUtils.copyProperties(item, dishDto);

            Long categoryId = item.getCategoryId();
            //根据id查询分类

            Category category = categoryService.getById(categoryId);

            if(category!=null){
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }
            //当前菜品的id
            Long dishId =  item.getId();
            LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(DishFlavor::getDishId,dishId);
            List<DishFlavor> list1 = dishFlavorService.list(lambdaQueryWrapper);

            dishDto.setFlavors(list1);
            return dishDto;
        }).collect(Collectors.toList());

        redisTemplate.opsForValue().set(key,dishDtoList,60, TimeUnit.MINUTES);
        return R.success(dishDtoList);
    }
    @PostMapping("/status/{status}")
    public R<String> status (@PathVariable int status, @RequestParam List<Long> ids){
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Dish::getId,ids);
        List<Dish> list = dishService.list(queryWrapper);
        for (Dish dish : list) {
            if(dish != null){
                dish.setStatus(status);
                dishService.updateById(dish);
            }else {
                throw new CustomException("套餐有问题");
            }
        }
        String statusstring = status==1 ?"禁用成功":"启用成功";
        //如果不存在，需要查询数据库，你查询菜品数据缓存到redis

        return R.success(statusstring);
    }


}
