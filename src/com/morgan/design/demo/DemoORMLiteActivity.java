package com.morgan.design.demo;

import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;

import com.j256.ormlite.android.apptools.OrmLiteBaseListActivity;
import com.j256.ormlite.dao.ForeignCollection;
import com.morgan.design.demo.domain.App;
import com.morgan.design.demo.domain.Person;

public class DemoORMLiteActivity extends OrmLiteBaseListActivity<DatabaseHelper> {

	private ListView listView;
	private PersonAdaptor listAdapter;
	private DemoRepository demoRepository;

	private List<Person> persons;

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		this.demoRepository = new DemoRepository(getHelper());

		// Simply clear down all test data on every run
		this.demoRepository.clearData();

		createFakeEntries();

		this.persons = this.demoRepository.getPersons();

		findAndCreateAllViews();

		this.listAdapter = new PersonAdaptor(this, R.layout.person_data_row, this.persons);

		this.listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long duration) {
				final Person person = DemoORMLiteActivity.this.listAdapter.getItem(position);
				final ForeignCollection<App> apps = person.getApps();
				final StringBuilder appList = new StringBuilder();
				for (final App app : apps) {
					appList.append(app.getName())
						.append("\n");
				}
				new AlertDialog.Builder(DemoORMLiteActivity.this).setTitle(
						String.format("%s has a total of %s Apps", person.getName(), apps.size()))
					.setMessage(appList.toString())
					.show();
			}
		});

		this.listView.setAdapter(this.listAdapter);
		registerForContextMenu(this.listView);
	}

	private static final int MENU_ADD_APP = Menu.FIRST;
	private static final int MENU_DELETE_PERSON = Menu.FIRST + 1;
	private static final int MENU_EDIT_PERSON = Menu.FIRST + 2;

	@Override
	public void onCreateContextMenu(final ContextMenu menu, final View v, final ContextMenuInfo menuInfo) {
		if (v.getId() == getListView().getId()) {
			final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
			final Person person = this.persons.get(info.position);
			menu.setHeaderTitle(person.getId() + " | " + person.getName());
			menu.add(Menu.NONE, MENU_ADD_APP, MENU_ADD_APP, "Add App");
			menu.add(Menu.NONE, MENU_DELETE_PERSON, MENU_DELETE_PERSON, "Delete Person");
			menu.add(Menu.NONE, MENU_EDIT_PERSON, MENU_EDIT_PERSON, "Edit Person");
		}
	}

	@Override
	public boolean onContextItemSelected(final MenuItem item) {
		final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
		final int menuItemIndex = item.getItemId();
		final Person person = this.persons.get(info.position);

		final LayoutInflater factory = LayoutInflater.from(this);
		final View textEntryView = factory.inflate(R.layout.alert_dialog_text_entry, null);
		final EditText editText = (EditText) textEntryView.findViewById(R.id.edit_text_dialog);

		switch (menuItemIndex) {
			case MENU_ADD_APP:
				new AlertDialog.Builder(this).setTitle("Add App")
					.setView(textEntryView)
					.setPositiveButton("Add", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(final DialogInterface dialog, final int whichButton) {
							final App app = new App();
							app.setPerson(person);
							app.setName(editText.getText()
								.toString());
							DemoORMLiteActivity.this.demoRepository.saveOrUpdateApp(app);
							reloadData();
						}
					})
					.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(final DialogInterface dialog, final int whichButton) {
							/* User clicked cancel so do some stuff */
						}
					})
					.show();
				break;
			case MENU_DELETE_PERSON:
				this.persons.remove(info.position);
				this.demoRepository.deletePerson(person);
				this.listAdapter.notifyDataSetChanged();
				break;
			case MENU_EDIT_PERSON:
				// Set name for editing
				editText.setText(person.getName());

				new AlertDialog.Builder(this).setTitle("Edit Person")
					.setView(textEntryView)
					.setPositiveButton("Update", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(final DialogInterface dialog, final int whichButton) {
							person.setName(editText.getText()
								.toString());
							DemoORMLiteActivity.this.demoRepository.saveOrUpdatePerson(person);
							reloadData();
						}
					})
					.show();

				break;
			default:
				break;
		}
		return true;
	}

	private void reloadData() {
		this.persons = this.demoRepository.getPersons();
		this.listAdapter.notifyDataSetChanged();
	}

	private void createFakeEntries() {
		// Create Two test Persons
		final Person person = new Person();
		person.setName("James");
		this.demoRepository.saveOrUpdatePerson(person);

		final Person person2 = new Person();
		person2.setName("Jimmy");
		this.demoRepository.saveOrUpdatePerson(person2);

		// Create two test apps
		final App app = new App();
		app.setName("Whos Making The Brew");
		app.setPerson(person);
		this.demoRepository.saveOrUpdateApp(app);

		final App app2 = new App();
		app2.setName("Whos Making The Brew");
		app2.setPerson(person2);
		this.demoRepository.saveOrUpdateApp(app2);
	}

	public void findAndCreateAllViews() {
		this.listView = getListView();
		registerForContextMenu(this.listView);
	}
}
