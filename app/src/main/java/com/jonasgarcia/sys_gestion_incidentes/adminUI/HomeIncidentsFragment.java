package com.jonasgarcia.sys_gestion_incidentes.adminUI;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.jonasgarcia.sys_gestion_incidentes.adapters.IncidentesListAdapter;
import com.jonasgarcia.sys_gestion_incidentes.adapters.UserListAdapter;
import com.jonasgarcia.sys_gestion_incidentes.models.Incidente;
import com.jonasgarcia.sys_gestion_incidentes.R;
import com.jonasgarcia.sys_gestion_incidentes.config.Configuration;
import com.jonasgarcia.sys_gestion_incidentes.models.Usuario;
import com.jonasgarcia.sys_gestion_incidentes.utils.DialogLoading;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class HomeIncidentsFragment extends Fragment {

    SharedPreferences preferences;
    List<Incidente> incidentes = new ArrayList<>();
    TextView incidentesCount;
    Configuration config = new Configuration();
    String URL = config.urlIncidentes;
    String basePath = config.urlBase;
    DialogLoading dialogLoading;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = this.getActivity().getSharedPreferences("session", Context.MODE_PRIVATE);
        dialogLoading = new DialogLoading(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_incidents, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        incidentesCount = view.findViewById(R.id.tv_CountIncidents);
    }

    @Override
    public void onResume() {
        super.onResume();
        dialogLoading.startDialog();
        getAllIncidents();
    }

    private void getAllIncidents() {
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject result = new JSONObject(response);
                    JSONArray data = result.getJSONArray("result");
                    incidentes.clear();

                    for (int i = 0; i < data.length(); i++) {
                        JSONObject incident = data.getJSONObject(i);
                        int id_incidente = Integer.parseInt(incident.getString("id_incidente"));
                        String tipo = incident.getString("tipo");
                        String descripcion = incident.getString("descripcion");
                        String fecha = incident.getString("fecha_ingreso");
                        int id_usuario = incident.getInt("id_usuario");
                        String estado = incident.getString("estado");
                        String imagen = basePath + incident.getString("imagen");
                        incidentes.add(new Incidente(id_incidente, tipo, descripcion, imagen, fecha, estado, "", id_usuario));
                    }
                    IncidentesListAdapter listAdapter = new IncidentesListAdapter(incidentes, getContext(), new IncidentesListAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(Incidente item) {
                            Intent window = new Intent(getContext(), DetailIncident.class);
                            window.putExtra("id_incidente", item.getId_incidente());
                            startActivity(window);
                        }
                    });

                    RecyclerView recyclerView = getView().findViewById(R.id.rvIncidents);
                    recyclerView.setHasFixedSize(false);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    recyclerView.setAdapter(listAdapter);

                    String count = Integer.toString(incidentes.size());
                    incidentesCount.setText(count + " Incidentes");
                    dialogLoading.dismissDialog();
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

                } catch (UnsupportedEncodingException | JSONException e) {
                    Toast.makeText(getContext(), "Err: " + e.getMessage(), Toast.LENGTH_SHORT).show();

                }
            }
        }) {
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> headers = new HashMap<>();
//                headers.put("Authorization", preferences.getString("token", ""));
//                return headers;
//            }
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("action", "list");
                return params;
            }
        };
        requestQueue.add(request);
    }

    public void showToastOK(String msg){
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.custom_toast_ok, (ViewGroup) getView().findViewById(R.id.ll_custom_toast_ok));
        TextView tvMessage = view.findViewById(R.id.tvMsg);
        tvMessage.setText(msg);

        Toast toastOK = new Toast(getContext());
        toastOK.setGravity(Gravity.BOTTOM, 0, 300);
        toastOK.setDuration(Toast.LENGTH_SHORT);
        toastOK.setView(view);
        toastOK.show();
    }
}





















