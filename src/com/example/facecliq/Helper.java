package com.example.facecliq;

import java.util.HashMap;
import java.util.List;

import com.example.facecliq.HomeActivity.ContentFragment;

import android.app.ListActivity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class Helper extends ListActivity {
	//private static String TAG_ID = "id";
	private static String TAG_NAME= "name";
	private  List<HashMap<String, String>> friendsArray;
	private Context currentView;
	private ListView myCliqList;
	
	public Helper(Context context, ListView view, List<HashMap<String, String>> arraylist){

		friendsArray = arraylist;
		currentView= context;
		myCliqList = view; 
	}

		public void display(){
			
		MySimpleAdapter myAdapter = new MySimpleAdapter(currentView,
				R.layout.cliq_list_item,
				R.id.cliqitem,
				friendsArray);
		
		setListAdapter(myAdapter);
		myAdapter.notifyDataSetChanged();
	}

	public class MySimpleAdapter extends ArrayAdapter<HashMap<String, String>> {

		List<HashMap<String, String>> listItems;

		public MySimpleAdapter(Context currentView, int resource, int
				textViewResourceId, List<HashMap<String, String>> objects) {

			super(currentView, resource, textViewResourceId, objects);
			listItems = objects;

		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			if(convertView == null) {

				LayoutInflater inflator = (LayoutInflater)
						getSystemService(Context.LAYOUT_INFLATER_SERVICE);

				convertView = inflator.inflate(R.layout.cliq_list_item, null);

			}

			TextView listName =(TextView) convertView.findViewById(R.id.cliqitem);

			//listName.setTag(listItems.get(position).get(TAG_ID));

			listName.setText(listItems.get(position).get(TAG_NAME));

			return convertView;

		}

	}


}
