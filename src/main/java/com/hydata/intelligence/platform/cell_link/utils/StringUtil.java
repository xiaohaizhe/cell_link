package com.hydata.intelligence.platform.cell_link.utils;

import com.hydata.intelligence.platform.cell_link.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @ClassName StringUtil
 * @Description TODO
 * @Author pyt
 * @Date 2019/8/21 10:06
 * @Version
 */
public class StringUtil {
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static Logger logger = LogManager.getLogger(StringUtil.class);

    public static boolean isNumeric(String str) {
        for(int i = str.length(); --i>= 0; ){
            if(!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
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

    public static Date getDate(String s){
        Date date = null;
        try {
            date = sdf.parse(s);
        } catch (ParseException e) {
            logger.error(e.getMessage());
        }
        return date;
    }

    public static String getDateString(Date date){
        return sdf.format(date);
    }

}
