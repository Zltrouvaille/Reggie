package wang.z.filter;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;
import wang.z.common.BaseContest;
import wang.z.common.R;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * @author like
 * @date 2022/9/24 7:06
 * @Description TODO
 */
@WebFilter(filterName = "loginCheckFilter",urlPatterns = "/*")
@Slf4j
public class LoginCheckFilter implements Filter {
    //路径匹配器，支持通配符
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        //获取本次的uri
        String requestURI = request.getRequestURI();

       // log.info("你的请求是: {}",requestURI);
        //定义补血药处理的请求路径
        String[] urls = new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/common/**",
                "/user/sendMsg",
                "/user/login",
                "/doc.html/**",
                "/webjars/**",
                "/swagger-resources",
                "/v2/api-docs"
        };

        //2. 判断本次是否需要处理
        boolean check = check(urls,requestURI);
        //3. 如果不需要处理，则直接放行
        if(check) {
            filterChain.doFilter(request, response);
            return;
        }
        log.info("本次访问的是: {}",requestURI);
        //4. 判断登录状态，如果已经登陆，则直接放行
        if(request.getSession().getAttribute("employee") != null){
            log.info("用户一登陆","employee");
            Long emId = (Long) request.getSession().getAttribute("employee");
            BaseContest.setCurrentId(emId);

            Long id = Thread.currentThread().getId();
            log.info("线程id为: {}",id);


            filterChain.doFilter(request,response);
            return;
        }
        //4. 判断登录状态，如果已经登陆，则直接放行
        if(request.getSession().getAttribute("user") != null){
            log.info("用户一登陆","employee");

            Long UserId = (Long) request.getSession().getAttribute("user");
            BaseContest.setCurrentId(UserId);

            filterChain.doFilter(request,response);
            return;
        }
        //5. 如果未登录则返回未登陆的结果
        log.info("用户未登录");
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return;
    }
    public boolean check (String[] urls,String requestURI){
        for (String url: urls){
            boolean match = PATH_MATCHER.match(url,requestURI);
            if(match){
                return true;
            }
        }
    return false;
    }
}
