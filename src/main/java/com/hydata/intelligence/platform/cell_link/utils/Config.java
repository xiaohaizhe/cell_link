package com.hydata.intelligence.platform.cell_link.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author pyt
 * @createTime 2018年11月20日上午9:48:47
 */
public class Config {
    private final static Properties properties;
    private static Logger logger = LogManager.getLogger(Config.class);
    static {
//		Logger.getRootLogger().setLevel(Level.OFF);
        InputStream in = Config.class.getClassLoader().getResourceAsStream("application.properties");
        properties = new Properties();
        //增加多配置管理判断 读取spring.profiles.active=test
        try {
            properties.load(in);
            String profile = properties.getProperty("spring.profiles.active");
            //logger.debug(profile);

            if ((profile != null)&&(!profile.equals(""))) {
                properties.clear();
                logger.info("读取配置文件："+"application-" + profile + ".properties");
                in.close();
                in = Config.class.getClassLoader().getResourceAsStream("application-" + profile + ".properties");
                properties.load(in);
                logger.info("配置文件读取结束");
            }
        } catch (IOException e) {
            logger.error("init config error", e);
        } catch (NullPointerException ne){
            logger.error("配置文件读取错误");
        }

    }

    /**
     * 读取以逗号分割的字符串，作为字符串数组返回
     *
     * @param conf
     * @return
     */
	/*public static List<String> getStringList(String conf) {
		return Arrays.asList(StringUtils.split(properties.getProperty(conf), ","));
	}*/

    /**
     * 读取字符串
     *
     * @param conf
     * @return
     */
    public static String getString(String conf) {
        return properties.getProperty(conf);
    }

    /**
     * 读取整数
     *
     * @param conf
     * @return
     */
    public static int getInt(String conf) {
        int ret = 0;
        try {
            ret = Integer.parseInt(getString(conf));
        } catch (NumberFormatException e) {
            logger.error("format error", e);
        }
        return ret;
    }

    /**
     * 读取布尔值
     *
     * @param conf
     * @return
     */
    public static boolean getBoolean(String conf) {
        return Boolean.parseBoolean(getString(conf));
    }
}
