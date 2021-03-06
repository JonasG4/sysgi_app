package com.jonasgarcia.sys_gestion_incidentes.adminUI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.jonasgarcia.sys_gestion_incidentes.NoteIncident;
import com.jonasgarcia.sys_gestion_incidentes.R;
import com.jonasgarcia.sys_gestion_incidentes.config.Configuration;
import com.jonasgarcia.sys_gestion_incidentes.utils.DialogLoading;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class DetailIncident extends AppCompatActivity {
    SharedPreferences preferences;
    int id_incident = 0;
    Configuration configuration = new Configuration();
    LinearLayout btnDetailsIncident_Back;
    String URL = configuration.urlIncidentes;
    String basePath = configuration.urlBase;
    ImageView imgIncident;
    String state = "";
    TextView txtIncident, txtDescription,txtState;
    DialogLoading dialogLoading;
    Button btnEdit, btnDelete;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = this.getSharedPreferences("session", Context.MODE_PRIVATE);
        dialogLoading = new DialogLoading(DetailIncident.this);
        setContentView(R.layout.activity_detail_incident);
        btnDetailsIncident_Back = findViewById(R.id.btnDetailsIncident_Back);
        imgIncident = findViewById(R.id.imgIncident);
        txtIncident = findViewById(R.id.txtIncident);
        txtDescription = findViewById(R.id.txtDescription);
        txtState = findViewById(R.id.txtState);
        btnEdit = findViewById(R.id.btnIncidentEdit);
        btnDelete = findViewById(R.id.btnDeleteIncident);

        btnDetailsIncident_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailIncident.this, NoteIncident.class);
                intent.putExtra("id_incident",Integer.toString(id_incident));
                intent.putExtra("estado", state);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        Bundle extras = getIntent().getExtras();
        dialogLoading.startDialog();
        if(extras != null){
            id_incident = extras.getInt("id_incidente");
            state = extras.getString("estado");
        }

        getIncidentById();
    }

    public void getIncidentById(){
        RequestQueue requestQueue = Volley.newRequestQueue(DetailIncident.this);
        StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject result = new JSONObject(response);
                    JSONObject data = result.getJSONObject("result");

                    txtIncident.setText(data.getString("tipo"));
                    txtDescription.setText(data.getString("descripcion"));
                    String urlImg = basePath + data.getString("imagen");
                    imgIncident.setImageURI(Uri.parse(urlImg));
                    Picasso.get().load(urlImg).into(imgIncident);
                    if(data.getString("estado").equals("0")) {
                        txtState.setText("Sin resolver");
                    }else {
                        txtState.setText("Resuelto");
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
                params.put("action", "get_incident");
                params.put("id", Integer.toString(id_incident));
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
}