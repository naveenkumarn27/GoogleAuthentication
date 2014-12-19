package com.android.npgraphics.googleauthentication;

import android.accounts.AccountManager;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.npgraphics.googleauthentication.Authenticator.Constants;
import com.android.npgraphics.googleauthentication.Authenticator.GoogleAuthListener;
import com.android.npgraphics.googleauthentication.Authenticator.GoogleAuthenticator;
import com.android.npgraphics.googleauthentication.Authenticator.User;


public class AuthenticationActivity extends ActionBarActivity implements GoogleAuthListener,View.OnClickListener{

    private Button mGoogleAuthButton;
    private GoogleAuthenticator mGoogleAuthenticator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        mGoogleAuthButton = (Button) findViewById(R.id.activity_auth_google_auth_button);

        mGoogleAuthButton.setOnClickListener(this);

        /**
         * Necessary  initialize to get authentication
         * **/
        mGoogleAuthenticator = new GoogleAuthenticator(this);
        mGoogleAuthenticator.setGoogleAuthListener(this);
    }

    @Override
    public void setAuthenticationSuccess(final User user) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                System.out.println("Welcome! -> "+user.getUserName());
                Toast.makeText(AuthenticationActivity.this,"Welcome! "+user.getUserName(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void setAuthenticationFailed(final String errorMessage) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(AuthenticationActivity.this,errorMessage,Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_CODE_PICK_ACCOUNT) {
            if (resultCode == RESULT_OK) {
                String email = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                mGoogleAuthenticator.getUsername(email);
            } else if (resultCode == RESULT_CANCELED) {
                mGoogleAuthenticator.handleAuthorizeResult(resultCode,data);
            }
        } else if ((requestCode == Constants.REQUEST_CODE_RECOVER_FROM_AUTH_ERROR ||
                requestCode == Constants.REQUEST_CODE_RECOVER_FROM_PLAY_SERVICES_ERROR)
                && resultCode == RESULT_OK) {
            mGoogleAuthenticator.handleAuthorizeResult(resultCode, data);
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.activity_auth_google_auth_button){
            if(mGoogleAuthenticator!=null) {
                mGoogleAuthenticator.execute();
            }
        }
    }
}
