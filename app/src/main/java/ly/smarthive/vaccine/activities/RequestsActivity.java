package ly.smarthive.vaccine.activities;

import static com.android.volley.Request.Method.GET;
import static ly.smarthive.vaccine.COMMON.*;
import static ly.smarthive.vaccine.COMMON.ACCEPT_REQUEST_URL;
import static ly.smarthive.vaccine.COMMON.REFUSE_REQUEST_URL;
import static ly.smarthive.vaccine.COMMON.REQUESTS_URL;
import static ly.smarthive.vaccine.COMMON.USER_TOKEN;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Cache;
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
import ly.smarthive.vaccine.adapter.RequestsDataAdapter;
import ly.smarthive.vaccine.models.Request;
import ly.smarthive.vaccine.util.MyDividerItemDecoration;

public class RequestsActivity extends AppCompatActivity implements RequestsDataAdapter.SelectedItem {
    private static final String TAG = RequestsActivity.class.getSimpleName();

    private final List<Request> requestsList = new ArrayList<>();
    private RequestsDataAdapter mAdapter;

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests);



    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    private void GrabAllRequests() {
        JsonObjectRequest jsonReq = new JsonObjectRequest(GET, REQUESTS_URL, null, response -> {
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
     * Parsing json reponse and passing the data to feed view list adapter
     **/

    @SuppressLint("NotifyDataSetChanged")
    private void parseJsonFeed(JSONObject response) {
        requestsList.clear();
        try {
            JSONArray feedArray = response.getJSONArray("data");
            for (int i = 0; i < feedArray.length(); i++) {
                JSONObject feedObj = (JSONObject) feedArray.get(i);
                Request request = new Request();
                request.setId(feedObj.getInt("id"));
                request.setFrom(feedObj.getString("from"));
                request.setPhone(feedObj.getString("phone"));
                request.setDate(feedObj.getString("created_at"));
                int state = feedObj.isNull("is_accepted") ? 3 : feedObj.getInt("is_accepted");
                request.setState(state);
                requestsList.add(request);
                mAdapter.notifyDataSetChanged();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void selectedItem(Request request, boolean accept) {
        String ACTION_URL = REQUESTS_URL + request.getId() + (accept ? ACCEPT_REQUEST_URL : REFUSE_REQUEST_URL);
        JsonObjectRequest jsonReq = new JsonObjectRequest(com.android.volley.Request.Method.POST, ACTION_URL, null, response -> {
            VolleyLog.d(TAG, "Response: " + response.toString());
            Log.e("RE", response.toString());
            try {
                if (response.getBoolean("success")) {
                    Toast.makeText(context, "The request has been Updated", Toast.LENGTH_LONG).show();
                    GrabAllRequests();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
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
}