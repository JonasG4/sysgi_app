package com.jonasgarcia.sys_gestion_incidentes.adminUI;

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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jonasgarcia.sys_gestion_incidentes.R;


public class DashboardFragment extends Fragment {

    TextView tvName, tvEmail;
    SharedPreferences preferences;
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
    }
}