package wang.z.service.impl;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import wang.z.entity.AddressBook;
import wang.z.mapper.AddressBookMapper;
import wang.z.service.AddressBookService;

/**
 * @author like
 * @date 2022/10/5 10:52
 * @Description TODO
 */
@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {
}
