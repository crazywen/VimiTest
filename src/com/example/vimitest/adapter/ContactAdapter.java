package com.example.vimitest.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.vimitest.R;
import com.example.vimitest.entity.Contact;

public class ContactAdapter extends ArrayAdapter<Contact> {

	private int viewId;
	private Context context;
	private List<Contact> contacts;

	public ContactAdapter(Context context, int viewId, List<Contact> contacts) {
		super(context, viewId, contacts);
		// TODO Auto-generated constructor stub
		this.context = context;
		this.viewId = viewId;
		this.contacts = contacts;
	}

	private class Holder {
		TextView tv1;

		public Holder(TextView tv1) {
			this.tv1 = tv1;
		}
	}

	// For each row, control the view assigned to the data
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View v = convertView;
		Holder holder;
		if (v == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(viewId, parent, false);
			holder = new Holder((TextView) v.findViewById(R.id.con_name));
			v.setTag(holder);
		} else {
			holder = (Holder) v.getTag();
		}

		Contact contact = contacts.get(position);

		// add data to views
		if (contact != null) {
			holder.tv1.setText(contact.getName());
		}
		// Add data to views
		// if (category != null) {
		// TextView tv1 = (TextView) v.findViewById(R.id.seq);
		// if (tv1 != null) {
		// tv1.setText(Integer.toString(category.getId()));
		// }
		//
		// TextView tv2 = (TextView) v.findViewById(R.id.name);
		// if (tv2 != null) {
		// tv2.setText(category.getName());
		// }
		// }

		return v;
	}

}
