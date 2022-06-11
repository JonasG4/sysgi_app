package com.jonasgarcia.sys_gestion_incidentes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.jonasgarcia.sys_gestion_incidentes.config.Configuration;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class ResetPassword extends AppCompatActivity {

    EditText txtNewPassword, txtConfirmPassword;
    Button btnUpdatePassword;
    LinearLayout btnBack;
    TextView tvNewPWError, tvConfirmPWError;
    Configuration configuration = new Configuration();
    String URL = configuration.urlUsuarios;
    String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        txtNewPassword = findViewById(R.id.txtNewPW);
        txtConfirmPassword = findViewById(R.id.txtConfirmPW);
        btnBack = findViewById(R.id.btnNewPWBack);
        btnUpdatePassword = findViewById(R.id.btnUpdatePW);
        tvNewPWError = findViewById(R.id.tvEmailNewPWError);
        tvConfirmPWError = findViewById(R.id.tvConfirmPWError);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnUpdatePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updatePassword();
            }
        });
    }

    public void updatePassword() {
        RequestQueue requestQueue = Volley.newRequestQueue(ResetPassword.this);
        StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject result = new JSONObject(response);

                    startActivity(new Intent(ResetPassword.this, ResetPWSuccessful.class));
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error == null || error.networkResponse == null) {
                    return;
                }
                String body;
                //get status code here
                final String statusCode = String.valueOf(error.networkResponse.statusCode);
                //get response body and parse with appropriate encoding
                try {
                    body = new String(error.networkResponse.data, "UTF-8");
                    JSONObject data = new JSONObject(body);
                    JSONObject errors = data.getJSONObject("validationError");

                    if (!errors.isNull("newPassword")) {
                        tvNewPWError.setText(errors.getString("newPassword"));
                    } else {
                        tvNewPWError.setText("");
                    }

                    if (!errors.isNull("confirmPassword")) {
                        tvConfirmPWError.setText(errors.getString("confirmPassword"));
                    } else {
                        tvConfirmPWError.setText("");
                    }

                    if (!errors.isNull("email")) {
                        Toast.makeText(ResetPassword.this, errors.getString("email"), Toast.LENGTH_SHORT).show();
                    }

                } catch (UnsupportedEncodingException | JSONException e) {
                    Toast.makeText(ResetPassword.this, "Err: " + e.getMessage(), Toast.LENGTH_SHORT).show();

                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("action", "reset_password");
                params.put("email", email);
                params.put("newPassword", txtNewPassword.getText().toString());
                params.put("confirmPassword", txtConfirmPassword.getText().toString());
                return params;
            }
        };
        requestQueue.add(request);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Bundle extras = getIntent().getExtras();

        if(extras != null){
            email = extras.getString("email");
        }
    }
}