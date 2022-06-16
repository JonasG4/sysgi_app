package com.jonasgarcia.sys_gestion_incidentes;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddUser extends AppCompatActivity {

    EditText txtName, txtLastname, txtEmail, txtPassword, txtConfirmPassword;
    Spinner spRoles;
    LinearLayout btnBack;
    Button btnSave;

    Configuration config = new Configuration();
    String URL = config.urlUsuarios;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);

//        SPINNER ROLES
//        spRoles = findViewById(R.id.spnRol);
//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.roles, R.layout.spinner_items);
//        spRoles.setAdapter(adapter);

        txtName = findViewById(R.id.txtAddUser_Name);
        txtLastname = findViewById(R.id.txtAddUser_Lastname);
        txtEmail = findViewById(R.id.txtAddUser_Email);
        txtPassword = findViewById(R.id.txtAddUser_Password);
        txtConfirmPassword = findViewById(R.id.txtAddUser_ConfirmPassword);
        btnBack = findViewById(R.id.btnAddUser_Back);
        btnSave = findViewById(R.id.btnAddUser_Save);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showToastOK("¡Usuario registrado con exito!");
                finish();
//                addNewUser();
            }
        });
    }

    private void addNewUser(){
        RequestQueue requestQueue = Volley.newRequestQueue(AddUser.this);
        StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject result = new JSONObject(response);
                    JSONObject user = result.getJSONObject("usuario");
                    String nombre = user.getString("nombre");
                    String apellido = user.getString("apellido");
                    String email = user.getString("email");
                    String rol = user.getString("id_rol");

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
//                    JSONObject errors = data.getJSONObject("validationError");
//
//                    if (!errors.isNull("email")) {
//                        tv.setText(errors.getString("email"));
//                    } else {
//                        tvEmailError.setText("");
//                        if (!errors.isNull("password")) {
//                            tvPasswordError.setText(errors.getString("password"));
//                        } else {
//                            tvPasswordError.setText("");
//                        }
//                    }
                } catch (UnsupportedEncodingException | JSONException e) {
                    Toast.makeText(AddUser.this, "Err: " + e.getMessage(), Toast.LENGTH_SHORT).show();

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

    public void showToastOK(String msg){
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.custom_toast_ok, (ViewGroup) findViewById(R.id.ll_custom_toast_ok));
        TextView tvMessage = view.findViewById(R.id.tvMsg);
        tvMessage.setText(msg);

        Toast toastOK = new Toast(getApplicationContext());
        toastOK.setGravity(Gravity.BOTTOM, 0, 300);
        toastOK.setDuration(Toast.LENGTH_LONG);
        toastOK.setView(view);
        toastOK.show();
    }
}