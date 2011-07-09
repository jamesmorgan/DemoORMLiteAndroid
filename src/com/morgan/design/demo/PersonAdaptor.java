package com.morgan.design.demo;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.morgan.design.demo.domain.Person;

public class PersonAdaptor extends ArrayAdapter<Person> {

	private final String LOG_TAG = "PersonAdapter";

	private final List<Person> persons;
	private final Activity context;

	public PersonAdaptor(final Activity context, final int textViewResourceId, final List<Person> persons) {
		super(context, textViewResourceId, persons);
		this.context = context;
		this.persons = persons;
	}

	@Override
	public View getView(final int position, final View convertView, final ViewGroup parent) {
		View view = convertView;
		ViewHolder holder;
		if (view == null) {
			final LayoutInflater vi = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = vi.inflate(R.layout.person_data_row, null);

			holder = new ViewHolder();
			holder.name = (TextView) view.findViewById(R.id.name);
			holder.numberOfApps = (TextView) view.findViewById(R.id.number_of_apps);
			view.setTag(holder);
		}
		else {
			holder = (ViewHolder) view.getTag();
		}

		final Person person = this.persons.get(position);
		Log.d(this.LOG_TAG, person.getName());

		if (person != null) {
			final TextView name = (TextView) view.findViewById(R.id.name);
			name.setText(person.getName());

			final TextView numOfApps = (TextView) view.findViewById(R.id.number_of_apps);
			numOfApps.setText(Integer.toString(person.getApps()
				.size()));
		}

		return view;
	}

	class ViewHolder {
		TextView name;
		TextView numberOfApps;
	}

}
