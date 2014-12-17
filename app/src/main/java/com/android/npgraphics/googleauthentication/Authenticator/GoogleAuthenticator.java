package com.android.npgraphics.googleauthentication.Authenticator;


import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.GooglePlayServicesAvailabilityException;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.AccountPicker;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class GoogleAuthenticator {

    private static final String TAG = "GoogleAuthenticator";
    private Activity mActivity;

    private ProgressDialog mProgressDialog;

    /**
     * Google auth listener variable which is used to send the result value to the user activity
     * *
     */
    private GoogleAuthListener mGoogleAuthListener;

    public GoogleAuthenticator(Activity activity) {
        if (activity != null) {
            this.mActivity = activity;
        } else {
            System.out.println("Please send the not null value");
        }
    }

    private void createDialogueBox() {
        if (mProgressDialog == null || !mProgressDialog.isShowing()) {
            mProgressDialog = new ProgressDialog(mActivity);
            mProgressDialog.setMessage("Google Authentication process...");
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        }
    }

    private void dismissDialogueBox() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    public void execute() {
        int statusCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(mActivity);
        if (statusCode == ConnectionResult.SUCCESS) {
            createDialogueBox();
            getUsername(null);
        } else if (GooglePlayServicesUtil.isUserRecoverableError(statusCode)) {
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(
                    statusCode, mActivity, 0 /* request code not used */);
            dialog.show();
        } else {
            setErrorMessage("No network connection available");
        }
    }

    private void setErrorMessage(String errorMessage) {
        dismissDialogueBox();
        if (getGoogleAuthListener() != null) {
            getGoogleAuthListener().setAuthenticationFailed(errorMessage);
        }
    }

    /**
     * Attempt to get the user name. If the email address isn't known yet,
     * then call pickUserAccount() method so the user can pick an account.
     */
    public void getUsername(String emailIdValue) {
        if (emailIdValue == null) {
            pickUserAccount();
        } else {
            if (NetworkHandler.isDeviceOnline(mActivity)) {
                new GetUserDetailsTask(emailIdValue, mActivity, Constants.SCOPE).execute();
            } else {
                setErrorMessage("No Network Connection available..");
            }
        }
    }

    public void handleAuthorizeResult(int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            Log.i(TAG, "Retrying");
            String emailIdValue = data.getStringExtra(Constants.EMAIL_ID);
            new GetUserDetailsTask(emailIdValue, mActivity, Constants.SCOPE).execute();
            return;
        }
        if (resultCode == Activity.RESULT_CANCELED) {
            setErrorMessage("User rejected authorization.");
            return;
        }
        if (data == null) {
            setErrorMessage("Unknown error, click the button again");
            return;
        }
        setErrorMessage("Unknown error, click the button again");
    }


    /**
     * This method is a hook for background threads and async tasks that need to provide the
     * user a response UI when an exception occurs.
     */
    public void handleException(final Exception e, final String emailID) {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (e instanceof GooglePlayServicesAvailabilityException) {
                    // The Google Play services APK is old, disabled, or not present.
                    // Show a dialog created by Google Play services that allows
                    // the user to update the APK
                    int statusCode = ((GooglePlayServicesAvailabilityException) e)
                            .getConnectionStatusCode();
                    Dialog dialog = GooglePlayServicesUtil.getErrorDialog(statusCode,
                            mActivity,
                            Constants.REQUEST_CODE_RECOVER_FROM_PLAY_SERVICES_ERROR);
                    dialog.show();
                } else if (e instanceof UserRecoverableAuthException) {
                    // Unable to authenticate, such as when the user has not yet granted
                    // the app access to the account, but the user can fix this.
                    // Forward the user to an activity in Google Play services.
                    Intent intent = ((UserRecoverableAuthException) e).getIntent();
                    intent.putExtra(Constants.EMAIL_ID, emailID);
                    mActivity.startActivityForResult(intent,
                            Constants.REQUEST_CODE_RECOVER_FROM_PLAY_SERVICES_ERROR);
                }
            }
        });
    }


    /**
     * Starts an activity in Google Play Services so the user can pick an account
     * *
     */
    private void pickUserAccount() {
        String[] accountTypes = new String[]{"com.google"};
        Intent intent = AccountPicker.newChooseAccountIntent(null, null,
                accountTypes, false, null, null, null, null);
        mActivity.startActivityForResult(intent, Constants.REQUEST_CODE_PICK_ACCOUNT);
    }

    /**
     * Google auth listener value will get using this method
     * *
     */
    public GoogleAuthListener getGoogleAuthListener() {
        return mGoogleAuthListener;
    }

    public void setGoogleAuthListener(GoogleAuthListener mGoogleAuthListener) {
        if (mGoogleAuthListener != null) {
            this.mGoogleAuthListener = mGoogleAuthListener;
        } else {
            System.out.println("Please send the not null value");
        }
    }


    private class GetUserDetailsTask extends AsyncTask<Void, Void, User> {

        private Activity activity;
        private String emailID;
        private String scopes;

        public GetUserDetailsTask(String emailID, Activity activity, String scopes) {
            this.activity = activity;
            this.emailID = emailID;
            this.scopes = scopes;
        }

        @Override
        protected User doInBackground(Void... params) {

            URL url;
            try {
                String token = fetchToken();
                if (token == null) {
                    // error has already been handled in fetchToken()
                    return null;
                }
                url = new URL("https://www.googleapis.com/oauth2/v1/userinfo?access_token=" + token);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                int sc = con.getResponseCode();
                if (sc == 200) {
                    InputStream is = con.getInputStream();
                    User user = getUserDetails(readResponse(is));
                    getGoogleAuthListener().setAuthenticationSuccess(user);
                    is.close();
                    return user;
                } else if (sc == 401) {
                    try {
                        GoogleAuthUtil.clearToken(mActivity, token);
                    } catch (Exception e) {
                        setErrorMessage("Server auth error, please try again.");
                    }
                    setErrorMessage("Server auth error, please try again.");
                    return null;
                } else {
                    setErrorMessage("Server returned the following error code: " + sc);
                    return null;
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(User user) {
            super.onPostExecute(user);
            dismissDialogueBox();
            if (user != null && getGoogleAuthListener() != null) {
                getGoogleAuthListener().setAuthenticationSuccess(user);
            }
        }

        /**
         * Reads the response from the input stream and returns it as a string.
         */
        private String readResponse(InputStream is) throws IOException {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] data = new byte[2048];
            int len;
            while ((len = is.read(data, 0, data.length)) >= 0) {
                bos.write(data, 0, len);
            }
            return new String(bos.toByteArray(), "UTF-8");
        }

        /**
         * Parses the response and returns the first name of the user.
         *
         * @throws JSONException if the response is not JSON or if first name does not exist in response
         */
        private User getUserDetails(String jsonResponse) throws JSONException {
            JSONObject profile = new JSONObject(jsonResponse);
            User user = new User();
            user.setFirstName(profile.getString(Constants.FIRST_NAME_KEY));
            user.setLastName(profile.getString(Constants.LAST_NAME_KEY));
            user.setUserID(profile.getString(Constants.USER_ID_KEY));
            user.setPictureURL(profile.getString(Constants.USER_PICTURE_KEY));
            user.setUserName(profile.getString(Constants.USER_NAME_KEY));
            user.setEmailId(emailID);
            return user;
        }

        /**
         * Get a authentication token if one is not available. If the error is not recoverable then
         * it displays the error message on parent activity right away.
         */
        private String fetchToken() throws IOException {
            try {
                return GoogleAuthUtil.getToken(activity, emailID, scopes);
            } catch (UserRecoverableAuthException userRecoverableException) {
                // GooglePlayServices.apk is either old, disabled, or not present, which is
                // recoverable, so we need to show the user some UI through the activity.
                userRecoverableException.printStackTrace();
                handleException(userRecoverableException, emailID);
            } catch (GoogleAuthException fatalException) {
                fatalException.printStackTrace();
                setErrorMessage("Unrecoverable error " + fatalException.getMessage());
            }
            return null;
        }
    }
}
