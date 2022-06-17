package com.jonasgarcia.sys_gestion_incidentes.adminUI;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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
import com.jonasgarcia.sys_gestion_incidentes.R;
import com.jonasgarcia.sys_gestion_incidentes.models.Usuario;
import com.jonasgarcia.sys_gestion_incidentes.config.Configuration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class DashboardFragment extends Fragment {

    TextView tvName, tvEmail, tvEmployeeCount, tvAdminCount, tvNewIncidentsCount, tvTotalUsersCount;
    SharedPreferences preferences;
    Configuration configuration = new Configuration();
    String URL = configuration.urlUsuarios;

    List<Usuario> usuarios = new ArrayList<>();
    List<Usuario> usuariosEmpleado;
    List<Usuario> usuariosAdmin = new ArrayList<>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = this.getActivity().getSharedPreferences("session", Context.MODE_PRIVATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvName = view.findViewById(R.id.tvFgHomeName);
        tvEmail = view.findViewById(R.id.tvFgHomeEmail);

        tvEmail.setText(preferences.getString("email", null));
        tvName.setText(preferences.getString("nombre", null) + " " + preferences.getString("apellido", null));

        tvAdminCount = view.findViewById(R.id.gadgetAdminNumber);
        tvEmployeeCount = view.findViewById(R.id.gadgetEmployeeNumber);
        tvNewIncidentsCount = view.findViewById(R.id.gadgetNewIncidentsNumber);
        tvTotalUsersCount = view.findViewById(R.id.gadgetTotalUsersNumber);
    }

    @Override
    public void onResume() {
        super.onResume();
        getAllUsers();
    }

    public void getAllUsers(){
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject result = new JSONObject(response);
                    JSONArray data = result.getJSONArray("result");
                    usuarios.clear();

                    for (int i = 0; i < data.length(); i++) {
                        JSONObject usuario = data.getJSONObject(i);
                        int id_usuario = Integer.parseInt(usuario.getString("id_usuario"));
                        String nombre = usuario.getString("nombre");
                        String apellido = usuario.getString("apellido");
                        String email = usuario.getString("email");
                        int rol = usuario.getInt("id_rol");

                        usuarios.add(new Usuario(id_usuario, rol, nombre, apellido, email, ""));
                    }
                    usuariosEmpleado = usuarios.stream().filter(usuario -> usuario.getRol() == 1).collect(Collectors.toList());
                    usuariosAdmin = usuarios.stream().filter(usuario -> usuario.getRol() == 2).collect(Collectors.toList());

                    tvAdminCount.setText(Integer.toString(usuariosAdmin.size()));
                    tvEmployeeCount.setText(Integer.toString(usuariosEmpleado.size()));
                    tvTotalUsersCount.setText(Integer.toString(usuarios.size()));

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
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("action", "list");
                return params;
            }
        };
        requestQueue.add(request);
    }
}