package com.jonasgarcia.sys_gestion_incidentes.adminUI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jonasgarcia.sys_gestion_incidentes.R;

public class DetailIncident extends AppCompatActivity {
    SharedPreferences preferences;
    ImageView imageViewIncident;
    EditText tipoIncident,descripcionIncident;
    TextView fechaIncident;
    CheckBox isActive;
    Button agregarNota;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = this.getSharedPreferences("session", Context.MODE_PRIVATE);
        setContentView(R.layout.activity_detail_incident);

        imageViewIncident = findViewById(R.id.imgIncident);
        tipoIncident = findViewById(R.id.txtTipoIncident);
        descripcionIncident = findViewById(R.id.txtDescripcion);
        fechaIncident = findViewById(R.id.fechaIncident);
        agregarNota = findViewById(R.id.btnNota);

        Intent intent = getIntent();
//        String message = intent.getStringExtra("tipo");
//        Toast.makeText(this,message,Toast.LENGTH_LONG).show();

        String tipo = intent.getStringExtra("tipo");
        String descripcion = intent.getStringExtra("descripcion");
        int imagen = intent.getIntExtra("imagen",0);
//        String estado = intent.getStringExtra("estado");
//        String nota = intent.getStringExtra("nota");
        String fecha = intent.getStringExtra("fecha");
//
//        imageViewIncident.setImageResource(Integer.parseInt(imagen));
        tipoIncident.setText(tipo);
        descripcionIncident.setText(descripcion);
        fechaIncident.setText(fecha);
        imageViewIncident.setImageResource(imagen);
    }
}