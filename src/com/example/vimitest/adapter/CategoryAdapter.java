package com.example.vimitest.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.vimitest.R;
import com.example.vimitest.entity.Category;

public class CategoryAdapter extends ArrayAdapter<Category> {

	private int viewId;
	private Context context;
	private List<Category> categories;

	public CategoryAdapter(Context context, int viewId,
			List<Category> categories) {
		super(context, viewId, categories);
		// TODO Auto-generated constructor stub
		this.context = context;
		this.viewId = viewId;
		this.categories = categories;
	}

	private class Holder {
		TextView tv1;
		TextView tv2;

		public Holder(TextView tv1, TextView tv2) {
			this.tv1 = tv1;
			this.tv2 = tv2;
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
			holder = new Holder((TextView) v.findViewById(R.id.seq),
					(TextView) v.findViewById(R.id.name));
			v.setTag(holder);
		} else {
			holder = (Holder) v.getTag();
		}

		Category category = categories.get(position);
		
		// add data to views
        if(category != null) {
            holder.tv1.setText(Integer.toString(category.getId())); 
            holder.tv2.setText(category.getName()); 
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
