package com.hydata.intelligence.platform.utils;
/**
 * @author pyt
 * @createTime 2018年11月15日上午11:25:03
 */
public class StringUtils {
	public static boolean isNumeric(String str) {		
		for(int i = str.length(); --i>= 0; ){
			if(!Character.isDigit(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	public static String escapeExprSpecialWord(String keyword) {
		String[] fbsArr = { "\\", "$", "(", ")", "*", "+", ".", "[", "]", "?", "^", "{", "}", "|" };
		for (String key : fbsArr) {
			if (keyword.contains(key)) {
				keyword = keyword.replace(key, "\\" + key);
			}
		}
		return keyword;
	}
}

