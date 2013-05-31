package com.example.facecliq;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.accounts.NetworkErrorException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class AccountAuthenticator extends AbstractAccountAuthenticator{

	private Context mContext;


	public static final String PARAM_AUTHTOKEN_TYPE = "auth.token";  
	public static final String PARAM_CREATE = "create";  

	public static final int REQ_CODE_CREATE = 1;  

	public static final int REQ_CODE_UPDATE = 2;  

	public static final String EXTRA_REQUEST_CODE = "req.code";  

	public static final int RESP_CODE_SUCCESS = 0;  

	public static final int RESP_CODE_ERROR = 1;  

	public static final int RESP_CODE_CANCEL = 2;  

	public AccountAuthenticator(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Bundle editProperties(
			AccountAuthenticatorResponse paramAccountAuthenticatorResponse,
			String paramString) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Bundle addAccount(
			AccountAuthenticatorResponse response,
			String accountType, String authTokenType,
			String[]requiredFeatures, Bundle options)
					throws NetworkErrorException {

		final Bundle result;
		final Intent intent;
		intent = new Intent(this.mContext, MainActivity.class);
		intent.putExtra(AccountManager.KEY_AUTHTOKEN, authTokenType);  
		intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);  

		result = new Bundle();  
		result.putParcelable(AccountManager.KEY_INTENT, intent);
		return result;
	}

	@Override
	public Bundle confirmCredentials(
			AccountAuthenticatorResponse paramAccountAuthenticatorResponse,
			Account paramAccount, Bundle paramBundle)
					throws NetworkErrorException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Bundle getAuthToken(
			AccountAuthenticatorResponse paramAccountAuthenticatorResponse,
			Account paramAccount, String paramString, Bundle paramBundle)
					throws NetworkErrorException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getAuthTokenLabel(String paramString) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Bundle updateCredentials(
			AccountAuthenticatorResponse paramAccountAuthenticatorResponse,
			Account paramAccount, String paramString, Bundle paramBundle)
					throws NetworkErrorException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Bundle hasFeatures(
			AccountAuthenticatorResponse paramAccountAuthenticatorResponse,
			Account paramAccount, String[] paramArrayOfString)
					throws NetworkErrorException {
		// TODO Auto-generated method stub
		return null;
	}

}
