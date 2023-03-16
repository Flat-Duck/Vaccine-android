package ly.smarthive.vaccine.activities;

import static ly.smarthive.vaccine.COMMON.CURRENT_USER_EMAIL;
import static ly.smarthive.vaccine.COMMON.CURRENT_USER_ID;
import static ly.smarthive.vaccine.COMMON.CURRENT_USER_PASSWORD;
import static ly.smarthive.vaccine.COMMON.UPDATE_PASSWORD_URL;
import static ly.smarthive.vaccine.COMMON.USER_TOKEN;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import ly.smarthive.vaccine.AppController;
import ly.smarthive.vaccine.ProfileActivity;
import ly.smarthive.vaccine.QrActivity;
import ly.smarthive.vaccine.R;
import ly.smarthive.vaccine.util.SessionManager;

public class NavActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = NavActivity.class.getSimpleName();
    CardView centers, qr, requests, map, exit, swipes, stats, posts;
    SessionManager session;
    FirebaseAuth mAuth;

    TextInputEditText currentPassword, newPassword, confirmPassword;
    String currentPasswordTXT, newPasswordTXT, confirmPasswordTXT;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav);
        mAuth = FirebaseAuth.getInstance();
        session = new SessionManager(this);
        retrieveData();

        centers = findViewById(R.id.centers_btn);
        centers.setOnClickListener(this);
        qr = findViewById(R.id.qr_btn);
        qr.setOnClickListener(this);
        requests = findViewById(R.id.profile_btn);
        requests.setOnClickListener(this);
        map = findViewById(R.id.map_btn);
        map.setOnClickListener(this);
        exit = findViewById(R.id.exit_btn);
        exit.setOnClickListener(this);
        swipes = findViewById(R.id.swipes_btn);
        swipes.setOnClickListener(this);
        stats = findViewById(R.id.stats_btn);
        stats.setOnClickListener(this);
        posts = findViewById(R.id.posts_btn);
        posts.setOnClickListener(this);

    }

    private void retrieveData() {
        CURRENT_USER_EMAIL = session.getEmail();
        CURRENT_USER_PASSWORD = session.getPassword();
        CURRENT_USER_ID = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        USER_TOKEN = session.getToken();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settings_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.change_password) {
            showChangePasswordDialogue();
        }
        return super.onOptionsItemSelected(item);
    }

    private void showChangePasswordDialogue() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View picker_layout = LayoutInflater.from(this).inflate(R.layout.layout_change_password, null);
        builder.setView(picker_layout);
        builder.setTitle(R.string.change_password);

        currentPassword = picker_layout.findViewById(R.id.current_password);
        newPassword = picker_layout.findViewById(R.id.new_password);
        confirmPassword = picker_layout.findViewById(R.id.confirm_password);

        builder.setPositiveButton(R.string.ok, (dialog, which) -> {
            if (checkNotNULL()) {
                currentPasswordTXT = Objects.requireNonNull(currentPassword.getText()).toString().trim();
                newPasswordTXT = Objects.requireNonNull(newPassword.getText()).toString().trim();
                confirmPasswordTXT = Objects.requireNonNull(confirmPassword.getText()).toString().trim();
                if (passwordConfirmed()) {
                    updateFCM();
                } else {
                    Toast.makeText(getApplicationContext(), R.string.please_enter_credentials, Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), R.string.please_enter_credentials, Toast.LENGTH_LONG).show();
            }
        });
        builder.setNegativeButton(R.string.cancel, (dialog, which) -> dialog.cancel());
        builder.show();
    }

    private void updatePHP() {

        JSONObject params = new JSONObject();
        try {
            params.put("password", newPasswordTXT);
            params.put("Content-Type", "multipart/form-data;");
            params.put("Accept", "application/json");
            // params.put()
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, UPDATE_PASSWORD_URL, params, response -> {

            try {
                if (response.getBoolean("success")) {
                    Log.d(TAG, "phpPassword updated");
                    Toast.makeText(getApplicationContext(), R.string.passord_changed_success, Toast.LENGTH_LONG).show();
                    signOut();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }, error -> Log.d("VOLLEY ERROR", error.toString())) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headerMap = new HashMap<>();
                headerMap.put("Content-Type", "multipart/form-data;");
                headerMap.put("Authorization", "Bearer " + USER_TOKEN);
                return headerMap;
            }
        };
        AppController.getInstance().addToRequestQueue(jsonObjectRequest);
    }

    private void updateFCM() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        AuthCredential credential = EmailAuthProvider.getCredential(CURRENT_USER_EMAIL, currentPasswordTXT);
        if (user != null) {
            user.reauthenticate(credential)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            user.updatePassword(newPasswordTXT).addOnCompleteListener(task1 -> {
                                if (task1.isSuccessful()) {
                                    Log.d(TAG, "fcmPassword updated");
                                    updatePHP();
                                } else {
                                    Log.d(TAG, "Error password not updated");
                                }
                            }).addOnFailureListener(e -> {
                                Log.d("XXX", e.getMessage());
                                Toast.makeText(getApplicationContext(), R.string.password_err_msg, Toast.LENGTH_LONG).show();
                            });
                        } else {
                            Log.d(TAG, "Error auth failed");
                        }
                    }).addOnFailureListener(e -> {
                Log.d("XXX", e.getMessage());
                Toast.makeText(getApplicationContext(), R.string.current_password_wrong, Toast.LENGTH_LONG).show();
            });
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.qr_btn:
                startActivity(new Intent(NavActivity.this, QrActivity.class));
                break;
            case R.id.profile_btn:
                startActivity(new Intent(NavActivity.this, ProfileActivity.class));
                break;
            case R.id.map_btn:
                startActivity(new Intent(NavActivity.this, MapActivity.class));
                break;
            case R.id.swipes_btn:
                startActivity(new Intent(NavActivity.this, SwipesActivity.class));
                break;
            case R.id.posts_btn:
                startActivity(new Intent(NavActivity.this, MainActivity.class));
                break;
            case R.id.centers_btn:
                startActivity(new Intent(NavActivity.this, CentersActivity.class));
                break;
            case R.id.stats_btn:
                startActivity(new Intent(NavActivity.this, IndexActivity.class));
                break;
            default:
                signOut();
                break;
        }
    }

    private boolean checkNotNULL() {
        return (!Objects.requireNonNull(currentPassword.getText()).toString().isEmpty() || !newPassword.toString().isEmpty() || !confirmPassword.toString().isEmpty());
    }

    private boolean passwordConfirmed() {
        return ((!currentPasswordTXT.isEmpty() || !newPasswordTXT.isEmpty() || confirmPasswordTXT.isEmpty())
                && (newPasswordTXT.equals(confirmPasswordTXT)));
    }

    private void signOut() {
        FirebaseAuth.getInstance().signOut();
        session.clear();
        startActivity(new Intent(NavActivity.this, StartActivity.class));
        finish();
    }
}