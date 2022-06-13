package com.jonasgarcia.sys_gestion_incidentes.adminUI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

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

        String tipo = getIntent().getStringExtra("tipo");
        String descripcion = getIntent().getStringExtra("descripcion");
        String imagen = getIntent().getStringExtra("imagen");
        String estado = getIntent().getStringExtra("estado");
        String nota = getIntent().getStringExtra("nota");
        String fecha = getIntent().getStringExtra("fecha");

        imageViewIncident.setImageResource(Integer.parseInt(imagen));
        tipoIncident.setText(tipo);
        descripcionIncident.setText(descripcion);
        fechaIncident.setText(fecha);

    }
}