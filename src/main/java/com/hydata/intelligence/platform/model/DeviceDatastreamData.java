package com.hydata.intelligence.platform.model;

import java.util.Observable;

/**
 * @author pyt
 * @createTime 2018年11月8日上午11:49:04
 */
public class DeviceDatastreamData extends Observable {
	private Integer deviceDatastreamId;//设备数据流id
	private String deviceDatastreamName;//设备数据流名称
	private Object value;				//设备数据流值

}

