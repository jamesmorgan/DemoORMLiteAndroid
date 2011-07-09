package com.morgan.design.demo.domain;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "apps")
public class App {

	@DatabaseField(generatedId = true)
	private int id;

	@DatabaseField(canBeNull = true)
	private String name;

	@DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = "person_id")
	private Person person;

	public App() {
		// all persisted classes must define a no-arg constructor with at least package visibility
	}

	public App(final String name, final Person person) {
		this.person = person;
		this.name = name;
	}

	public int getId() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}

	public void setId(final int id) {
		this.id = id;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public void setPerson(final Person person) {
		this.person = person;
	}

	public Person getPerson() {
		return this.person;
	}

	public final boolean hasId() {
		return 0 != this.id;
	}
}
