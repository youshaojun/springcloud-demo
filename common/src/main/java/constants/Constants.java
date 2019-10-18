package constants;

import java.util.concurrent.TimeUnit;

public class Constants {

    /**
     * 服务异常返回
     */
    public final static String ADD_ERROR = "添加失败";
    public final static String MODIFY_ERROR = "修改失败";
    public static final String FALL_BACK_MSG = "服务器开小差啦";

    /**
     * redis key
     */
    public final static String REDIS_USER_LIST_KEY = "userList";
    public final static String USERS_KEY = "users";
    public final static String REDIS_KEY = "test";

    /**
     * redis配置相关
     */
    public final static long TIME_OUT = 30L;// 失效时间
    public static final String LOCK_TITLE = "redisLock_"; // 拼接key前缀
    public static final long WAIT_TIME = 1L;// 获取锁的等待时间, 若在此时间内没有获取到则直接返回
    public static final TimeUnit TIME_UNIT = TimeUnit.SECONDS; // 时间单位

    /**
     * 分页相关
     */
    public final static String TOTAL = "total"; // 总记录数
    public final static String ROWS = "rows";// 分页数据集合

    /**
     * 数据源配置
     */
    public static final String URL = "url";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String DRIVER_CLASSNAME = "driverClassName";
    public static final String XA_DATASOURCE_CLASSNAME = "com.alibaba.druid.pool.xa.DruidXADataSource";
    public static final int POOL_SIZE = 5;
    public static final String TYPE_ALIASES_PACKAGE = "com.cloud.provider.entity";
    public static final String MAPPERSCAN_BASEPACKAGES = "com.cloud.provider.dao";
    public static final String MAPPERSCAN_BASEPACKAGES_01 = "com.cloud.provider.dao.dao01";
    public static final String MAPPERSCAN_BASEPACKAGES_02 = "com.cloud.provider.dao.dao02";
    public static final String MAPPERSCAN_BASEPACKAGES_03 = "com.cloud.provider.dao.dao03";
    public static final String MAPPER_LOCATIONS_01 = "classpath*:mapper01/*.xml";
    public static final String MAPPER_LOCATIONS_02 = "classpath*:mapper02/*.xml";
    public static final String MAPPER_LOCATIONS_03 = "classpath*:mapper03/*.xml";
    public static final String PREFIX_01 = "spring.datasource.test01.";
    public static final String PREFIX_02 = "spring.datasource.test02.";
    public static final String PREFIX_03 = "spring.datasource.test03.";
    public static final String RESOURCE_NAME_01 = "test01";
    public static final String RESOURCE_NAME_02 = "test02";
    public static final String RESOURCE_NAME_03 = "test03";

    public final static int ZERO = 0;
    public final static int ONE = 1;

}
