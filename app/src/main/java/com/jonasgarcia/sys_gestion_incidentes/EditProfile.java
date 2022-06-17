package com.jonasgarcia.sys_gestion_incidentes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.jonasgarcia.sys_gestion_incidentes.adminUI.DetailsUser;
import com.jonasgarcia.sys_gestion_incidentes.config.Configuration;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class EditProfile extends AppCompatActivity {

    LinearLayout btnBack;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    Button btnUpdate;
    Configuration config = new Configuration();
    String URL = config.urlUsuarios;
    String id_usuario = "0", token = "", id_rol = "0";
    EditText txtName, txtLastname, txtEmail;
    TextView txtNameErr, txtLastnameErr, txtEmailErr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        btnBack = findViewById(R.id.btnEditProfile_Back);
        btnUpdate = findViewById(R.id.btnUpdateProfile_Save);
        txtName = findViewById(R.id.txtUpdateProfile_Name);
        txtLastname = findViewById(R.id.txtUpdateProfile_Lastname);
        txtEmail = findViewById(R.id.txtUpdateProfile_Email);

        txtNameErr = findViewById(R.id.txtUpdateProfile_NameError);
        txtLastnameErr = findViewById(R.id.txtUpdateProfile_LastnameError);
        txtEmailErr = findViewById(R.id.txtUpdateProfile_EmailError);


        // Inicialize preferences
        preferences = this.getSharedPreferences("session", MODE_PRIVATE);

        resetInputBackground();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUser();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (preferences.getString("id_usuario", null) != null) {
            id_usuario = preferences.getString("id_usuario", null);
            token = preferences.getString("token", null);
            id_rol = preferences.getString("id_rol", null);
        }
        getUserById();
    }

    public void getUserById(){
        RequestQueue requestQueue = Volley.newRequestQueue(EditProfile.this);
        StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject result = new JSONObject(response);
                    JSONObject data = result.getJSONObject("result");

                    txtName.setText(data.getString("nombre"));
                    txtLastname.setText(data.getString("apellido"));
                    txtEmail.setText(data.getString("email"));

                } catch (Exception e) {
                    e.printStackTrace();
                    showToastErr(preferences.getString("id_usuario", null));
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
                    showToastErr(data.getString("msg"));
                } catch (UnsupportedEncodingException | JSONException e) {
                    showToastErr(e.getMessage());
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("action", "list_user_id");
                params.put("id_usuario", id_usuario);
                return params;
            }
        };
        requestQueue.add(request);
    }

    public void updateUser(){
        RequestQueue requestQueue = Volley.newRequestQueue(EditProfile.this);
        StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject result = new JSONObject(response);
                    savedSession(id_usuario, txtName.getText().toString(), txtLastname.getText().toString(), txtEmail.getText().toString(), id_rol, token);
                    showToastOK("Datos actualizados correctamente.");
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                    showToastErr(preferences.getString("id_usuario", null));
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

                    if (!errors.isNull("nombre")) {
                        setBackgroundError(txtName);
                        txtNameErr.setText(errors.getString("nombre"));
                    }

                    if (!errors.isNull("apellido")) {
                        setBackgroundError(txtLastname);
                        txtLastnameErr.setText(errors.getString("apellido"));
                    }

                    if (!errors.isNull("email")) {
                        setBackgroundError(txtEmail);
                        txtEmailErr.setText(errors.getString("email"));
                    }
                } catch (UnsupportedEncodingException | JSONException e) {
                    showToastErr(e.getMessage());
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", token);
                return headers;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("action", "update");
                params.put("id_usuario", id_usuario);
                params.put("nombre", txtName.getText().toString());
                params.put("apellido", txtLastname.getText().toString());
                params.put("email", txtEmail.getText().toString());
                params.put("id_rol", id_rol);
                return params;
            }
        };
        requestQueue.add(request);
    }

    public void showToastErr(String msg) {
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.custom_toast_err, (ViewGroup) findViewById(R.id.ll_custom_toast_err));
        TextView tvMessage = view.findViewById(R.id.tvMsg);
        tvMessage.setText(msg);

        Toast toastOK = new Toast(getApplicationContext());
        toastOK.setGravity(Gravity.BOTTOM, 0, 300);
        toastOK.setDuration(Toast.LENGTH_LONG);
        toastOK.setView(view);
        toastOK.show();
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

    public void savedSession(String id_usuario, String nombre, String apellido, String email, String id_rol, String token) {
        editor = preferences.edit();

        editor.putString("id_usuario", id_usuario);
        editor.putString("nombre", nombre);
        editor.putString("apellido", apellido);
        editor.putString("email", email);
        editor.putString("id_rol", id_rol);
        editor.putString("token", token);
        editor.commit();
    }

    public void setBackgroundNormal(EditText editText, TextView textView){
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                editText.setBackgroundResource(R.drawable.input_rounded);
                editText.setHintTextColor(getColor(R.color.slate_400));
                textView.setText("");
            }
        });
    }

    public void setBackgroundError(EditText editText){
        editText.setBackgroundResource(R.drawable.input_rounded_error);
        editText.setHintTextColor(getColor(R.color.slate_50));

    }

    public void resetInputBackground(){
        setBackgroundNormal(txtName, txtNameErr);
        setBackgroundNormal(txtLastname, txtLastnameErr);
        setBackgroundNormal(txtEmail, txtEmailErr);
    }
}