package com.revenco.daemonsdk.java.accounts;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.accounts.NetworkErrorException;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.revenco.daemonsdk.DaemonManager;
import com.revenco.daemonsdk.R;
import com.revenco.daemonsdk.utils.XLog;

/**
 * 账户服务
 */
public class LiveAccountService extends Service {
    private static final String TAG = Debugger.TAG;
    private LiveAuthenticator authenticator;

    public LiveAccountService() {
    }

    public LiveAuthenticator getAuthenticator() {
        Log.d(TAG, "getAuthenticator() called ");
        if (authenticator == null)
            authenticator = new LiveAuthenticator(this);
        return authenticator;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate() called ");
        authenticator = new LiveAuthenticator(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind() called ");
        return getAuthenticator().getIBinder();
    }

    //建立账号系统
    class LiveAuthenticator extends AbstractAccountAuthenticator {
        private final Context context;
        private AccountManager accountManager;

        public LiveAuthenticator(Context context) {
            super(context);
            Log.d(TAG, "LiveAuthenticator() called ");
            this.context = context;
            accountManager = AccountManager.get(context);
        }

        @Override
        public Bundle editProperties(AccountAuthenticatorResponse response, String accountType) {
            Log.d(TAG, "editProperties() called ");
            return null;
        }

        @Override
        public Bundle addAccount(AccountAuthenticatorResponse response, String accountType, String authTokenType, String[] requiredFeatures, Bundle options) throws NetworkErrorException {
            XLog.log2Sdcard(TAG, "addAccount() called ");
            final Intent intent = new Intent(context, AuthenticatorActivity.class);
            authTokenType = context.getResources().getString(R.string.account_auth_type);
            intent.putExtra(AuthenticatorActivity.PARAM_AUTHTOKEN_TYPE, authTokenType);
            intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);
            final Bundle bundle = new Bundle();
            bundle.putParcelable(AccountManager.KEY_INTENT, intent);
            DaemonManager.INSTANCE.SendSDKWakeUpBroadcast(context,null);
            return bundle;
        }

        @Override
        public Bundle confirmCredentials(AccountAuthenticatorResponse response, Account
                account, Bundle options) throws NetworkErrorException {
            Log.d(TAG, "confirmCredentials() called ");
            if (options != null && options.containsKey(AccountManager.KEY_PASSWORD)) {
                final String password =
                        options.getString(AccountManager.KEY_PASSWORD);
                final boolean verified = true;
                final Bundle result = new Bundle();
                result.putBoolean(AccountManager.KEY_BOOLEAN_RESULT, verified);
                return result;
            }
            // Launch AuthenticatorActivity to confirm credentials
            final Intent intent = new Intent(context, AuthenticatorActivity.class);
            intent.putExtra(AuthenticatorActivity.PARAM_USERNAME, account.name);
            intent.putExtra(AuthenticatorActivity.PARAM_CONFIRMCREDENTIALS, true);
            intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE,
                    response);
            final Bundle bundle = new Bundle();
            bundle.putParcelable(AccountManager.KEY_INTENT, intent);
            return bundle;
        }

        @Override
        public Bundle getAuthToken(AccountAuthenticatorResponse response, Account
                account, String authTokenType, Bundle options) throws NetworkErrorException {
            Log.d(TAG, "getAuthToken() called ");
            if (!authTokenType.equals(Constants.AUTHTOKEN_TYPE)) {
                final Bundle result = new Bundle();
                result.putString(AccountManager.KEY_ERROR_MESSAGE, "invalid authTokenType");
                return result;
            }
            final AccountManager am = AccountManager.get(context);
            final String password = am.getPassword(account);
            if (password != null) {
                final boolean verified = true;
                if (verified) {
                    final Bundle result = new Bundle();
                    result.putString(AccountManager.KEY_ACCOUNT_NAME, account.name);
                    result.putString(AccountManager.KEY_ACCOUNT_TYPE,
                            Constants.ACCOUNT_TYPE);
                    result.putString(AccountManager.KEY_AUTHTOKEN, password);
                    return result;
                }
            }
            // the password was missing or incorrect, return an Intent to an
            // Activity that will prompt the user for the password.
            final Intent intent = new Intent(context, AuthenticatorActivity.class);
            intent.putExtra(AuthenticatorActivity.PARAM_USERNAME, account.name);
            authTokenType = context.getResources().getString(R.string.account_auth_type);
            intent.putExtra(AuthenticatorActivity.PARAM_AUTHTOKEN_TYPE, authTokenType);
            intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);
            final Bundle bundle = new Bundle();
            bundle.putParcelable(AccountManager.KEY_INTENT, intent);
            return bundle;
        }

        @Override
        public String getAuthTokenLabel(String authTokenType) {
            Log.d(TAG, "getAuthTokenLabel() called ");
            return null;
        }

        @Override
        public Bundle updateCredentials(AccountAuthenticatorResponse response, Account
                account, String authTokenType, Bundle options) throws NetworkErrorException {
            Log.d(TAG, "updateCredentials() called ");
            return null;
        }

        @Override
        public Bundle hasFeatures(AccountAuthenticatorResponse response, Account
                account, String[] features) throws NetworkErrorException {
            Log.d(TAG, "hasFeatures() called ");
            return null;
        }
    }
}
