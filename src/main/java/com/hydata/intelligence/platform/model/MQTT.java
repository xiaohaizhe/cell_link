package com.hydata.intelligence.platform.model;

/**
 * @author pyt
 * @createTime 2018年12月27日下午2:50:20
 */
public class MQTT {
	private static String broker;

	private static String userName;

	private static String password;

	private static String hostUrl;

	private static String clientId;

	private static String defaultTopic;

	private static int completionTimeout ;   //连接超时
	 
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
		 broker = builder.broker;
		 clientId = builder.clientId;
		 completionTimeout = builder.completionTimeout;
		 defaultTopic = builder.defaultTopic;
		 hostUrl = builder.hostUrl;
		 password = builder.password;
		 userName = builder.userName;
	 }
	public static String getBroker() {
		return broker;
	}
	public static String getUserName() {
		return userName;
	}
	public static String getPassword() {
		return password;
	}
	public static String getHostUrl() {
		return hostUrl;
	}
	public static String getClientId() {
		return clientId;
	}
	public static String getDefaultTopic() {
		return defaultTopic;
	}
	public static int getCompletionTimeout() {
		return completionTimeout;
	}
	 
	 
}

