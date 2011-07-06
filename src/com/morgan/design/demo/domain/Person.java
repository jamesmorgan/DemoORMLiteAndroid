package com.morgan.design.demo.domain;

public class Person {

	private int id;
	private String name;
	private List<App> apps;

	public List<App> getApps() {
		return this.apps;
	}

	public int getId() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}

	public void setApps(final List<App> apps) {
		this.apps = apps;
	}

	public void setId(final int id) {
		this.id = id;
	}

	public void setName(final String name) {
		this.name = name;
	}
}
