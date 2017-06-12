package com.revenco.daemon.java.accounts;

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

import com.revenco.daemon.utils.XLog;

/**
 * 账户服务 - 官方标准写法
 */
public class LiveAccountService extends Service {
    private static final String TAG = Debugger.TAG;
    private LiveAuthenticator authenticator;

    public LiveAccountService() {
    }

    public LiveAuthenticator getAuthenticator() {
        XLog.d(TAG, "getAuthenticator() called ");
        if (authenticator == null)
            authenticator = new LiveAuthenticator(this);
        return authenticator;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        XLog.d(TAG, "onCreate() called ");
        authenticator = new LiveAuthenticator(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        XLog.d(TAG, "onBind() called ");
        return getAuthenticator().getIBinder();
    }

    /**
     * 账户认证器
     * 建立账号系统 ,代码标准写法
     */
    class LiveAuthenticator extends AbstractAccountAuthenticator {
        private final Context context;
        private AccountManager accountManager;

        public LiveAuthenticator(Context context) {
            super(context);
            XLog.d(TAG, "LiveAuthenticator() called ");
            this.context = context;
            accountManager = AccountManager.get(context);
        }

        @Override
        public Bundle editProperties(AccountAuthenticatorResponse response, String accountType) {
            XLog.d(TAG, "editProperties() called ");
            return null;
        }

        /**
         * 添加账户
         *
         * @param response
         * @param accountType
         * @param authTokenType
         * @param requiredFeatures
         * @param options
         * @return
         * @throws NetworkErrorException
         */
        @Override
        public Bundle addAccount(AccountAuthenticatorResponse response, String accountType, String authTokenType, String[] requiredFeatures, Bundle options) throws NetworkErrorException {
            XLog.log2Sdcard(TAG, "addAccount() called ");
            final Intent intent = new Intent(context, AuthenticatorActivity.class);
            intent.putExtra(AuthenticatorActivity.PARAM_AUTHTOKEN_TYPE, authTokenType);
            intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);
            final Bundle bundle = new Bundle();
            bundle.putParcelable(AccountManager.KEY_INTENT, intent);
            return bundle;
        }

        /**
         * 确认用户知道证书凭证
         *
         * @param response
         * @param account
         * @param options
         * @return
         * @throws NetworkErrorException
         */
        @Override
        public Bundle confirmCredentials(AccountAuthenticatorResponse response, Account
                account, Bundle options) throws NetworkErrorException {
            XLog.d(TAG, "confirmCredentials() called ");
            if (options != null && options.containsKey(AccountManager.KEY_PASSWORD)) {
                final String password = options.getString(AccountManager.KEY_PASSWORD);
                final boolean verified = true;
                final Bundle result = new Bundle();
                result.putBoolean(AccountManager.KEY_BOOLEAN_RESULT, verified);
                return result;
            }
            // Launch AuthenticatorActivity to confirm credentials
            final Intent intent = new Intent(context, AuthenticatorActivity.class);
            intent.putExtra(AuthenticatorActivity.PARAM_USERNAME, account.name);
            //设置确认证书 PARAM_CONFIRMCREDENTIALS 为 true
            intent.putExtra(AuthenticatorActivity.PARAM_CONFIRMCREDENTIALS, true);
            intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE,
                    response);
            final Bundle bundle = new Bundle();
            bundle.putParcelable(AccountManager.KEY_INTENT, intent);
            return bundle;
        }

        /**
         * 获取授权token
         *
         * @param response
         * @param account
         * @param authTokenType
         * @param options
         * @return
         * @throws NetworkErrorException
         */
        @Override
        public Bundle getAuthToken(AccountAuthenticatorResponse response, Account
                account, String authTokenType, Bundle options) throws NetworkErrorException {
            XLog.d(TAG, "getAuthToken() called ");
            if (!authTokenType.equals(Constants.AUTHTOKEN_TYPE)) {
                final Bundle result = new Bundle();
                result.putString(AccountManager.KEY_ERROR_MESSAGE, "invalid authTokenType");
                return result;
            }
            final AccountManager am = AccountManager.get(context);
            final String password = am.getPassword(account);
            if (password != null) {
                //默认校验成功
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
            intent.putExtra(AuthenticatorActivity.PARAM_AUTHTOKEN_TYPE, authTokenType);
            intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);
            final Bundle bundle = new Bundle();
            bundle.putParcelable(AccountManager.KEY_INTENT, intent);
            return bundle;
        }

        @Override
        public String getAuthTokenLabel(String authTokenType) {
            XLog.d(TAG, "getAuthTokenLabel() called ");
            return null;
        }

        /**
         * 更新证书
         *
         * @param response
         * @param account
         * @param authTokenType
         * @param options
         * @return
         * @throws NetworkErrorException
         */
        @Override
        public Bundle updateCredentials(AccountAuthenticatorResponse response, Account
                account, String authTokenType, Bundle options) throws NetworkErrorException {
            XLog.d(TAG, "updateCredentials() called ");
            return null;
        }

        @Override
        public Bundle hasFeatures(AccountAuthenticatorResponse response, Account
                account, String[] features) throws NetworkErrorException {
            XLog.d(TAG, "hasFeatures() called ");
            return null;
        }
    }
}
