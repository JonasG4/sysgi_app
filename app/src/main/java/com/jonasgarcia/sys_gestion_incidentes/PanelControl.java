package com.jonasgarcia.sys_gestion_incidentes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.jonasgarcia.sys_gestion_incidentes.adminUI.DashboardFragment;
import com.jonasgarcia.sys_gestion_incidentes.adminUI.HomeIncidents;
import com.jonasgarcia.sys_gestion_incidentes.employeeIU.HomeFragment;
import com.jonasgarcia.sys_gestion_incidentes.employeeIU.ProfileFragment;

public class PanelControl extends AppCompatActivity {


    //    SharedPreferences
    SharedPreferences preferences;

    DashboardFragment dashboardFragment = new DashboardFragment();
    ProfileFragment profileFragment = new ProfileFragment();
    HomeIncidents homeIncidents = new HomeIncidents();

    BottomNavigationView nav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panelcontrol);


        preferences = getSharedPreferences("session", MODE_PRIVATE);

        nav = findViewById(R.id.admin_navigation);
        nav.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        loadFragment(dashboardFragment);
    }

    private final BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()){
                case R.id.menuDashboard:
                    loadFragment(dashboardFragment);
                    return true;
                case R.id.menuIncident:
                    loadFragment(homeIncidents);
                    return true;
                case R.id.menuProfile:
                    loadFragment(profileFragment);
                    return true;
            }
            return false;
        }
    };

    public void loadFragment(Fragment fragment){
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

    @Override
    protected void onStop() {
        super.onStop();
        if (!preferences.getBoolean("isSaveSession", false)) {
            logout();
        }
    }
}