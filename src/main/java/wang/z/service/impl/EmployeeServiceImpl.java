package wang.z.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import wang.z.entity.Employee;
import wang.z.mapper.EmployeeMapper;
import wang.z.service.EmployeeService;

/**
 * @author like
 * @date 2022/9/20 7:14
 * @Description TODO
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {

}
