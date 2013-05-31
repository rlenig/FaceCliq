package com.example.facecliq;

import java.util.Iterator;
import org.json.JSONException;
import org.json.JSONObject;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings.Global;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

	protected static AccountManager manager;
	Account [] account;
	protected Intent intent;

	JSONObject json;
	jsonParser jParser = new jsonParser();

	static String base= "http://facecliq.co/api/auth/generate_auth_cookie" +
			"/?json=get_nonce&dev=1&controller=auth&method=generate_auth_cookie&username=";

	static String start="http://facecliq.co/api/auth/generate_auth_cookie/?json=auth/generate_auth_cookie&dev=1&nonce=";

	static String second="&controller=auth&method=generate_auth_cookie&username=";
	static String last="&password=";

	static String nonceID;
	static String value=null;
	static String cookie;
	static Boolean error;
	String nonce;
	static String key= null;

	String  url;
	static String userName;
	static String passWord;
	static EditText user_Text, pswd_Text;
	Button login;

	@SuppressLint("NewApi")
	@Override

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	     manager = AccountManager.get(this);
	     //Account [] account = manager.getAccountsByType("com.example.facecliq");
	     getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		setContentView(R.layout.login);
		setTitle("Login");
	}
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		MenuInflater menuInflater =	getMenuInflater();
		menuInflater.inflate(R.menu.register, menu);
		return true;
	}
	//listener to register
	public void registerListener(View v) {
		Intent startRegister = new Intent (MainActivity.this, RegisterActivity.class);		
		MainActivity.this.startActivity(startRegister);
	}

	//listener to login
	public void loginListener(View v) {
		//should get token method
		GetCookieTask getCookie = new GetCookieTask();
		getCookie.execute();
	}


	public class GetCookieTask extends AsyncTask<String,Void,Boolean>{

		private ProgressDialog dialog = new ProgressDialog(MainActivity.this);

		protected void onPreExecute() {
			dialog = new ProgressDialog(MainActivity.this);
			dialog.setMessage("Loging in... ");
			dialog.show();
		}

		protected Boolean doInBackground(final String... args) {

			error = false;

			//get textfields 
			user_Text = (EditText)findViewById(R.id.userNameLabel);
			pswd_Text = (EditText)findViewById(R.id.passwordLabel);
			login = (Button)findViewById(R.id.btnLogin);

			//parse to string
			userName =user_Text.getText().toString();
			passWord =pswd_Text.getText().toString();

			//add them together
			url = base + userName+"&password="+passWord;
			Log.e("URL1",url);

			try{

				//get response JSON Object
				json = jParser.getJSONfromURL(url);
				Log.e("JSON",json + " ");

				@SuppressWarnings("unchecked")
				Iterator<String> myIter = json.keys();

				//will get nonceID everytime
				if(myIter.hasNext()){
					String key = (String)myIter.next();
					nonceID= json.getString(key);
				}
				Log.e("NONCEID",  nonceID);

				//create new url
				url =start+nonceID+second+userName+last+passWord;
				Log.e("URL2",url);
			}catch(JSONException e){
				e.printStackTrace();
			}


			//use new url to get next response
			//use nonceID to verify
			//if true you get a cookie

			try{
				json = jParser.getJSONfromURL(url);
				Log.e("JSON",json + " ");

				@SuppressWarnings("unchecked")
				Iterator<String> myIter = json.keys();

				if(myIter.hasNext()){
					key = (String)myIter.next();
					value= json.getString(key);

					Log.e("VALUE",value);
					//if there's an error
					if (key.equals("cookie")){
						//need to prompt user
						error=false;

						Bundle bundle = new Bundle();
						bundle.putString("cookie", value);
						bundle.putString("nonce", nonceID);

						
						Log.e("BUN",value+ nonceID);

						Account myAccount = new Account(userName, "com.example.facecliq");
						manager.addAccountExplicitly(myAccount, passWord, bundle);
						//add that account explicitly to the AccountManager

					}else{
						error = true;
					}
				}
				Log.e("MAP",key +value);

			}catch(JSONException e){
				Log.e("JSON WTF", e.toString());
			}catch(Exception e){
				Log.e("WTF", e.toString());
			}
			return error;
		}//end doInBackground

		protected void onPostExecute(Boolean error){

			if (dialog.isShowing())
				dialog.dismiss();

			if(error){
				Toast.makeText(getApplicationContext(), "Invalid username and or/password", Toast.LENGTH_LONG).show();
				user_Text.setText("");
				pswd_Text.setText("");
			}else{
				
				Intent start = new Intent (MainActivity.this, HomeActivity.class);		
				Bundle mBundle = new Bundle();
				mBundle.putString("username", userName);
				mBundle.putString("nonce", nonceID);
				start.putExtras(mBundle);
				MainActivity.this.startActivity(start);
				
				
			}

		}//end onPostExecute

	}//End GetCookieTask


}
