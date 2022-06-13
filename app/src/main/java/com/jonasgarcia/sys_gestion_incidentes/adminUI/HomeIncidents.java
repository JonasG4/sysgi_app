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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.jonasgarcia.sys_gestion_incidentes.Indicentes.Incidente;
import com.jonasgarcia.sys_gestion_incidentes.R;
import com.jonasgarcia.sys_gestion_incidentes.config.Configuration;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;


public class HomeIncidents extends Fragment implements RecyclerViewInterface {

    SharedPreferences preferences;
    private ArrayList<Incidente> listIncidet;
    private RecyclerView recyclerView;
    Configuration config = new Configuration();
    String URL = config.urlUsuarios;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = this.getActivity().getSharedPreferences("session", Context.MODE_PRIVATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vista = inflater.inflate(R.layout.fragment_home_incidents, container, false);
        listIncidet = new ArrayList<>();
        recyclerView = vista.findViewById(R.id.rvIncidents);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        llenarLista();

        IncidentAdapter adapter = new IncidentAdapter(listIncidet,this);
        recyclerView.setAdapter(adapter);

        return vista;
    }

    private void llenarLista() {
        listIncidet.add(new Incidente("Leve","incidente leve",R.drawable.closet,"2022-02-21","0",""));
        listIncidet.add(new Incidente("Grave","incidente grave",R.drawable.comedor_madera,"2022-02-22","0",""));
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        loadIncidents();

        recyclerView = view.findViewById(R.id.rvIncidents);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(getActivity(),DetailIncident.class);
        intent.putExtra("tipo", listIncidet.get(position).getTipo());
        intent.putExtra("descripcion", listIncidet.get(position).getDescripcion());
        intent.putExtra("imagen", listIncidet.get(position).getImagen());
//        intent.putExtra("estado", listIncidet.get(position).getEstado());
        intent.putExtra("fecha", listIncidet.get(position).getFechaString());
//        intent.putExtra("nota", listIncidet.get(position).getNota());
        startActivity(intent);
    }

//    private void loadIncidents() {
//        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
//        StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                try {
//                    JSONObject result = new JSONObject(response);
//                    JSONObject users = result.getJSONObject("incidentes");
//                    String tipo = users.getString("tipo");
//                    String descripcion = users.getString("descripcion");
//                    String imagen = users.getString("imagen");
//                    String fechaString = users.getString("fecha_ingreso");
//                    String estado = users.getString("estado");
//                    String nota = users.getString("nota");
//
//                    Incidente incidente = new Incidente(tipo,descripcion,imagen,fechaString,
//                            estado,nota);
//                    listIncidet.add(incidente);
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//            }
//        });
//        requestQueue.add(request);
//    }
}





















