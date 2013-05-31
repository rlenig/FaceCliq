package com.example.facecliq;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import android.widget.ListAdapter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.facecliq.Helper.MySimpleAdapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class HomeActivity extends Activity {
	JSONObject json;
	jsonParser jParser = new jsonParser();

	//JSON Node names
	private static String TAG_USER = "username";
	private static String TAG_NAME= "name";

	private static String displayName;

	static ArrayList<HashMap<String, String>> friendList;

	private String[] drawer_options;

	private ListView mDrawerList;
	private static ListView mCliqList;
	private static TextView listName; 
	static Helper helpFragment;
	static MySimpleAdapter	myAdapter;

	ListAdapter adapter;


	private static String[] friends_array;



	private DrawerLayout mDrawerLayout;
	private ActionBarDrawerToggle mDrawerToggle;
	private CharSequence mDrawerTitle;
	private CharSequence mTitle;

	//URLS
	private static String friendsURL = "http://facecliq.co/api/buddypressread/friends_get_friends/?" +
			"json=buddypressread/friends_get_friends&dev=1&controller=buddypressread&method=friends_get_friends&username=";

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		//get username from intent bundle
		displayName= getIntent().getExtras().getString("username");
		Log.e("INTENT", displayName);

		//execute background task
		getFriendsTask findFriends = new getFriendsTask();
		findFriends.execute();

		
		friendList = new ArrayList<HashMap<String, String>>();
		
		helpFragment = new Helper(this, mCliqList, friendList);
		
		mTitle = mDrawerTitle = getTitle();
		drawer_options = getResources().getStringArray(R.array.drawer_array);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);

		// set a custom shadow that overlays the main content when the drawer opens
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

		// set up the drawer's list view with items and click listener
		mDrawerList.setAdapter(new ArrayAdapter<String>(this,
				R.layout.drawer_list_item, 
				drawer_options));

		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

		// enable ActionBar app icon to behave as action to toggle nav drawer
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		// ActionBarDrawerToggle ties together the the proper interactions
		// between the sliding drawer and the action bar app icon
		mDrawerToggle = new ActionBarDrawerToggle(
				this,                  /* host Activity */
				mDrawerLayout,         /* DrawerLayout object */
				R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
				R.string.drawer_open,  /* "open drawer" description for accessibility */
				R.string.drawer_close  /* "close drawer" description for accessibility */
				) {
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(mTitle);
				invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
			}

			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(mDrawerTitle);
				invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		if (savedInstanceState == null) {
			selectItem(0);
		}
	}//end of onCreate method

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	/* Called whenever we call invalidateOptionsMenu() */
	@Override
	public boolean onPrepareOptionsMenu(Menu main) {
		// If the nav drawer is open, hide action items related to the content view
		//boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
		//main.findItem(R.id.action_search).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(main);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// The action bar home/up action should open or close the drawer.
		// ActionBarDrawerToggle will take care of this.
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		// Handle action buttons
		switch(item.getItemId()) {

		case R.id.submenu_exit:
			finish();
			return true;
		case R.id.action_refresh:
			//Reload
			return true;

		case R.id.action_notifi:
			//change layout to personal
			//notifactions layout
			selectItem(2);

			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/* The click listner for ListView in the navigation drawer */
	private class DrawerItemClickListener implements ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			selectItem(position);
		}
	}

	private void selectItem(int position) {
		// update the main content by replacing fragments
		// Create fragment and give it an argument for the selected 

		Fragment fragment = new ContentFragment();

		//creates a bundle to be passed to FragmentManager
		Bundle args = new Bundle();
		args.putInt(ContentFragment.ARG_SELECTED_NUM, position);
		fragment.setArguments(args);

		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

		// update selected item and title, then close the drawer
		mDrawerList.setItemChecked(position, true);
		setTitle(drawer_options[position]);
		mDrawerLayout.closeDrawer(mDrawerList);
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(mTitle);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
	}
	public class getFriendsTask extends AsyncTask <String, Void, JSONObject>{

		@Override
		protected JSONObject doInBackground(String... params) {

			String newURL= friendsURL + displayName;

			//get response JSON Object
			Log.e("URL", newURL);
			json = jParser.getJSONfromURL(newURL);
			Log.e("JSON",json + " ");	
			return json;

		}

		protected void onPostExecute(JSONObject s) {
			super.onPostExecute(s);

			String user= null; 
			try {

				int friendCount = s.getInt("count");
				//get friends array

				s = s.getJSONObject("friends");
				Log.e("Users", s + "");

				for(int i=1; i < friendCount + 1; i++){
					if(s.getJSONObject(i + "").getString(TAG_USER) != null){
						user = s.getJSONObject(i + "").getString(TAG_USER);
						
						addFriendtoList(user);
						Log.e("User", user);
					}

				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

	}//getFriendsTask()

	public void addFriendtoList(String name){

		//create new HashMap
		HashMap<String,String> map = new HashMap<String, String>();

		//add each child node to HashMap key
		map.put(TAG_USER, name);

		//adding HashList to ArrayList
		friendList.add(map);
		
		//helpFragment = new Helper(this, mCliqList, friendList);
	}

	/**
	 * Fragment that appears in the "content_frame"
	 */
	public static class ContentFragment extends Fragment {
		public static final String ARG_SELECTED_NUM = "planet_number";
		public static View convertView, rootView;
		
		public ContentFragment() {
			// Empty constructor required for fragment subclasses

		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup ScrollView,
				Bundle savedInstanceState) {
			



			int i = getArguments().getInt(ARG_SELECTED_NUM);
			//context = getActivity().getBaseContext();

			String drawer_title = getResources().getStringArray(R.array.drawer_array)[i];

			switch(i){

			case 0:  rootView = inflater.inflate(R.layout.cliq_stream, ScrollView, false);
				
					getActivity().setTitle(drawer_title);
			break;

			case 1:  rootView= inflater.inflate(R.layout.cliq_fragment, ScrollView, false);
					
			mCliqList = (ListView) rootView.findViewById(R.id.myCliqList);
			listName =(TextView) rootView.findViewById(R.id.cliqitem);
			helpFragment.display();
			
			getActivity().setTitle(drawer_title);
			break;	

			case 2:	rootView = inflater.inflate(R.layout.cliq_tivity, ScrollView, false);
			getActivity().setTitle(drawer_title);
			break;	

			case 3:	rootView = inflater.inflate(R.layout.cliq_tags, ScrollView, false);
			getActivity().setTitle(drawer_title);
			break;

			case 4:	rootView = inflater.inflate(R.layout.cliq_search, ScrollView, false);
			getActivity().setTitle(drawer_title);
			break;	

			}

			return rootView;
			
		}


	}//end of contentfragment()

	

}

