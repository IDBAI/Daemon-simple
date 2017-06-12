/*
 * Copyright (C) 2010 The Android Open Source Project
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.revenco.daemon.java.accounts;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.revenco.daemon.utils.XLog;

/**
 * Activity which displays login screen to the user.
 * 添加账户的页面，官方默认提供的标准代码写法
 */
public class AuthenticatorActivity extends AccountAuthenticatorActivity {
    public static final String PARAM_CONFIRMCREDENTIALS = "confirmCredentials";
    public static final String PARAM_USERNAME = "username";
    public static final String PARAM_AUTHTOKEN_TYPE = "authtokenType";
    private static final String TAG = Debugger.TAG;
    /**
     * for posting authentication attempts back to UI thread
     */
    private final Handler mHandler = new Handler();
    /**
     * Was the original caller asking for an entirely new account?
     */
    protected boolean mRequestNewAccount = false;
    private AccountManager mAccountManager;
    private String mAuthtoken;
    private String mAuthtokenType;
    /**
     * If set we are just checking that the user knows their credentials; this doesn't cause the user's password to be changed on the device.
     */
    private Boolean mConfirmCredentials = false;

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCreate(Bundle icicle) {
        XLog.i(TAG, "onCreate(" + icicle + ")");
        super.onCreate(icicle);
        mAccountManager = AccountManager.get(this);
        XLog.i(TAG, "loading data from Intent");
        final Intent intent = getIntent();
        mAuthtokenType = intent.getStringExtra(PARAM_AUTHTOKEN_TYPE);
        mRequestNewAccount = true;
        mConfirmCredentials = intent.getBooleanExtra(PARAM_CONFIRMCREDENTIALS, false);
        XLog.i(TAG, "    request new: " + mRequestNewAccount);
        handleLogin();
    }

    /**
     * Handles onClick event on the Submit button. Sends username/password to the server for authentication.
     */
    private void handleLogin() {
        // Start authenticating...
        // 不验证账号，直接添加
        onAuthenticationResult(true);
    }

    /**
     * 确认证书请求
     * Called when response is received from the server for confirm credentials request.
     * <p>
     * See onAuthenticationResult(). Sets the AccountAuthenticatorResult which is sent back to the caller.
     *
     * @param result
     */
    protected void finishConfirmCredentials(boolean result) {
        XLog.i(TAG, "finishConfirmCredentials()");
        final Account account = new Account(Constants.mUsername, Constants.ACCOUNT_TYPE);
        mAccountManager.setPassword(account, Constants.mPassword);
        final Intent intent = new Intent();
        intent.putExtra(AccountManager.KEY_BOOLEAN_RESULT, result);
        setAccountAuthenticatorResult(intent.getExtras());
        setResult(RESULT_OK, intent);
        finish();
    }

    /**
     * Called when response is received from the server for authentication request. See onAuthenticationResult(). Sets the AccountAuthenticatorResult which is sent back to the caller. Also sets the authToken in AccountManager for this account.
     */
    protected void finishLogin() {
        final Account account = new Account(Constants.mUsername, Constants.ACCOUNT_TYPE);
        if (mRequestNewAccount) {
            XLog.i(TAG, "mAccountManager.addAccountExplicitly(account, Constants.mPassword, null); ");
            mAccountManager.addAccountExplicitly(account, Constants.mPassword, null);
            //设置让这个账号可以自己主动同步
            ContentResolver.setSyncAutomatically(account, LiveAccountProvider.AUTHORITY, true);
        } else {
            XLog.i(TAG, "mAccountManager.setPassword(account, Constants.mPassword); ");
            mAccountManager.setPassword(account, Constants.mPassword);
        }
        final Intent intent = new Intent();
        mAuthtoken = Constants.mPassword;
        intent.putExtra(AccountManager.KEY_ACCOUNT_NAME, Constants.mUsername);
        intent.putExtra(AccountManager.KEY_ACCOUNT_TYPE, Constants.ACCOUNT_TYPE);
        if (mAuthtokenType != null && mAuthtokenType.equals(Constants.AUTHTOKEN_TYPE)) {
            intent.putExtra(AccountManager.KEY_AUTHTOKEN, mAuthtoken);
        }
        setAccountAuthenticatorResult(intent.getExtras());
        setResult(RESULT_OK, intent);
        finish();
    }

    /**
     * Called when the authentication process completes (see attemptLogin()).
     */
    public void onAuthenticationResult(boolean result) {
        XLog.i(TAG, "onAuthenticationResult(" + result + ")");
        // Hide the progress dialog
        if (result) {
            if (!mConfirmCredentials) {
                try {
                    finishLogin();
                } catch (Exception e) {
                    e.printStackTrace();
                    finish();
                }
            } else {
                finishConfirmCredentials(true);
            }
        } else {
            XLog.e(TAG, "onAuthenticationResult: failed to authenticate");
        }
    }
}
