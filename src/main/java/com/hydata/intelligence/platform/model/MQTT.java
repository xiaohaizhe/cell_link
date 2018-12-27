package com.hydata.intelligence.platform.model;

import org.springframework.beans.factory.annotation.Value;

/**
 * @author pyt
 * @createTime 2018年12月27日下午2:50:20
 */
public class MQTT {
	private String broker;

	private String userName;

	private String password;

	private String hostUrl;

	private String clientId;

	private String defaultTopic;

	private int completionTimeout ;   //连接超时
	 
	public static class Builder{
		private String broker;

		private String userName;

		private String password;

		private String hostUrl;

		private String clientId;

		private String defaultTopic;

		private int completionTimeout ;   //连接超时
		
		public Builder setBroker(String broker) {
			this.broker = broker;
			return this;
		}

		public Builder setUserName(String userName) {
			this.userName = userName;
			return this;
		}

		public Builder setPassword(String password) {
			this.password = password;
			return this;
		}

		public Builder setHostUrl(String hostUrl) {
			this.hostUrl = hostUrl;
			return this;
		}

		public Builder setClientId(String clientId) {
			this.clientId = clientId;
			return this;
		}

		public Builder setDefaultTopic(String defaultTopic) {
			this.defaultTopic = defaultTopic;
			return this;
		}

		public Builder setCompletionTimeout(int completionTimeout) {
			this.completionTimeout = completionTimeout;
			return this;
		}

		public MQTT build() {
			return new MQTT(this);
		}
	}
	 
	 public static  Builder options(){
		 return new MQTT.Builder();
	 }
	 public MQTT(Builder builder) {
			// TODO Auto-generated constructor stub
		 this.broker = builder.broker;
		 this.clientId = builder.clientId;
		 this.completionTimeout = builder.completionTimeout;
		 this.defaultTopic = builder.defaultTopic;
		 this.hostUrl = builder.hostUrl;
		 this.password = builder.password;
		 this.userName = builder.userName;
	 }
	public String getBroker() {
		return broker;
	}
	public String getUserName() {
		return userName;
	}
	public String getPassword() {
		return password;
	}
	public String getHostUrl() {
		return hostUrl;
	}
	public String getClientId() {
		return clientId;
	}
	public String getDefaultTopic() {
		return defaultTopic;
	}
	public int getCompletionTimeout() {
		return completionTimeout;
	}
	 
	 
}

