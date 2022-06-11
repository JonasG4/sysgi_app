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

public class VerifyCode extends AppCompatActivity {

    Button btnVerify, btnResendEmail;
    LinearLayout btnBack;
    EditText txtCode;
    TextView codeError;
    Configuration config = new Configuration();
    String URL = config.urlUsuarios;

    int code = 0;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_code);

        codeError = findViewById(R.id.tvCodeError);
        btnVerify = findViewById(R.id.btnVerify);
        btnResendEmail = findViewById(R.id.btnResendEmail);
        txtCode = findViewById(R.id.txtVerifyCode);
        btnBack = findViewById(R.id.btnVerifyBack);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnResendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resendEmail();
            }
        });

        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifiyCode();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            code =  Integer.parseInt(extras.getString("codigo"));
            email = extras.getString("email");
        }
    }

    public void verifiyCode(){
        int providerCode = Integer.parseInt(txtCode.getText().toString());
        if(code != providerCode){
            codeError.setText("El codigo no es correcto.");
        }else{
            Intent window = new Intent(VerifyCode.this, ResetPassword.class);
            window.putExtra("email", email);
            startActivity(window);
            finish();
        }
    }

    public void resendEmail(){
        RequestQueue requestQueue = Volley.newRequestQueue(VerifyCode.this);
        StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject result = new JSONObject(response);
                    String codigo = result.getString("codigo");
                    code = Integer.parseInt(codigo);
                    Toast.makeText(VerifyCode.this, "Codigo enviado. Revise su correo", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(VerifyCode.this, data.getString("validationError"), Toast.LENGTH_SHORT).show();
                    }

                } catch (UnsupportedEncodingException | JSONException e) {
                    Toast.makeText(VerifyCode.this, "Err: " + e.getMessage(), Toast.LENGTH_SHORT).show();

                }
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("action", "send_email_reset_password");
                params.put("email", email);
                return params;
            }
        };
        requestQueue.add(request);
    }
}