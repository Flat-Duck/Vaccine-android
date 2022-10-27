package ly.smarthive.vaccine.util;

import static ly.smarthive.vaccine.AppController.TAG;
import static ly.smarthive.vaccine.COMMON.CURRENT_LAT;
import static ly.smarthive.vaccine.COMMON.CURRENT_LNG;
import static ly.smarthive.vaccine.COMMON.CURRENT_USER_ID;
import static ly.smarthive.vaccine.COMMON.FB_TOKEN;

import android.annotation.SuppressLint;
import android.content.Context;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

public class Helper {
    DatabaseReference mDatabase;


    public Helper() {
      //  this.mContext = context;
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public void createFbUser() {
        mDatabase.child("users").child(CURRENT_USER_ID).child("lat").setValue(CURRENT_LAT);
        mDatabase.child("users").child(CURRENT_USER_ID).child("lng").setValue(CURRENT_LNG);
    }

    public void updateFbUser() {
        mDatabase.child("users").child(CURRENT_USER_ID).child("lat").setValue(CURRENT_LAT);
        mDatabase.child("users").child(CURRENT_USER_ID).child("lng").setValue(CURRENT_LNG);
    }

    @SuppressLint("SimpleDateFormat")
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static String getCurrentDate() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        return df.format(c);
    }

    public static String generateToken(Context context) {
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                return;
            }
            FB_TOKEN = task.getResult();
            Log.d(TAG, FB_TOKEN);
            Toast.makeText(context, FB_TOKEN, Toast.LENGTH_SHORT).show();
        });
        return FB_TOKEN;
    }


}
