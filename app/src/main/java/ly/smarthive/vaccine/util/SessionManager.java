package ly.smarthive.vaccine.util;


import static ly.smarthive.vaccine.COMMON.*;
import static ly.smarthive.vaccine.COMMON.KEY_EMAIL;
import static ly.smarthive.vaccine.COMMON.KEY_IS_LOGGED_IN;
import static ly.smarthive.vaccine.COMMON.KEY_PASSWORD;
import static ly.smarthive.vaccine.COMMON.KEY_STATUS;
import static ly.smarthive.vaccine.COMMON.KEY_TOKEN;
import static ly.smarthive.vaccine.COMMON.PREF_NAME;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import java.io.File;

public class SessionManager {
    private static final String TAG = SessionManager.class.getSimpleName();

    SharedPreferences pref;
    Editor editor;
    Context _context;

    @SuppressLint("CommitPrefEdits")
    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, 0);
        editor = pref.edit();
    }

    public void setLogin(boolean isLoggedIn) {
        editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn);
        editor.commit();
        Log.d(TAG, "User login session modified!");
    }


    public void setEmailPassword(String email, String password, boolean stat) {
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_PASSWORD, password);
        editor.putBoolean(KEY_STATUS, stat);
        editor.commit();
        Log.d(TAG, "User E&P set modified!");
    }

    public void setToken(String token) {
        editor.putString(KEY_TOKEN, token);
        editor.commit();
        Log.d(TAG, "User login session modified!");
    }

    public void clear() {
        File dir = new File(_context.getFilesDir().getParent() + "/shared_prefs/");
        String[] children = dir.list();
        for (int i = 0; i < children.length; i++) {
            // clear each preference file
            _context.getSharedPreferences(children[i].replace(".xml", ""), 0).edit().clear().commit();
            //delete the file
            new File(dir, children[i]).delete();
        }
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public String getToken() {
        return pref.getString(KEY_TOKEN, null);
    }

    public String getEmail() {

        return pref.getString(KEY_EMAIL, null);
    }

    public String getPassword() {
        return pref.getString(KEY_PASSWORD, null);
    }
}