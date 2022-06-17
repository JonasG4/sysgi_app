package com.jonasgarcia.sys_gestion_incidentes.adminUI;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.jonasgarcia.sys_gestion_incidentes.EditUser;
import com.jonasgarcia.sys_gestion_incidentes.R;
import com.jonasgarcia.sys_gestion_incidentes.config.Configuration;
import com.jonasgarcia.sys_gestion_incidentes.utils.DialogLoading;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class DetailsUser extends AppCompatActivity {
    TextView tvName, tvLastname, tvEmail, tvRole;
    int id_usuario = 0;
    Configuration configuration = new Configuration();
    String URL = configuration.urlUsuarios;
    LinearLayout btnClose;
    Button btnEdit, btnDelete;
    SharedPreferences preferences;
    DialogLoading dialogLoading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_user);
        tvName = findViewById(R.id.tvDetailsName);
        tvLastname = findViewById(R.id.tvDetailsLastname);
        tvEmail = findViewById(R.id.tvDetailsEmail);
        tvRole = findViewById(R.id.tvDetailsRole);
        btnClose = findViewById(R.id.btnDetailsUser_Back);
        btnEdit = findViewById(R.id.btnDetailUserEdit);
        btnDelete = findViewById(R.id.btnDetailUserDelete);

        preferences = this.getSharedPreferences("session", MODE_PRIVATE);
        dialogLoading = new DialogLoading(DetailsUser.this);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent window = new Intent(DetailsUser.this, EditUser.class);
                window.putExtra("id_usuario", Integer.toString(id_usuario));
                startActivity(window);
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showModelConfirm();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            id_usuario = extras.getInt("id_usuario");
        }

        dialogLoading.startDialog();
        getUserById();
    }

    public void getUserById(){
        RequestQueue requestQueue = Volley.newRequestQueue(DetailsUser.this);
        StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject result = new JSONObject(response);
                    JSONObject data = result.getJSONObject("result");

                    tvName.setText(data.getString("nombre"));
                    tvLastname.setText(data.getString("apellido"));
                    tvEmail.setText(data.getString("email"));
                    if(data.getString("id_rol").equals("1")){
                        tvRole.setText("Empleado");
                    }else{
                        tvRole.setText("Administrador");
                    }
                    dialogLoading.dismissDialog();
                } catch (Exception e) {
                    e.printStackTrace();
                    dialogLoading.dismissDialog();
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
                dialogLoading.dismissDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("action", "list_user_id");
                params.put("id_usuario", Integer.toString(id_usuario));
                return params;
            }
        };
        requestQueue.add(request);
    }

    public void showModelConfirm(){
        AlertDialog.Builder builder = new AlertDialog.Builder(DetailsUser.this, R.style.AlertDialogTheme);
        View view = LayoutInflater.from(DetailsUser.this).inflate(R.layout.layout_alert_dialog, findViewById(R.id.layoutDialogContainer));
        builder.setView(view);

        final AlertDialog alertDialog = builder.create();

        view.findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        view.findViewById(R.id.btnConfirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteUser();
                alertDialog.dismiss();
            }
        });
        if(alertDialog.getWindow() != null){
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();
    }

    public void deleteUser(){
        RequestQueue requestQueue = Volley.newRequestQueue(DetailsUser.this);
        StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject result = new JSONObject(response);
                    showToastOK("Se ha eliminado el usuario correctamente.");
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
                    showToastErr(data.getString("msg"));
                } catch (UnsupportedEncodingException | JSONException e) {
                    showToastErr(e.getMessage());
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", preferences.getString("token", ""));
                return headers;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("action", "delete");
                params.put("id_usuario", Integer.toString(id_usuario));
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
}