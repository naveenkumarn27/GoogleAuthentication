package com.android.npgraphics.googleauthentication.Authenticator;

public interface GoogleAuthListener {

    public void setAuthenticationSuccess(User user);

    public void setAuthenticationFailed(String errorMessage);
}
