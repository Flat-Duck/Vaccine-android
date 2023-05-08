package ly.smarthive.vaccine.activities;


import static ly.smarthive.vaccine.COMMON.CURRENT_USER_ID;
import static ly.smarthive.vaccine.COMMON.POSTS_URL;
import static ly.smarthive.vaccine.COMMON.USER_TOKEN;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Cache;
import com.android.volley.Request;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
import ly.smarthive.vaccine.adapter.PostsDataAdapter;
import ly.smarthive.vaccine.models.Post;
import ly.smarthive.vaccine.util.Helper;
import ly.smarthive.vaccine.util.MyDividerItemDecoration;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private final List<Post> PostList = new ArrayList<>();
    private PostsDataAdapter mAdapter;
    DatabaseReference mDatabase;
    private Helper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        helper = new Helper();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        RecyclerView recyclerView = findViewById(R.id.postsRV);

        mAdapter = new PostsDataAdapter(PostList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(this, DividerItemDecoration.VERTICAL, 36));
        recyclerView.setAdapter(mAdapter);
        mDatabase.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(CURRENT_USER_ID).exists()) {
                    helper.updateFbUser();
                } else {
                    helper.createFbUser();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Cache cache = AppController.getInstance().getRequestQueue().getCache();
        Cache.Entry entry = cache.get(POSTS_URL);
        if (entry != null) {
            // fetch the data from cache
            String data = new String(entry.data, StandardCharsets.UTF_8);
            try {
                parseJsonFeed(new JSONObject(data));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else {

            JsonObjectRequest jsonReq = new JsonObjectRequest(Request.Method.GET, POSTS_URL, null, response -> {
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
                Post post = new Post();
                post.setTitle(feedObj.getString("title"));
                post.setDate(feedObj.getString("created_at"));
                post.setBody(feedObj.getString("body"));
                PostList.add(post);
                mAdapter.notifyDataSetChanged();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

