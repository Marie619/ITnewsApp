package app.muneef.itnewsapp.preferences;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import app.muneef.itnewsapp.activities.LoginActivity;

public class PreferenceManager {

    private Context context;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    private static final String FILE_NAME = "Login File";
    private static final String USER_LOGIN_STATE = "login_state";
    private static final String USER_LOGIN_ID = "user_id";
    private static final String USER_EMAIL = "user_email";
    private static final String USER_PASSWORD = "user_password";
    private static final String IS_FIRST_TIME_LAUNCH = "Is first time launch";

    private static final int PRIVATE_MODE = 0;

    public PreferenceManager(Context context) {
        this.context = context;
        preferences = context.getSharedPreferences(FILE_NAME, PRIVATE_MODE);
        editor = preferences.edit();
    }


    public void setUserLog(String userEmail, String userPassword,  boolean islogged) {
        editor.putBoolean(USER_LOGIN_STATE, islogged);
        editor.putString(USER_EMAIL, userEmail);
        editor.putString(USER_PASSWORD, userPassword);
        editor.commit();
    }


    public int getUserId() {
        return preferences.getInt(USER_LOGIN_ID, 0);
    }

    public boolean userIsLogged() {
        return preferences.getBoolean(USER_LOGIN_STATE, false);
    }

    public void logoutUser(Context context) {
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);


    }

    public void setIsFirstTimeLaunch(Boolean isFirstTimeLaunch) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTimeLaunch);
        editor.commit();
    }

    public boolean isFirstTimeLaunch() {
        return preferences.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }
}

