package wang.z.service;

import com.baomidou.mybatisplus.extension.service.IService;
import wang.z.entity.Category;

public interface CategoryService extends IService<Category> {
    public void remove(Long id);
}
