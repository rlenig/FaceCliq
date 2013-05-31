package com.example.facecliq;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class RegisterActivity extends Activity {

	static String base= "https://facecliq.co/api/?";

	String userName, passWord, email, firstname, lastname,url;
	EditText user_Text, pswd_Text, first_Text, last_Text, email_Text;
	Button create;
	JSONObject json;
	jsonParser jParser = new jsonParser();
	static Boolean flag;
	String success = "0";


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle("Register");
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		setContentView(R.layout.activity_register);


	}

	public void createAccountListener(View v){
		Request request = new Request();
		request.execute();
		//request.onPostExecute();

		
	}


	public class Request extends AsyncTask<String,Void,Boolean>{
		private ProgressDialog dialog = 
				new ProgressDialog(RegisterActivity.this);


		protected void onPreExecute() {
			dialog = new ProgressDialog(RegisterActivity.this);
			dialog.setMessage("Creating account real quick... Please wait...");
			dialog.show();
		}

		protected Boolean doInBackground(final String... args) {
			
			//get text from textboxes
			user_Text = (EditText)findViewById(R.id.R_user);
			pswd_Text = (EditText)findViewById(R.id.R_pass);
			first_Text = (EditText)findViewById(R.id.R_first);
			last_Text = (EditText)findViewById(R.id.R_last);
			email_Text = (EditText)findViewById(R.id.R_email);
			create = (Button)findViewById(R.id.btnRegister);

			//make them into strings
			userName = user_Text.getText().toString();
			passWord = pswd_Text.getText().toString();
			firstname = first_Text.getText().toString();
			lastname = last_Text.getText().toString();
			email =email_Text.getText().toString();

			//add them together
			url = base + "userName="+userName+"&email="+email
					+"&password="+passWord+"&firstName="+firstname
					+"&lastName="+lastname;



			System.out.println("url: "+url );
			try{
				URLConnection connection = new URL(url).openConnection();
				BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()), 2048 * 16);
				StringBuffer builder = new StringBuffer();
				String line;
				
				if( connection != null){

					while ((line = reader.readLine()) != null) {
						builder.append(line).append("\n");
					}

					String blah = builder.toString();
					System.out.print("response" + blah);
					
					//did it register successfully?
					//does it have 0
					if(blah.contains(success)){
					flag = true;}
					//send error message
					else{
						flag = false;
					}
					//clear fields?
					
					//reader.close();
				}
				
				
			} catch(Exception e){
				Log.e("Http Error","Error in http connection " + e.toString());
				
			}
			System.out.print(flag);
			return flag;

			//return flag;
		}//end doInBackground

		protected void onPostExecute(Boolean flag){
			
			System.out.print("do we even get here?");
			System.out.print(flag);
			
			
			 if (dialog.isShowing()) {
		            dialog.dismiss();
		        }
			 //if there's a success code
			if(flag){
				//Intent start = new Intent (RegisterActivity.this, Buddypress.class);		
				//RegisterActivity.this.startActivity(start);
				Intent i = new Intent(RegisterActivity.this, Buddypress.class);
				i.putExtra("username", user_Text.getText().toString());
				i.putExtra("password", pswd_Text.getText().toString());
				setResult(Activity.RESULT_OK, i);
			}else{
				Toast.makeText(getApplicationContext(), "Login Not Successful !!!", Toast.LENGTH_LONG).show();
			}
			
		}//end onPostExecute



	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.register, menu);
		return true;
	}

}
