package wang.z.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import wang.z.common.R;
import wang.z.entity.Category;
import wang.z.service.CategoryService;

import java.util.List;


/**
 * @author like
 * @date 2022/9/29 12:49
 * @Description TODO
 */
@Slf4j
@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    @PostMapping
    public R<String> Save(@RequestBody Category category){
        categoryService.save(category);
        return R.success("成功");
    }
    @GetMapping("/page")
    public R<Page> page (int page, int pageSize){
        //分页构造器
        Page<Category> pageInfo  = new Page<>(page,pageSize);
        //条件构造器
        LambdaQueryWrapper<Category> queryWrap = new LambdaQueryWrapper<>();
        queryWrap.orderByAsc(Category::getSort);
        //进行分页 查询
        categoryService.page(pageInfo,queryWrap);
        return R.success(pageInfo);
    }

    /**
     * 根据id删除分类
     * @param id
     * @return
     */
    @DeleteMapping("")
    public R<String> delete(Long id){
        categoryService.remove(id);
        return R.success("信息删除成功");
    }
    @PutMapping
    public R<String> update(@RequestBody Category category){
        categoryService.updateById(category);
        return R.success("修改信息成功");
    }
    @GetMapping("/list")
    public R<List<Category>> list(Category category){
        LambdaQueryWrapper<Category> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(category.getType() != null,Category::getType,category.getType());
        lambdaQueryWrapper.orderByAsc(Category::getSort).orderByAsc(Category::getCreateTime);
        List<Category> list = categoryService.list(lambdaQueryWrapper);
    return R.success(list);
}
}
