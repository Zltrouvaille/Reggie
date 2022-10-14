package wang.z.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;


import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wang.z.Utis.SMSUtils;
import wang.z.Utis.ValidateCodeUtils;
import wang.z.common.R;

import wang.z.entity.User;
import wang.z.service.UserService;
import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author like
 * @date 2022/10/4 19:15
 * @Description TODO
 */
@RestController
@RequestMapping("user")
@Slf4j

public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private RedisTemplate redisTemplate;
    /**
 * 发送手机短信验证码
 */
@PostMapping("/sendMsg")
    public R<String> sendMessage(@RequestBody User user, HttpSession session){
        //声称手机号
        String phone = user.getPhone();

        if (StringUtils.isNotEmpty(phone)){
            //生成随机的四位验证码
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            log.info("code={}",code);
            //调用阿里云提供的短信服务API完成发送短信
//            SMSUtils.sendMessage("测试专用模板","SMS_154950909",phone,code);
            //  session.setAttribute(phone,code);
            //  session.setAttribute("code",code);

            //降生成的验证码环翠道Redis中，并且是指有效期为5分钟
            redisTemplate.opsForValue().set(phone,code,5, TimeUnit.MINUTES);

            return R.success("手机验证码发送短信");
        }
        return R.error("短信发送失败  ");
    }

    /**
     * 登录
     * @param map
     * @param session
     * @return
     */
    @PostMapping("/login")
    public R<User> login(@RequestBody Map map, HttpSession session){
        //获取手机号
        String phone = (String) map.get("phone");
        //获取验证码
        String code = (String) map.get("code");
//        //从Session里面获取code
//        Object truecode = session.getAttribute("code");

        Object truecode = redisTemplate.opsForValue().get(phone);

        redisTemplate.delete(phone);
        //进行验证码对比
        if(truecode.toString().equals(code) && truecode != null){
            //比对成功，登录
            LambdaQueryWrapper<User> queryWrap = new LambdaQueryWrapper<User>();
            queryWrap.eq(User::getPhone,phone);
            User user = userService.getOne(queryWrap);
            if(user == null){
                ////判断是否为新用户
                user = new User();
                user.setPhone(phone);
                user.setName("新用户"+phone.toString());
                user.setStatus(1);
                userService.save(user);
            }
            session.setAttribute("user",user.getId());
            return R.success(user);

        }
        return R.error("验证码错误");

    }

}
