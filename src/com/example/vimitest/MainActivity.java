package com.example.vimitest;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vimitest.db.ContactsManager;
import com.example.vimitest.entity.Category;

public class MainActivity extends ActionBarActivity {

	private SharedPreferences sharedPref;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		sharedPref = this.getSharedPreferences("me.vamei.vamei",
				Context.MODE_PRIVATE);
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}

	}

	@Override
	protected void onResume() {
		super.onResume();
		TextView nameView = (TextView) findViewById(R.id.welcome);

		// retrieve content from shared preference, with key "name"
		String welcome = "Welcome, " + sharedPref.getString("name", "unknown")
				+ "!";
		nameView.setText(welcome);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		Log.i("run", "option menu run?" + menu.size() + "???"
				+ menu.getItem(0).getTitle());
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		switch (id) {
		case R.id.action_download:
			Thread thread = new Thread() {
				@Override
				public void run() {
					try {
						// Http Get
						InputStream content;
						HttpClient httpclient = new DefaultHttpClient();
						HttpResponse response = httpclient
								.execute(new HttpGet(
										"http://files.cnblogs.com/vamei/android_contact.js"));
						content = response.getEntity().getContent();
						BufferedReader reader = new BufferedReader(
								new InputStreamReader(content));
						final StringBuilder sb = new StringBuilder();
						String line = null;

						while ((line = reader.readLine()) != null) {
							sb.append(line);
						}
						content.close();

						// Parse JSON Object and Save to DB
						JSONObject receivedObject = new JSONObject(
								sb.toString());
						JSONArray categoryObjects = receivedObject
								.getJSONArray("category");

						ContactsManager cm = new ContactsManager(
								getApplicationContext());
						JSONObject categoryObject;
						for (int i = 0; i < categoryObjects.length(); i++) {
							categoryObject = categoryObjects.getJSONObject(i);
							String name = categoryObject.getString("name");
							Category category = new Category(name);
							cm.addCateGory(category);
						}
					} catch (Exception e) {
						Log.i("Http Error", e.getMessage().toString());
					}
				}
			};
			thread.start();
			break;
		// upload action
		case R.id.action_upload:
			UploadTask newTask = new UploadTask();
			newTask.execute("http://files.cnblogs.com/vamei/android_contact.js");
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment implements
			OnClickListener {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			// ContactsManager cm = new ContactsManager(container.getContext());
			//
			// // add categories to the database
			// cm.addCateGory(new Category("Friend"));
			// cm.addCateGory(new Category("Enermy"));
			//
			// // add contact to the database
			// Category cat1 = cm.getCategory(1);
			// cm.addContact(new Contact("vamei",
			// "http://www.cnblogs.com/vamei",
			// cat1));
			//
			// // retrieve and display
			// Toast.makeText(container.getContext(),
			// cm.getContact(1).getName(),
			// Toast.LENGTH_LONG).show();
			Button btn1 = (Button) rootView.findViewById(R.id.author);
			btn1.setOnClickListener(this);
			Button btn2 = (Button) rootView.findViewById(R.id.category);
			btn2.setOnClickListener(this);
			return rootView;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent;
			// Routing to different view elements
			switch (v.getId()) {
			case R.id.author:
				intent = new Intent(this.getActivity(), SelfEditActivity.class);
				startActivity(intent);
				break;
			case R.id.category:
				intent = new Intent(this.getActivity(), CategoryActivity.class);
				startActivity(intent);
				break;
			}
		}

	}

	private class UploadTask extends AsyncTask<String, String, String> {
		/* main worker */
		@Override
		protected String doInBackground(String... params) {
			ContactsManager cm = new ContactsManager(getApplicationContext());
			List<Category> categories = cm.getCategories();
			JSONObject sendObject = new JSONObject();
			JSONArray categoryObjects = new JSONArray();
			try {
				for (int i = 0; i < categories.size(); i++) {
					JSONObject categoryObject = new JSONObject();
					categoryObject.put("name", categories.get(i).getName());
					categoryObjects.put(categoryObject);
				}
				sendObject.put("category", categoryObjects);
				// update progress once
				publishProgress("JSON DONE");

				// posting to URL
				HttpClient httpClient = new DefaultHttpClient();
				HttpPost httpPost = new HttpPost(params[0]);
				StringEntity se = new StringEntity(sendObject.toString());
				se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,
						"application/json"));
				httpPost.setEntity(se);
				HttpResponse httpResponse = httpClient.execute(httpPost);
				// update progress again
				publishProgress("NETWORK DONE");

				return httpResponse.getStatusLine().toString();
			} catch (Exception e) {
				e.printStackTrace();
				return "Crashed";
			}
		}

		/* after background work is done */
		@Override
		protected void onPostExecute(String result) {
			Toast.makeText(MainActivity.this, result, Toast.LENGTH_LONG).show();
		}

		/* when progress is updated */
		@Override
		protected void onProgressUpdate(String... params) {
			Toast.makeText(MainActivity.this, params[0], Toast.LENGTH_SHORT)
					.show();
		}
	}

}
