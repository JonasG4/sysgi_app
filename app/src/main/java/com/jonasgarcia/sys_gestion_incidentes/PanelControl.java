package com.jonasgarcia.sys_gestion_incidentes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.jonasgarcia.sys_gestion_incidentes.adminUI.DashboardFragment;
import com.jonasgarcia.sys_gestion_incidentes.adminUI.HomeIncidentsFragment;
import com.jonasgarcia.sys_gestion_incidentes.adminUI.HomeUsersFragment;
import com.jonasgarcia.sys_gestion_incidentes.employeeIU.ProfileFragment;

public class PanelControl extends AppCompatActivity {


    //    SharedPreferences
    SharedPreferences preferences;

    //    Fragments
    DashboardFragment dashboardFragment = new DashboardFragment();
    ProfileFragment profileFragment = new ProfileFragment();
    HomeIncidentsFragment homeIncidentsFragment = new HomeIncidentsFragment();
    HomeUsersFragment homeUsersFragment = new HomeUsersFragment();

    //    Dialogs
    BottomNavigationView nav;
    BottomSheetDialog bottomSheetDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panelcontrol);


        preferences = getSharedPreferences("session", MODE_PRIVATE);

        nav = findViewById(R.id.admin_navigation);
        nav.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        bottomSheetDialog = new BottomSheetDialog(
                PanelControl.this, R.style.BottomSheetDialogTheme);
        loadFragment(dashboardFragment);
    }

    private final BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.menuDashboard:
                    loadFragment(dashboardFragment);
                    return true;
                case R.id.menuAdd:
                    modalMenu();
                    return true;
                case R.id.menuProfile:
                    loadFragment(profileFragment);
                    return true;
            }
            return false;
        }
    };

    public void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        isAdmin();
    }

    public void isAdmin() {
        if (preferences.getString("token", null) == null && !preferences.getString("id_rol", "").equals("2")) {
            pushTo(Login.class, true);
        }
    }

    public void pushTo(Class view, boolean setClose) {
        startActivity(new Intent(this, view));
        if (setClose) {
            finish();
        }
    }

    public void logout() {
        preferences.edit().clear().apply();
        pushTo(Login.class, true);
    }

    public void modalMenu() {

        View bottomSheetView =
                LayoutInflater.from(getApplicationContext()).
                        inflate(R.layout.layout_bottom_sheet,
                                findViewById(R.id.bottomSheetContainer));

        bottomSheetView.findViewById(R.id.btnModuleUsers).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadFragment(homeUsersFragment);
                bottomSheetDialog.dismiss();
            }
        });


        bottomSheetView.findViewById(R.id.btnModuleIncidents).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadFragment(homeIncidentsFragment);
                bottomSheetDialog.dismiss();
            }
        });

        bottomSheetView.findViewById(R.id.btnClose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
            }
        });

        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();
    }

    public void showToastOK(String msg) {
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.custom_toast_ok, (ViewGroup) findViewById(R.id.ll_custom_toast_ok));
        TextView tvMessage = view.findViewById(R.id.tvMsg);
        tvMessage.setText(msg);

        Toast toastOK = new Toast(getApplicationContext());
        toastOK.setGravity(Gravity.BOTTOM, 0, -200);
        toastOK.setDuration(Toast.LENGTH_SHORT);
        toastOK.setView(view);
        toastOK.show();
    }
}