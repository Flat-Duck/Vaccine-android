package ly.smarthive.vaccine;

import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.textfield.TextInputEditText;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class StartActivity extends AppCompatActivity {
    SessionManager session;
    private static final String TAG = StartActivity.class.getSimpleName();
    TextInputEditText inputPhone;
    TextInputEditText inputPassword;
    private ProgressDialog pDialog;
    Button btnSignIn, btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        setContentView(R.layout.activity_start);
     //   btnRegister = (Button) findViewById(R.id.btnRegister);
        btnSignIn = (Button) findViewById(R.id.btnSignIn);

        btnRegister.setOnClickListener(v -> finish());

        btnSignIn.setOnClickListener(v -> showSignInDialog());

        checkLogin();
    }

    private void checkLogin() {
        session = new SessionManager(getApplicationContext());
        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(StartActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            showSignInDialog();
        }

    }

    private void showSignInDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View picker_layout = LayoutInflater.from(this).inflate(R.layout.layout_login, null);
        builder.setView(picker_layout);
        builder.setTitle(R.string.sign_in_title);
        builder.setMessage(R.string.please_use_email);
        inputPhone = picker_layout.findViewById(R.id.phone);
        inputPassword = picker_layout.findViewById(R.id.passwrod);


        builder.setPositiveButton(R.string.ok, (dialog, which) -> {
            final String phone = Objects.requireNonNull(inputPhone.getText()).toString().trim();
            final String password = Objects.requireNonNull(inputPassword.getText()).toString().trim();
            Log.d(TAG, "P:" + phone + "//W:" + password);
            login(phone, password);

        });
        builder.setNegativeButton(R.string.cancel, (dialog, which) -> dialog.cancel());
        builder.show();
    }

    private void login(String phone, String password) {


        // Check for empty data in the form
        if (!phone.isEmpty() && !password.isEmpty()) {
            // login user
            sendLogin(phone, password);
        } else {
            // Prompt user to enter credentials
            Toast.makeText(getApplicationContext(), R.string.please_enter_credentials, Toast.LENGTH_LONG).show();
        }
    }
    /**
     * function to verify login details in mysql db
     */
    private void sendLogin(final String phone, final String password) {
        // Tag used to cancel the request
        String tag_string_req = "req_login";

        pDialog.setMessage(getString(R.string.logging_in));
        showDialog();

        StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST, COMMON.LOGIN_URL, response -> {
            Log.d(TAG, "Login Response: " + response);
            hideDialog();
            try {
                JSONObject jObj = new JSONObject(response);
                boolean success = jObj.getBoolean("success");
                if (success) {
                    session.setLogin(true);
                    JSONObject data = jObj.getJSONObject("data");
                    String token = data.getString("accessToken");
                    session.setToken(token);
                    //    Toast.makeText(getApplicationContext(), "Token: " + token, Toast.LENGTH_LONG).show();
                    startActivity(new Intent(StartActivity.this, MainActivity.class));
                    finish();
                } else {
                    String errorMsg = jObj.getString("message");
                    Toast.makeText(getApplicationContext(),errorMsg, Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        },
                error -> {
                    Log.e(TAG, "Login Error: " + error.getMessage());
                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                    hideDialog();
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("Accept", "application/json");
                params.put("phone", phone);
                params.put("password", password);

                return params;
            }

        };
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_string_req);
    }
    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

}