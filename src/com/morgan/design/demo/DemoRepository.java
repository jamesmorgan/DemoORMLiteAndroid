package com.morgan.design.demo;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.ForeignCollection;
import com.morgan.design.demo.domain.App;
import com.morgan.design.demo.domain.Person;

public class DemoRepository {
	private final static String LOG_TAG = "DemoRepository";

	private Dao<App, Integer> appDao;
	private Dao<Person, Integer> personDao;

	public DemoRepository(final DatabaseHelper databaseHelper) {
		this.personDao = getPersonDao(databaseHelper);
		this.appDao = getAppDao(databaseHelper);
	}

	public void clearData() {
		final List<Person> persons = getPersons();
		for (final Person person : persons) {
			deletePerson(person);
		}
	}

	public List<Person> getPersons() {
		try {
			return this.personDao.queryForAll();
		}
		catch (final SQLException e) {
			e.printStackTrace();
		}
		return new ArrayList<Person>();
	}

	public void saveOrUpdatePerson(final Person person) {
		try {
			this.personDao.createOrUpdate(person);
		}
		catch (final SQLException e) {
			e.printStackTrace();
		}
	}

	public void saveOrUpdateApp(final App app) {
		try {
			if (app.hasId()) {
				// update
				this.appDao.update(app);
			}
			else {
				// create/save
				this.appDao.create(app);
			}
		}
		catch (final SQLException e) {
			e.printStackTrace();
		}
	}

	private Dao<Person, Integer> getPersonDao(final DatabaseHelper databaseHelper) {
		if (null == this.personDao) {
			try {
				this.personDao = databaseHelper.getPersonDao();
			}
			catch (final SQLException e) {
				Log.e(LOG_TAG, "Unable to load DAO: " + e.getMessage());
				e.printStackTrace();
			}
		}
		return this.personDao;
	}

	private Dao<App, Integer> getAppDao(final DatabaseHelper databaseHelper) {
		if (null == this.appDao) {
			try {
				this.appDao = databaseHelper.getAppDao();
			}
			catch (final SQLException e) {
				Log.e(LOG_TAG, "Unable to load DAO: " + e.getMessage());
				e.printStackTrace();
			}
		}
		return this.appDao;
	}

	public void deletePerson(final Person person) {
		try {
			final ForeignCollection<App> apps = person.getApps();
			for (final App app : apps) {
				this.appDao.delete(app);
			}
			this.personDao.delete(person);
		}
		catch (final SQLException e) {
			e.printStackTrace();
		}
	}

}
