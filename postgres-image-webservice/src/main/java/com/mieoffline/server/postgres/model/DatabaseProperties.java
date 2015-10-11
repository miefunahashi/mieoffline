package com.mieoffline.server.postgres.model;

public class DatabaseProperties {
	
	public final String password = getProperty("postgres.password");
	private String getProperty(String key) {
		final String value = System.getProperty(key);
		System.out.println(String.format("Captured system property: %s : %s",key,value));
		return value;
	}
	public final String username = getProperty("postgres.username");
	public final String hostname= getProperty("postgres.hostname");	
}
