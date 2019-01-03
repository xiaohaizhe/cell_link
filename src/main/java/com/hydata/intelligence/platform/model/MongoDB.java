package com.hydata.intelligence.platform.model;
/**
 * @author pyt
 * @createTime 2019年1月2日下午5:52:34
 */
public class MongoDB {
	private String username;
	private String password;
	private String host;
	private int port;
			
	public static class Builder{
		private String username;
		private String password;
		private String host;
		private int port;
		public Builder setUsername(String username) {
			this.username = username;
			return this;
		}
		public Builder setPassword(String password) {
			this.password = password;
			return this;
		}
		public Builder setHost(String host) {
			this.host = host;
			return this;
		}
		public Builder setPort(int port) {
			this.port = port;
			return this;
		}
		public MongoDB Builder() {
			return new MongoDB(this);
		}		
	}
	
	public static Builder options() {
		return new MongoDB.Builder();
	}

	public MongoDB(Builder builder) {
		this.host = builder.host;
		this.password = builder.password;
		this.port = builder.port;
		this.username = builder.username;
		
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public String getHost() {
		return host;
	}

	public int getPort() {
		return port;
	}
	
	

}

