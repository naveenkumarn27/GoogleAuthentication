package com.android.npgraphics.googleauthentication.Authenticator;

public interface GoogleAuthListener {

    /**
     * This method will used to give the success message to the user Activity
     * *
     */
    void setAuthenticationSuccess(User user);

    /**
     * This method will used to give the failure message to the user Activity
     * *
     */
    void setAuthenticationFailed(String errorMessage);
}
