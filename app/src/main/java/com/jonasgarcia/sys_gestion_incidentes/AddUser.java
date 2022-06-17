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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
    TextView txtNameErr, txtLastnameErr, txtEmailErr, txtPasswordErr, txtConfirmPasswordErr;
    Spinner spRoles;
    LinearLayout btnBack;
    Button btnSave;
    String id_rol = "0";
    SharedPreferences preferences;

    Configuration config = new Configuration();
    String URL = config.urlUsuarios;
    RadioButton rb_employee, rb_admin;
    RadioGroup rd_group;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);

        txtName = findViewById(R.id.txtAddUser_Name);
        txtLastname = findViewById(R.id.txtAddUser_Lastname);
        txtEmail = findViewById(R.id.txtAddUser_Email);
        txtPassword = findViewById(R.id.txtAddUser_Password);
        txtConfirmPassword = findViewById(R.id.txtAddUser_ConfirmPassword);
        btnBack = findViewById(R.id.btnAddUser_Back);
        btnSave = findViewById(R.id.btnAddUser_Save);

        txtNameErr = findViewById(R.id.txtAddUser_NameError);
        txtLastnameErr = findViewById(R.id.txtAddUser_LastnameError);
        txtEmailErr = findViewById(R.id.txtAddUser_EmailError);
        txtPasswordErr = findViewById(R.id.txtAddUser_PasswordError);
        txtConfirmPasswordErr = findViewById(R.id.txtAddUser_ConfirmPasswordError);

        rd_group = findViewById(R.id.rd_group);
        rb_admin = findViewById(R.id.rb_admin);
        rb_employee = findViewById(R.id.rb_employee);

        preferences = this.getSharedPreferences("session", MODE_PRIVATE);

        resetInputBackground();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewUser();
            }
        });
    }

    private void addNewUser() {
        RequestQueue requestQueue = Volley.newRequestQueue(AddUser.this);
        StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject result = new JSONObject(response);
                    showToastOK("¡Usuario registrado con exito!");
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

                    if (!errors.isNull("password")) {
                        setBackgroundError(txtPassword);
                        txtPasswordErr.setText(errors.getString("password"));
                    }

                    if (!errors.isNull("confirmPassword")) {
                        setBackgroundError(txtConfirmPassword);
                        txtConfirmPasswordErr.setText(errors.getString("confirmPassword"));
                    }

                } catch (UnsupportedEncodingException | JSONException e) {
                    Toast.makeText(AddUser.this, "Err: " + e.getMessage(), Toast.LENGTH_SHORT).show();

                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", preferences.getString("token", null));
                return headers;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("action", "create");
                params.put("nombre", txtName.getText().toString());
                params.put("apellido", txtLastname.getText().toString());
                params.put("email", txtEmail.getText().toString());
                params.put("password", txtPassword.getText().toString());
                params.put("confirmPassword", txtConfirmPassword.getText().toString());
                if (rb_employee.isChecked()) {
                    id_rol = "1";
                } else {
                    id_rol = "2";
                }
                params.put("id_rol", id_rol);
                return params;
            }
        };
        requestQueue.add(request);
    }

    public void showToastOK(String msg) {
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
       setBackgroundNormal(txtPassword, txtPasswordErr);
       setBackgroundNormal(txtConfirmPassword, txtConfirmPasswordErr);
    }
}