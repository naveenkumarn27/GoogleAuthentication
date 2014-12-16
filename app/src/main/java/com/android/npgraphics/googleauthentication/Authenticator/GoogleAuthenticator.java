package com.android.npgraphics.googleauthentication.Authenticator;


import android.app.Activity;

public class GoogleAuthenticator implements GoogleAuthListener{

    private Activity mActivity;

    /**
     * Google auth listener variable which is used to send the result value to the user activity
     * **/
    private GoogleAuthListener mGoogleAuthListener;

    public GoogleAuthenticator (Activity activity) {
        this.mActivity = activity;
    }


    @Override
    public void setAuthenticationSuccess(User user) {
        
    }

    @Override
    public void setAuthenticationFailed(String errorMessage) {

    }
}
