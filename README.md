GoogleAuthentication
====================

Google plus authentication in android
--------------------

This project will simplify the user Google authentication you can Directly use this project Code.

I made this project with our Future and reliable Platform Tool Android Studio for Android developer.

First of all check this project in this Project i added the one Package name Google Authenticator use that package with your project.

HOW TO USE THIS PROJECT!!!!!
--------------------

<b>Step 1:</b> copy google authenticator into your project and it will make the some errors.

<b>Step 2:</b> You need to import the 
```gradle
compile 'com.google.android.gms:play-services:6.5.87'
``` 
library into your project.It will resolve your problems

<b>Step 3:</b> And one more important steps in this project is you need to create a GOOGLE API CREDENTIALS in your google 
console developers site https://console.developers.google.com/  

from the above site you can create the api credentials for your application.

Add some more lines into your AndroidMainfest file
```xml
<application>
        <meta-data
            android:name="com.google.android.gms.version"
            android:value="@integer/google_play_services_version" />
</application>
```
And don't forgot to add the internet access permissions into your mainfest file
```xml
      <uses-permission android:name="android.permission.INTERNET"/>
      <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
```
<b>Step 4:</b> Then Initialize the global variable of our Google Authenticator in your Activity file like below.

```java
    private GoogleAuthenticator mGoogleAuthenticator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        /**
         * Necessary  initialize to get authentication
         **/
        mGoogleAuthenticator = new GoogleAuthenticator(this);
        mGoogleAuthenticator.setGoogleAuthListener(this);
    }
```

<b>Step 5:</b> You need implement the GoogleAuthListener interface 
      
```java
    public class AuthenticationActivity extends ActionBarActivity implements GoogleAuthListener {

        @Override
        public void setAuthenticationSuccess(final User user) {
            //get your success user details here
        }

        @Override
        public void setAuthenticationFailed(final String errorMessage) {
            //get your failure message here
        }
    }
```

<b>Step 6:</b> The you need to write a few coding on your activityresult method

Write these  lines into your Activity

```java
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_CODE_PICK_ACCOUNT) {
            if (resultCode == RESULT_OK) {
                String email = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                //Authenticator variable name
                mGoogleAuthenticator.getUsername(email);
            } else if (resultCode == RESULT_CANCELED) {
                mGoogleAuthenticator.handleAuthorizeResult(resultCode, data);
            }
        } else if ((requestCode == Constants.REQUEST_CODE_RECOVER_FROM_AUTH_ERROR ||
                requestCode == Constants.REQUEST_CODE_RECOVER_FROM_PLAY_SERVICES_ERROR)
                && resultCode == RESULT_OK) {
            mGoogleAuthenticator.handleAuthorizeResult(resultCode, data);
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
```
<b>Step 7:</b> Final step if you did the above steps properly then you can execute the authenticator

```java
        if (mGoogleAuthenticator != null) {
            mGoogleAuthenticator.execute();
        }
```

## License

[Apache Version 2.0](http://www.apache.org/licenses/LICENSE-2.0.html)

    Copyright (C) 2014 Naveen Kumar Kuppan

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
