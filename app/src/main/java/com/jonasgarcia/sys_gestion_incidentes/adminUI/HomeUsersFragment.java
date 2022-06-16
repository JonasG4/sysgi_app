package com.jonasgarcia.sys_gestion_incidentes.adminUI;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.jonasgarcia.sys_gestion_incidentes.AddUser;
import com.jonasgarcia.sys_gestion_incidentes.R;

public class HomeUsersFragment extends Fragment {
    Button btnCreate, btnFilter;
    BottomSheetDialog bottomCreateDialog, bottomFilterDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home_users, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        btnCreate = view.findViewById(R.id.btnHomeUsersCreate);
        btnFilter = view.findViewById(R.id.btnHomeUsersFilters);

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

        bottomCreateDialog = new BottomSheetDialog(getContext(), R.style.BottomSheetDialogTheme);
        bottomFilterDialog = new BottomSheetDialog(getContext(), R.style.BottomSheetDialogTheme);
        super.onViewCreated(view, savedInstanceState);
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
        bottomCreateDialog.setContentView(bottomCreateUsersView);
        bottomCreateDialog.show();
    }

    public void showModelFilters() {
        View bottomFilterView =
                LayoutInflater.from(getContext())
                        .inflate(
                                R.layout.layout_bottom_filters,
                                getView().findViewById(R.id.bottomFilterContainer));

        bottomFilterView.findViewById(R.id.btnFilterClose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomFilterDialog.dismiss();
            }
        });

        bottomFilterDialog.setContentView(bottomFilterView);
        bottomFilterDialog.show();
    }


}