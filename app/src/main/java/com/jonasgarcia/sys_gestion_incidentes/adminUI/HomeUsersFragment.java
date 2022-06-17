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

import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.jonasgarcia.sys_gestion_incidentes.AddUser;
import com.jonasgarcia.sys_gestion_incidentes.R;
import com.jonasgarcia.sys_gestion_incidentes.adapters.UserListAdapter;
import com.jonasgarcia.sys_gestion_incidentes.models.Usuario;
import com.jonasgarcia.sys_gestion_incidentes.config.Configuration;
import com.jonasgarcia.sys_gestion_incidentes.utils.DialogLoading;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeUsersFragment extends Fragment {
    Button btnCreate, btnFilter;
    BottomSheetDialog bottomCreateDialog, bottomFilterDialog;

    List<Usuario> usuarios = new ArrayList<>();
    TextView userCount;
    Configuration config = new Configuration();
    String URL = config.urlUsuarios;
    EditText txtSearch;
    boolean isChangeCheck = false;
    int currentIdCheck = 0;
    DialogLoading dialogLoading;
    SharedPreferences preferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = this.getActivity().getSharedPreferences("session", Context.MODE_PRIVATE);
        dialogLoading = new DialogLoading(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home_users, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnCreate = view.findViewById(R.id.btnHomeUsersCreate);
        btnFilter = view.findViewById(R.id.btnHomeUsersFilters);
        userCount = view.findViewById(R.id.tv_CountUsers);
        txtSearch = view.findViewById(R.id.txtHomeUsersSearch);

        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showModelFilters();
            }
        });

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showModelCreateUser();
            }
        });

        txtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                Toast.makeText(getContext(), charSequence.toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                filterUsers(editable.toString());
            }
        });

        bottomCreateDialog = new BottomSheetDialog(getContext(), R.style.BottomSheetDialogTheme);
        bottomFilterDialog = new BottomSheetDialog(getContext(), R.style.BottomSheetDialogTheme);
    }

    @Override
    public void onResume() {
        super.onResume();
        dialogLoading.startDialog();
        getAllUsers();
    }

    public void showModelCreateUser() {
        View bottomCreateUsersView =
                LayoutInflater.from(getContext())
                        .inflate(
                                R.layout.layout_bottom_create_users,
                                getView().findViewById(R.id.bottomCreateContainer));

        bottomCreateUsersView.findViewById(R.id.btnCreateUserClose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomCreateDialog.dismiss();
            }
        });
        bottomCreateUsersView.findViewById(R.id.btnModalCreateUser).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), AddUser.class));
                bottomCreateDialog.dismiss();
            }
        });

        bottomCreateUsersView.findViewById(R.id.btnModalCreateReport).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
           }
        });

        bottomCreateDialog.setContentView(bottomCreateUsersView);
        bottomCreateDialog.show();
    }

    public void showModelFilters() {
        View bottomFilterView =
                LayoutInflater.from(getContext())
                        .inflate(
                                R.layout.layout_bottom_filters,
                                getView().findViewById(R.id.bottomFilterContainer));


        RadioGroup rd_group = bottomFilterView.findViewById(R.id.rd_group);
        RadioButton rb_admin = bottomFilterView.findViewById(R.id.rb_admin);
        RadioButton rb_employee = bottomFilterView.findViewById(R.id.rb_employee);
        RadioButton rb_all = bottomFilterView.findViewById(R.id.rb_all);

        if (!isChangeCheck) {
            currentIdCheck = rb_all.getId();
        }

        rd_group.check(currentIdCheck);

        bottomFilterView.findViewById(R.id.btnFilterClose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomFilterDialog.dismiss();
            }
        });

        rb_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getUsersByRole("2");
                currentIdCheck = rb_admin.getId();
                isChangeCheck = true;
            }
        });
        rb_employee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getUsersByRole("1");
                currentIdCheck = rb_employee.getId();
                isChangeCheck = true;
            }
        });
        rb_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getAllUsers();
                currentIdCheck = rb_all.getId();
                isChangeCheck = true;
            }
        });

        bottomFilterDialog.setContentView(bottomFilterView);
        bottomFilterDialog.show();
    }

    public void getAllUsers() {
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
                    UserListAdapter listAdapter = new UserListAdapter(usuarios, getContext(), new UserListAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(Usuario item) {
                            if (item.getId_user() != Integer.parseInt(preferences.getString("id_usuario", "0"))) {
                                Intent window = new Intent(getContext(), DetailsUser.class);
                                window.putExtra("id_usuario", item.getId_user());
                                startActivity(window);
                            }else{
                                showToastOK("Esta es tu cuenta de usuario.");
                            }
                        }
                    });

                    RecyclerView recyclerView = getView().findViewById(R.id.rv_users);
                    recyclerView.setHasFixedSize(false);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    recyclerView.setAdapter(listAdapter);
                    String count = Integer.toString(usuarios.size());
                    userCount.setText(count + " Usuarios");
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
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("action", "list");
                return params;
            }
        };
        requestQueue.add(request);
    }

    public void filterUsers(String filter) {
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

                    UserListAdapter listAdapter = new UserListAdapter(usuarios, getContext(), new UserListAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(Usuario item) {
                            if (item.getId_user() != Integer.parseInt(preferences.getString("id_usuario", "0"))) {
                                Intent window = new Intent(getContext(), DetailsUser.class);
                                window.putExtra("id_usuario", item.getId_user());
                                startActivity(window);
                            }else{
                                showToastOK("Esta es tu cuenta de usuario.");
                            }
                        }
                    });

                    RecyclerView recyclerView = getView().findViewById(R.id.rv_users);
                    recyclerView.setHasFixedSize(false);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    recyclerView.setAdapter(listAdapter);

                    String count = Integer.toString(usuarios.size());

                    userCount.setText(count + " Usuarios");

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
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", preferences.getString("token", ""));
                return headers;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("action", "search");
                params.put("filter", filter);
                return params;
            }
        };
        requestQueue.add(request);
    }

    public void getUsersByRole(String id_role) {
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
                    UserListAdapter listAdapter = new UserListAdapter(usuarios, getContext(), new UserListAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(Usuario item) {
                            if (item.getId_user() != Integer.parseInt(preferences.getString("id_usuario", "0"))) {
                                Intent window = new Intent(getContext(), DetailsUser.class);
                                window.putExtra("id_usuario", item.getId_user());
                                startActivity(window);
                            }else{
                                showToastOK("Esta es tu cuenta de usuario.");
                            }
                        }
                    });

                    RecyclerView recyclerView = getView().findViewById(R.id.rv_users);
                    recyclerView.setHasFixedSize(false);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    recyclerView.setAdapter(listAdapter);

                    String count = Integer.toString(usuarios.size());

                    userCount.setText(count + " Usuarios");

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
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", preferences.getString("token", ""));
                return headers;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("action", "list_by_role");
                params.put("id_rol", id_role);
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


