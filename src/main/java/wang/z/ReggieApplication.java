package wang.z;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author like
 * @date 2022/9/20 5:51
 * @Description TODO
 */
@SpringBootApplication
@Slf4j /**注解开发get，set方法*/
@ServletComponentScan
@EnableTransactionManagement//开启事务注解
@EnableCaching //开启Spring Cache 注解
public class ReggieApplication {
    public static void main(String[] args) {
        SpringApplication.run(ReggieApplication.class,args);
        log.info("项目启动成功");/**日志*/
    }
}
