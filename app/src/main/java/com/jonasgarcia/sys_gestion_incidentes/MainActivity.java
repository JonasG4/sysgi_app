package com.jonasgarcia.sys_gestion_incidentes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.jonasgarcia.sys_gestion_incidentes.employeeIU.HomeFragment;
import com.jonasgarcia.sys_gestion_incidentes.employeeIU.ProfileFragment;
import com.jonasgarcia.sys_gestion_incidentes.models.Usuario;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    static List<Usuario> ListaUsuarios = new ArrayList<>();
    Button btnCrearPDF;


    //    SharedPreferences
    SharedPreferences preferences;

    HomeFragment homeFragment = new HomeFragment();
    ProfileFragment profileFragment = new ProfileFragment();

    BottomNavigationView nav;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (checkPermission()) {
            Toast.makeText(this, "Permiso Aceptado", Toast.LENGTH_SHORT).show();
        } else {
            requestPermission();
        }

        btnCrearPDF = findViewById(R.id.btnCrearPdf);

        ListaUsuarios.add(new Usuario("karla", "yaneth", "karla85@gmail.com"));
        ListaUsuarios.add(new Usuario("karla", "yaneth", "karla85@gmail.com"));
        ListaUsuarios.add(new Usuario("karla", "yaneth", "karla85@gmail.com"));

        btnCrearPDF.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                CrearPDF();
            }
        });


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

    public static void CrearPDF(){
        try {
            String path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/EjemploITextPDF";
            File dir = new File(path);
            if (!dir.exists()) {
                dir.mkdir();
            }
            File file = new File(dir, "usuarios.pdf");
            FileOutputStream fileOutputStream = new FileOutputStream(file);

            Document documento = new Document();
            PdfWriter.getInstance(documento, fileOutputStream);

            documento.open();
            Paragraph titulo = new Paragraph(
                    "Lista de ususarios \n\n\n",
                    FontFactory.getFont("arial", 22, Font.BOLD, BaseColor.BLUE)
            );

            documento.add(titulo);

            PdfPTable tabla = new PdfPTable(3);
            tabla.addCell("USUARIO");
            tabla.addCell("NOMBRE");
            tabla.addCell("CORREO");

            for (int i = 0 ; i < ListaUsuarios.size() ; i++) {
                tabla.addCell(ListaUsuarios.get(i).getNombre());
                tabla.addCell(ListaUsuarios.get(i).getApellido());
                tabla.addCell(ListaUsuarios.get(i).getEmail());
            }

        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private boolean checkPermission() {
        int permission1 = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permission2 = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
        return permission1 == PackageManager.PERMISSION_GRANTED && permission2 == PackageManager.PERMISSION_GRANTED;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResult){
        super.onRequestPermissionsResult(requestCode, permissions, grantResult);
        if (requestCode == 200){
            if (grantResult.length > 0){
                boolean writeStorage = grantResult[0] == PackageManager.PERMISSION_GRANTED;
                boolean readStorage = grantResult[1] == PackageManager.PERMISSION_GRANTED;
            }
        }
    }

    private void requestPermission(){
        ActivityCompat.requestPermissions(this, new String[]{ Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 200);
    }
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