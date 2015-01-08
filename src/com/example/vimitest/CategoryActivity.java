package com.example.vimitest;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.example.vimitest.adapter.CategoryAdapter;
import com.example.vimitest.db.ContactsManager;
import com.example.vimitest.entity.Category;

public class CategoryActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_category);
		ListView listView = (ListView) findViewById(R.id.categoryList);

		ContactsManager cm = new ContactsManager(this);

		final List<Category> categories = cm.getCategories();

		// transform data to a list of strings
		ArrayList<String> list = new ArrayList<String>();
		for (int i = 0; i < categories.size(); ++i) {
			list.add(categories.get(i).getName());
		}

		CategoryAdapter adapter = new CategoryAdapter(this,
				R.layout.list_category, categories);
		// ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
		// android.R.layout.simple_list_item_1, list);

		listView.setAdapter(adapter);

		// listView.setOnItemClickListener(new OnItemClickListener() {
		// public void onItemClick(AdapterView<?> parent, View view,
		// int position, long id) {
		// // When clicked, show a Toast text
		// Toast.makeText(getApplicationContext(),
		// "id:" + categories.get(position).getId(),
		// Toast.LENGTH_SHORT).show();
		// }
		// });
		
		listView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                Intent intent = new Intent(CategoryActivity.this, ContactListActivity.class);
                // put extra data into intent
                // the data will be passed along with the intent, as a key-value pair
                intent.putExtra("CATEGORY_ID", categories.get(position).getId());
                
                startActivity(intent);
            }
        });

	}

}
