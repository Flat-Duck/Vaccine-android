package ly.smarthive.vaccine;

import static ly.smarthive.vaccine.COMMON.DONORS_URL;
import static ly.smarthive.vaccine.COMMON.PROFILE_URL;
import static ly.smarthive.vaccine.COMMON.USER_TOKEN;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import ly.smarthive.vaccine.activities.RequestsActivity;
import ly.smarthive.vaccine.models.Donor;

public class ProfileActivity extends AppCompatActivity {
    private static final String TAG = ProfileActivity.class.getSimpleName();

    TextView name,mobile,email,address,date_birth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        name = findViewById(R.id.u_name);
        mobile= findViewById(R.id.u_phone);
        email= findViewById(R.id.u_email);
        address= findViewById(R.id.u_address);
        date_birth= findViewById(R.id.u_birth);
        GrabAllRequests();

    }

    private void GrabAllRequests() {
        JsonObjectRequest jsonReq = new JsonObjectRequest(com.android.volley.Request.Method.GET, PROFILE_URL, null, response -> {
            VolleyLog.d(TAG, "Response: " + response.toString());
            Log.e("RE", response.toString());
            parseJsonFeed(response);
        }, error -> Log.d("VOLLEY ERROR", error.toString())) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headerMap = new HashMap<>();
                headerMap.put("Content-Type", "application/json");
                headerMap.put("Authorization", "Bearer " + USER_TOKEN);
                return headerMap;
            }
        };
        AppController.getInstance().addToRequestQueue(jsonReq);
    }

    /**
     * Parsing json response and passing the data to feed view list adapter
     **/

    @SuppressLint("NotifyDataSetChanged")
    private void parseJsonFeed(JSONObject response) {

        try {
            JSONObject feedObj  = response.getJSONObject("data");

                name.setText(feedObj.getString("username"));
                mobile.setText(feedObj.getString("phone"));
                email.setText(feedObj.getString("email"));
                address.setText(feedObj.getString("address"));
                date_birth.setText(feedObj.getString("date_birth"));



    } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}