package com.hydata.intelligence.platform.cell_link.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * @ClassName StringUtil
 * @Description TODO
 * @Author pyt
 * @Date 2019/8/21 10:06
 * @Version
 */
public class StringUtil {
    private static String format = "yyyy-MM-dd HH:mm:ss";
    private static Logger logger = LogManager.getLogger(StringUtil.class);
    private static final ThreadLocal<SimpleDateFormat> threadLocal = new ThreadLocal<>();

    public static boolean check(String str) {
        logger.info("检查devicesn：" + str);
        Pattern regex1 = Pattern
                .compile("^[0-9A-Za-z]{4,32}$");
        /*Pattern regex2 = Pattern
                .compile("^(?!([a-zA-Z]+|\\d+)$)");*/
        logger.info(regex1.matcher(str).matches());
        return regex1.matcher(str).matches() ;
    }


    public static Boolean isBlank(String s) {
        boolean result = false;
        if (StringUtils.isEmpty(s)) {
            result = true;
        } else if (StringUtils.isEmpty(s.trim())) {
            result = true;
        }
        return result;
    }

    public static Date getDate(String s) {
        SimpleDateFormat sdf = null;
        sdf = threadLocal.get();
        if (sdf == null) sdf = new SimpleDateFormat(format);
        Date date = null;
        try {
            logger.info("当前线程为：" + Thread.currentThread().getName());
            date = sdf.parse(s);
        } catch (ParseException e) {
            logger.error(e.getMessage());
        }
        return date;
    }

    public static String getDateString(Date date) {
        SimpleDateFormat sdf = null;
        sdf = threadLocal.get();
        if (sdf == null) sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

}
