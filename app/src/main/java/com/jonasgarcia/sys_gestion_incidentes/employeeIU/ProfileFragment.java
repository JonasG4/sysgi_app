package com.jonasgarcia.sys_gestion_incidentes.employeeIU;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.jonasgarcia.sys_gestion_incidentes.ChangePassword;
import com.jonasgarcia.sys_gestion_incidentes.EditProfile;
import com.jonasgarcia.sys_gestion_incidentes.Login;
import com.jonasgarcia.sys_gestion_incidentes.R;


public class ProfileFragment extends Fragment {

    SharedPreferences preferences;
    Button btnLogout;
    RelativeLayout btnEdit, btnDelete, btnChangePassword;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = this.getActivity().getSharedPreferences("session", Context.MODE_PRIVATE);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnLogout = view.findViewById(R.id.btnLogout);
        btnEdit = view.findViewById(R.id.btnProfileEdit);
        btnChangePassword = view.findViewById(R.id.btnProfileChangePassword);
        btnDelete = view.findViewById(R.id.btnProfileDelete);

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pushTo(EditProfile.class);
            }
        });

        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pushTo(ChangePassword.class);
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });
    }


    public void logout() {
        preferences.edit().clear().apply();
        pushTo(Login.class);
    }

    public void pushTo(Class view) {
        startActivity(new Intent(this.getActivity(), view));
    }

}