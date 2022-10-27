package ly.smarthive.vaccine.activities;


import static ly.smarthive.vaccine.AppController.TAG;
import static ly.smarthive.vaccine.COMMON.*;
import static ly.smarthive.vaccine.COMMON.MAIN_URL;
import static ly.smarthive.vaccine.COMMON.USER_TOKEN;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Cache;
import com.android.volley.Request;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import ly.smarthive.vaccine.AppController;
import ly.smarthive.vaccine.R;

public class IndexActivity extends AppCompatActivity {

    TextView all, infected, well, dead;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);

        all = findViewById(R.id.all_txt);
        infected = findViewById(R.id.infected_txt);
        well = findViewById(R.id.well_txt);
        dead = findViewById(R.id.dead_txt);


        Cache cache = AppController.getInstance().getRequestQueue().getCache();
        Cache.Entry entry = cache.get(MAIN_URL);
        if (entry != null) {
            // fetch the data from cache
            String data = new String(entry.data, StandardCharsets.UTF_8);
            try {
                parseJsonFeed(new JSONObject(data));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else {

            JsonObjectRequest jsonReq = new JsonObjectRequest(Request.Method.GET, MAIN_URL, null, response -> {
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
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }


    /**
     * Parsing json response and passing the data to feed view list adapter
     **/

    @SuppressLint("NotifyDataSetChanged")
    private void parseJsonFeed(JSONObject response) {
        try {
            JSONArray feedArray = response.getJSONArray("data");
            for (int i = 0; i < feedArray.length(); i++) {
                JSONObject feedObj = (JSONObject) feedArray.get(i);

                all.setText(feedObj.getString("all"));
                well.setText(feedObj.getString("well"));
                dead.setText(feedObj.getString("dead"));
                infected.setText(feedObj.getString("infected"));

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}