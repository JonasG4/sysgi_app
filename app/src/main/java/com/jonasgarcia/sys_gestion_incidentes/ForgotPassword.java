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

public class ForgotPassword extends AppCompatActivity {

    Button btnContinue;
    LinearLayout btnBack;
    EditText txtEmail;
    TextView tvEmailError;
    Configuration config = new Configuration();
    String URL = config.urlUsuarios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        btnContinue = findViewById(R.id.btnResetPWContinue);
        btnBack = findViewById(R.id.btnFPWBack);
        txtEmail = findViewById(R.id.txtResetPWEmail);
        tvEmailError = findViewById(R.id.tvEmailResetPWError);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendEmailCode();
            }
        });

    }

    public void sendEmailCode(){
        RequestQueue requestQueue = Volley.newRequestQueue(ForgotPassword.this);
        StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject result = new JSONObject(response);
                    String codigo = result.getString("codigo");

                    Intent window = new Intent(ForgotPassword.this, VerifyCode.class);
                    window.putExtra("codigo", codigo);
                    window.putExtra("email", txtEmail.getText().toString());
                    startActivity(window);

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

                    if (!data.isNull("validationError")) {
                        tvEmailError.setText(data.getString("validationError"));
                    } else {
                        tvEmailError.setText("");
                    }

                } catch (UnsupportedEncodingException | JSONException e) {
                    Toast.makeText(ForgotPassword.this, "Err: " + e.getMessage(), Toast.LENGTH_SHORT).show();

                }
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("action", "send_email_reset_password");
                params.put("email", txtEmail.getText().toString());
                return params;
            }
        };
        requestQueue.add(request);
    }
}