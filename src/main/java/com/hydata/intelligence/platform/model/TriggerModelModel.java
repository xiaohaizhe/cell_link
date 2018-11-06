package com.hydata.intelligence.platform.model;

import java.util.Date;
import java.util.List;

import com.hydata.intelligence.platform.dto.DeviceTrigger;
import com.hydata.intelligence.platform.dto.TriggerModel;

/**
 * @author pyt
 * @createTime 2018年11月5日下午3:38:06
 */
public class TriggerModelModel {
	private TriggerModel trigger;
   private List<DeviceTrigger> deviceTriggerList;

   
	public TriggerModel getTrigger() {
		return trigger;
	}

	public void setTrigger(TriggerModel trigger) {
		this.trigger = trigger;
	}

	public List<DeviceTrigger> getDeviceTriggerList() {
		return deviceTriggerList;
	}

	public void setDeviceTriggerList(List<DeviceTrigger> deviceTriggerList) {
		this.deviceTriggerList = deviceTriggerList;
	}
   
}

