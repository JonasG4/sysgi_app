package com.jonasgarcia.sys_gestion_incidentes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
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

public class NoteIncident extends AppCompatActivity {
    LinearLayout btnAddNoteIncident_Back;
    EditText addNote;
    TextView errorNote;
    Configuration config = new Configuration();
    String URL = config.urlIncidentes;
    SharedPreferences preferences;
    Button btnAddNote;
    String id_incident = "0", token = "", id_rol = "0", state = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_incident);
        preferences = this.getSharedPreferences("session", Context.MODE_PRIVATE);

        btnAddNoteIncident_Back = findViewById(R.id.btnAddNoteIncident_Back);
        addNote = findViewById(R.id.addNote);
        errorNote = findViewById(R.id.errorNote);
        btnAddNote = findViewById(R.id.btnAddNote);

        btnAddNoteIncident_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNoteIncident();
            }
        });

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            id_incident = extras.getString("id_incident");
            if(extras.getString("estado").equals("0")) {
                state = "1";
            }else {
                state = "0";
            }
            token = preferences.getString("token", "");
        }
    }

    public void addNoteIncident() {
        RequestQueue requestQueue = Volley.newRequestQueue(NoteIncident.this);
        StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(NoteIncident.this,"AAAAA",
                        Toast.LENGTH_LONG).show();
                try {
                    JSONObject result = new JSONObject(response);
                    showToastOK("Nota agregada!.");
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

                    if (!errors.isNull("incidente")) {
                        setBackgroundError(addNote);
                        errorNote.setText(errors.getString("incidente"));
                    }

                } catch (UnsupportedEncodingException | JSONException e) {

                    showToastErr(e.getMessage());
                    Toast.makeText(NoteIncident.this,token,
                            Toast.LENGTH_LONG).show();
                }
                Toast.makeText(NoteIncident.this,addNote.getText().toString() + " " +
                        id_incident + " " + state,
                        Toast.LENGTH_LONG).show();
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
                params.put("action", "update_admin");
                params.put("id_incidente", id_incident);
                params.put("estado", state);
                params.put("nota", addNote.getText().toString());

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
    public void setBackgroundError(EditText editText){
        editText.setBackgroundResource(R.drawable.input_rounded_error);
        editText.setHintTextColor(getColor(R.color.slate_50));
    }
}