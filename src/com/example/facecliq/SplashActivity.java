package com.example.facecliq;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;

public class SplashActivity extends Activity {

	static String authBase ="http://facecliq.co/api/auth/validate_auth_cookie" +
			"/?json=auth/validate_auth_cookie&dev=1&nonce=";

	static String authLast="&controller=auth&method=validate_auth_cookie&cookie=";

	static String nonceBase= "http://facecliq.co/api/auth/generate_auth_cookie" +
			"/?json=get_nonce&dev=1&controller=auth&method=generate_auth_cookie&username=";

	static String url;

	static String token, nonceID;
	Account myAccount;
	Account [] account;
	AccountManager manager;
	Boolean wrong;
	static Boolean connection;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		connection= isNetworkConnected();

		if (connection ==false){
			final AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("Sorry, No Connection.")
			.setCancelable(false)
			.setPositiveButton("Retry?", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					if (connection ==false){
						AlertDialog alert = builder.create();
						alert.show();
						//Toast.makeText(getApplicationContext(), "No, connection available", Toast.LENGTH_LONG).show();
					}else{
						startLogin login = new startLogin();
						login.execute();
					}
				}
			})

			.setNegativeButton("Exit", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					finish();
				}
			});
			AlertDialog alert = builder.create();
			alert.show();


		}else{
			startLogin login = new startLogin();
			login.execute();
		}



	}//end of onCreate 

	private boolean isNetworkConnected() {
		ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		if (ni == null) {
			// There are no active networks.
			return false;
		} else
			return true;
	}

	public class startLogin extends AsyncTask<String,Void,Boolean>{

		@Override
		protected Boolean doInBackground(String... params) {
			//authenticate cookie
			Bundle packet = new Bundle();
			try{


				if(account!= null){
					myAccount= account[0];

					//already has account
					manager.getAuthToken(
							myAccount,                     // Account retrieved using getAccountsByType()
							"Manage your tasks",            // Auth scope
							packet,                        // Authenticator-specific options
							SplashActivity.this,             // Your activity
							new OnTokenAcquired(),          // Callback called when a token is successfully acquired
							null);   

					//verify token go to main screen
					wrong = false;

				}else{
					wrong = true;
				}


			}catch(Exception e){
				Log.e("VALID", wrong + " ");
			}
			return wrong;


		}
		protected void onPostExecute(Boolean wrong){
			if(wrong){
				//wrong go login
				Intent intent = new Intent (SplashActivity.this, MainActivity.class);		
				SplashActivity.this.startActivity(intent);
			}else{
				//everything worked go to home
				Intent intentHome = new Intent (SplashActivity.this, MainActivity.class);		
				SplashActivity.this.startActivity(intentHome);
			}
		}//end post

	}//end of task 
	private class OnTokenAcquired implements AccountManagerCallback<Bundle> {

		@Override
		public void run(AccountManagerFuture<Bundle> result) {
			// Get the result of the operation from the AccountManagerFuture.
			Bundle bundle;
			try{
				bundle = result.getResult();

				// The token is a named value in the bundle. The name of the value
				// is stored in the constant AccountManager.KEY_AUTHTOKEN.
				token = bundle.getString(AccountManager.KEY_AUTHTOKEN);

			}catch(Exception e){
				Log.e("ONTA",token); 

			}//end of tryCatch

		}//end of run()

	}

}