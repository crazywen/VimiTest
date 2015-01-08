package com.example.vimitest;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.vimitest.db.ContactsManager;
import com.example.vimitest.entity.Category;
import com.example.vimitest.entity.Contact;

public class SelfEditActivity extends Activity {

	private EditText nameInput;
	private EditText urlInput;
	private TextView tvSubmit;

	private SharedPreferences sharedPref;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_self_edit);
		// find views
		tvSubmit = (TextView) findViewById(R.id.submit);
		nameInput = (EditText) findViewById(R.id.name);
		urlInput = (EditText) findViewById(R.id.url);

		sharedPref = this.getSharedPreferences("me.vamei.vamei",
				Context.MODE_PRIVATE);

		final ContactsManager cm = new ContactsManager(this);
		// submit
		tvSubmit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String name = nameInput.getText().toString();
				String url = urlInput.getText().toString();

				// Save to Shared Preferences
				SharedPreferences.Editor editor = sharedPref.edit();
				editor.putString("name", name);
				editor.putString("url", url);

				editor.commit();

				Category cg = cm.getCategory(name);
				if (null == cg) {
					cg = new Category(name);
					cm.addCateGory(cg);
					cg = cm.getCategory(name);
				}
				
				Contact contact = new Contact(name, url, cg);
				cm.addContact(contact);

				// End Current Activity
				SelfEditActivity.this.finish();

			}
		});
	}

}
