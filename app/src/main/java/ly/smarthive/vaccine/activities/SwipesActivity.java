package ly.smarthive.vaccine.activities;

import static ly.smarthive.vaccine.AppController.TAG;
import static ly.smarthive.vaccine.COMMON.SWIPES_URL;
import static ly.smarthive.vaccine.COMMON.USER_TOKEN;
import static ly.smarthive.vaccine.COMMON.SWIPES_URL;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Cache;
import com.android.volley.Request;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import ly.smarthive.vaccine.AppController;
import ly.smarthive.vaccine.R;
import ly.smarthive.vaccine.adapter.SwipesDataAdapter;
import ly.smarthive.vaccine.models.Swipe;
import ly.smarthive.vaccine.util.MyDividerItemDecoration;
import ly.smarthive.vaccine.AppController;
import ly.smarthive.vaccine.adapter.SwipesDataAdapter;

public class SwipesActivity extends AppCompatActivity {

    private final List<Swipe> SwipesList = new ArrayList<>();
    private SwipesDataAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipes);

        RecyclerView recyclerView = findViewById(R.id.swipes_rv);

        mAdapter = new SwipesDataAdapter(SwipesList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(this, DividerItemDecoration.VERTICAL, 36));
        recyclerView.setAdapter(mAdapter);

        Cache cache = AppController.getInstance().getRequestQueue().getCache();
        Cache.Entry entry = cache.get(SWIPES_URL);
        if (entry != null) {
            // fetch the data from cache
            String data = new String(entry.data, StandardCharsets.UTF_8);
            try {
                parseJsonFeed(new JSONObject(data));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else {

            JsonObjectRequest jsonReq = new JsonObjectRequest(Request.Method.GET, SWIPES_URL, null, response -> {
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

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
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
                Swipe post = new Swipe();
                post.setNumber(feedObj.getString("number"));
                post.setResult(feedObj.getString("result"));
                post.setTakenAt(feedObj.getString("taken_at"));
                SwipesList.add(post);
                mAdapter.notifyDataSetChanged();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}