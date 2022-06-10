package com.jonasgarcia.sys_gestion_incidentes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
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

public class Login extends AppCompatActivity {

    EditText txtEmail, txtPassword;
    Button btnLogin;
    TextView tvForgotPassword, tvEmailError, tvPasswordError;
    CheckBox cbRememberMe;

    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    //    Instances objects cofiguration
    Configuration config = new Configuration();
    String URL = config.urlAuth;
    boolean isPasswordVisible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //        Bind view with objects
        txtEmail = findViewById(R.id.txtLoginEmail);
        txtPassword = findViewById(R.id.txtLoginPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvForgotPassword = findViewById(R.id.btnLoginForgotPasswod);
        cbRememberMe = findViewById(R.id.cbRememberMe);
        tvEmailError = findViewById(R.id.tvLoginEmailError);
        tvPasswordError = findViewById(R.id.tvLoginPasswordError);

        // Inicialize preferences
        preferences = this.getSharedPreferences("session", MODE_PRIVATE);

        //       set events
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Login();
            }
        });

        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pushTo(ForgotPassword.class, false);
            }
        });
    }

    public void Login() {
        RequestQueue requestQueue = Volley.newRequestQueue(Login.this);
        StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject result = new JSONObject(response);
                    JSONObject user = result.getJSONObject("usuario");
                    String token = result.getString("token");
                    clearInputsError();

                    String id_usuario = user.getString("id_usuario");
                    String nombre = user.getString("nombre");
                    String apellido = user.getString("apellido");
                    String email = user.getString("email");
                    String rol = user.getString("nombre");

                    savedSession(id_usuario, nombre, apellido, email, rol, token);

                        pushTo(PanelControl.class, true);


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

                    if (!errors.isNull("email")) {
                        tvEmailError.setText(errors.getString("email"));
                    } else {
                        tvEmailError.setText("");

                        if (!errors.isNull("password")) {
                            tvPasswordError.setText(errors.getString("password"));
                        } else {
                            tvPasswordError.setText("");
                        }
                    }

                } catch (UnsupportedEncodingException | JSONException e) {
                    Toast.makeText(Login.this, "Err: " + e.getMessage(), Toast.LENGTH_SHORT).show();

                }


            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("action", "Login");
                params.put("email", txtEmail.getText().toString());
                params.put("password", txtPassword.getText().toString());
                return params;
            }
        };
        requestQueue.add(request);
    }

    @Override
    protected void onResume() {
        super.onResume();
        isAuth();
    }

    public void pushTo(Class view, boolean setClose) {
        startActivity(new Intent(this, view));
        if (setClose) {
            finish();
        }
    }

    public void savedSession(String id_usuario, String nombre, String apellido, String email, String id_rol, String token) {
        editor = preferences.edit();

        boolean isSaveSession = cbRememberMe.isChecked();

        editor.putString("id_usuario", id_usuario);
        editor.putString("nombre", nombre);
        editor.putString("apellido", apellido);
        editor.putString("email", email);
        editor.putString("id_rol", id_rol);
        editor.putString("token", token);
        editor.putBoolean("isSaveSession", isSaveSession);
        editor.commit();
    }

    public void clearInputsError() {
        tvEmailError.setText("");
        tvPasswordError.setText("");
    }

    public void isAuth() {
        if (preferences.getString("token", null) != null) {
            pushTo(MainActivity.class, true);
        }
    }

}