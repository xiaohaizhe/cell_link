package com.hydata.intelligence.platform.utils;
/**
 * @author pyt
 * @createTime 2018年11月8日下午4:23:09
 */
public enum MongoConst {
	GT("$gt"),
    LT("$lt"),
    GTE("$gte"),
    LTE("$lte"),
    AND("and"),
    OR("or"),
    NOT("not");
    private String compareIdentify;
 
    MongoConst(String compareIdentify) {
        this.compareIdentify = compareIdentify;
    }
    public String getCompareIdentify() {
        return compareIdentify;
    }
}

