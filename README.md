GoogleAuthentication
====================

Google plus authentication in android


This project will simplify the user Google authentication you can Directly use this project Code.

I made this project with our Future and reliable Platform Tool Android Studio for Android developer.

First of check this project in this Project i added the one Package name Google Authenticator use that package with your project.

HOW TO USE THIS PROJECT!!!!!


Step 1: copy google authenticator into your project and it will make the some errors.

Step 2: You need to import the 'com.google.android.gms:play-services:6.5.87' library into your project.It will resolve your problems

Step 3: And one more important steps in this project you need to create a GOOGLE API CREDENTIALS in your google 
console developers site https://console.developers.google.com/  

from the above site you can create the api credentials for your application.

Add some more lines into your AndroidMainfest file

        <application>
        -----
        -----
        <meta-data
            android:name="com.google.android.gms.version"
            android:value="@integer/google_play_services_version" />
        -----
        -----
        </application>
        
And don't forgot to add the internet access permissions into your mainfest file

      <uses-permission android:name="android.permission.INTERNET"/>
      <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>

Step 4: Then Initialize the global variable of our Google Authenticator in your Activity file like below.

   
    private GoogleAuthenticator mGoogleAuthenticator;
   
    /***/
   
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        /**
         * Necessary  initialize to get authentication
         * **/
        mGoogleAuthenticator = new GoogleAuthenticator(this);
        mGoogleAuthenticator.setGoogleAuthListener(this);
    }
    
    

Step 4: You need implement the GoogleAuthListener interface 
      
    public class AuthenticationActivity extends ActionBarActivity implements GoogleAuthListener{
    
     @Override
    public void setAuthenticationSuccess(final User user) {
        //get your success user details here
    }

    @Override
    public void setAuthenticationFailed(final String errorMessage) {
        //get your failure message here
    }
    }
    
Step 5: The you need to write a few coding on your activityresult method

Write these  lines into your Activity

      @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_CODE_PICK_ACCOUNT) {
            if (resultCode == RESULT_OK) {
                String email = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                //Authenticator variable name
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

Step 6: Final step if you did the above steps properly then you can execute the authenticator 

            if(mGoogleAuthenticator!=null) {
                mGoogleAuthenticator.execute();
            }

  


