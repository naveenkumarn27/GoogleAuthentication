package com.android.npgraphics.googleauthentication.Authenticator;


public class Constants {

    public static final String SCOPE = "oauth2:https://www.googleapis.com/auth/userinfo.profile";


    public static final int REQUEST_CODE_PICK_ACCOUNT = 1000;
    public static final int REQUEST_CODE_RECOVER_FROM_AUTH_ERROR = 1001;
    public static final int REQUEST_CODE_RECOVER_FROM_PLAY_SERVICES_ERROR = 1002;

    public static final String FIRST_NAME_KEY = "given_name";
    public static final String LAST_NAME_KEY = "family_name";
    public static final String USER_NAME_KEY = "name";
    public static final String USER_ID_KEY = "id";
    public static final String USER_PICTURE_KEY = "picture";
    public static final String USER_LOCALE_KEY = "locale";

    public static final String EMAIL_ID = "email_id";
}
