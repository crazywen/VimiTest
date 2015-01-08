package com.example.vimitest;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.example.vimitest.adapter.ContactAdapter;
import com.example.vimitest.db.ContactsManager;
import com.example.vimitest.entity.Contact;

public class ContactListActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_contact_list);

		// Get extra data from the Intent
		// i.e., category id
		Intent intent = getIntent();
		int cat_id = intent.getIntExtra("CATEGORY_ID", -1);

		ContactsManager cm = new ContactsManager(this);
		final List<Contact> contacts = cm.getContactsByCategoryId(cat_id);

		ListView listview = (ListView) findViewById(R.id.contactList);

		ContactAdapter adapter = new ContactAdapter(this,
				R.layout.list_contact, contacts);

		listview.setAdapter(adapter);

		listview.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(ContactListActivity.this,
						BlogActivity.class);
				// Put extra data into the Intent, which is a URL
				intent.putExtra("BLOG_URL", contacts.get(position).getUrl());

				startActivity(intent);
			}
		});

	}

}
