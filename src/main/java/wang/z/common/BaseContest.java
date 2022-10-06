package wang.z.common;

/**
 * 基于ThreadLocal封装工具类，用于保存和后去当前登录用户的id
 * @author like
 * @date 2022/9/29 9:47
 * @Description TODO
 */
public class BaseContest {
    private static ThreadLocal<Long> threadLocal = new ThreadLocal<Long>();

    /**
     * @param id 建立值
     */
    public static void setCurrentId(Long id) {
         threadLocal.set(id);
    }

    /**
     * @return 获取值
     */
    public static Long getCurrentId(){
        return threadLocal.get();
    }
}
