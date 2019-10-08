package com.hydata.intelligence.platform.cell_link.utils;

import com.hydata.intelligence.platform.cell_link.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public static boolean check(String str) {
        Pattern regex1 = Pattern
                .compile("^[0-9A-Za-z]{4,32}$");
        Pattern regex2 = Pattern
                .compile("^(?!([a-zA-Z]+|\\d+)$)");
        return regex1.matcher(str).matches() && regex2.matcher(str).matches();
    }

    public static void main(String[] args) {

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
