package com.hydata.intelligence.platform.cell_link.utils;

import org.springframework.util.StringUtils;

/**
 * @ClassName StringUtil
 * @Description TODO
 * @Author pyt
 * @Date 2019/8/21 10:06
 * @Version
 */
public class StringUtil {
    public static Boolean isBlank(String s) {
        boolean result = false;
        if (StringUtils.isEmpty(s)) {
            result = true;
        } else if (StringUtils.isEmpty(s.trim())) {
            result = true;
        }
        return result;
    }

}
