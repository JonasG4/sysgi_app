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
import com.jonasgarcia.sys_gestion_incidentes.config.Configuration;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class ChangePassword extends AppCompatActivity {
    LinearLayout btnBack;
    EditText txtCurrentPw, txtNewPw, txtConfirmPw;
    TextView tvCurrentPwErr, tvNewPwErr, tvConfirmPwErr;
    Button btnSave;
    SharedPreferences preferences;
    Configuration configuration = new Configuration();
    String URL = configuration.urlUsuarios;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        btnBack = findViewById(R.id.btnChangePw_Back);
        btnSave = findViewById(R.id.btnChangePw_Save);

        txtCurrentPw = findViewById(R.id.txtChangePw_CurrentPw);
        txtNewPw = findViewById(R.id.txtChangePw_NewPw);
        txtConfirmPw = findViewById(R.id.txtChangePw_ConfirmPw);

        tvCurrentPwErr = findViewById(R.id.txtChangePw_CurrentPwErr);
        tvNewPwErr = findViewById(R.id.txtChangePw_NewPwErr);
        tvConfirmPwErr = findViewById(R.id.txtChangePw_ConfirmPwErr);

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
                changeUserPassword();
            }
        });
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
        setBackgroundNormal(txtCurrentPw, tvCurrentPwErr);
        setBackgroundNormal(txtNewPw, tvNewPwErr);
        setBackgroundNormal(txtConfirmPw, tvConfirmPwErr);
    }

    public void changeUserPassword(){
        RequestQueue requestQueue = Volley.newRequestQueue(ChangePassword.this);
        StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject result = new JSONObject(response);
                    showToastOK("Contraseña actualizada correctamente.");
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

                    if (!errors.isNull("oldPassword")) {
                        setBackgroundError(txtCurrentPw);
                        tvCurrentPwErr.setText(errors.getString("oldPassword"));
                    }

                    if (!errors.isNull("newPassword")) {
                        setBackgroundError(txtNewPw);
                        tvNewPwErr.setText(errors.getString("newPassword"));
                    }

                    if (!errors.isNull("confirmPassword")) {
                        setBackgroundError(txtConfirmPw);
                        tvConfirmPwErr.setText(errors.getString("confirmPassword"));
                    }
                } catch (UnsupportedEncodingException | JSONException e) {
                    showToastErr(e.getMessage());
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("action", "change_password");
                params.put("id_usuario", preferences.getString("id_usuario", "0"));
                params.put("oldPassword", txtCurrentPw.getText().toString());
                params.put("newPassword", txtNewPw.getText().toString());
                params.put("confirmPassword", txtConfirmPw.getText().toString());
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
}