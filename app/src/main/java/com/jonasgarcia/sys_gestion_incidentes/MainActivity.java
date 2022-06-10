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
import com.jonasgarcia.sys_gestion_incidentes.employeeIU.HomeFragment;
import com.jonasgarcia.sys_gestion_incidentes.employeeIU.ProfileFragment;

public class MainActivity extends AppCompatActivity {

    //    SharedPreferences
    SharedPreferences preferences;

    HomeFragment homeFragment = new HomeFragment();
    ProfileFragment profileFragment = new ProfileFragment();

    BottomNavigationView nav;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferences = getSharedPreferences("session", MODE_PRIVATE);

        nav = findViewById(R.id.botton_navigation);
        nav.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        loadFragment(homeFragment);
    }

    private final BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()){
                case R.id.menuHome:
                    loadFragment(homeFragment);
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
        isAuth();
    }

    public void isAuth() {
        if (preferences.getString("token", null) == null) {
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