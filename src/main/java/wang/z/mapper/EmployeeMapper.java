package wang.z.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import wang.z.entity.Employee;

/**
 * @author like
 * @date 2022/9/20 7:10
 * @Description TODO
 */
@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
}
