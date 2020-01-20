package com.example.ventasejemplo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class Menu_Ambiente extends AppCompatActivity {
    Button btnAmbiente,btnEdificio,btnEscuela,btnFacultad;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu__ambiente);

        btnAmbiente= findViewById(R.id.btnAmbiente);
        btnEdificio= findViewById(R.id.btnEdificio);
        btnEscuela= findViewById(R.id.btnEscuela);
        btnFacultad= findViewById(R.id.btnFacultad);
    }
    public void Onclick(View view) {
        Intent intent;
        switch (view.getId()){
            case R.id.btnAmbiente:
                intent= new Intent (getApplicationContext(), RegisterAmbiente.class);
                startActivityForResult(intent, 9);
                break;
            case R.id.btnEdificio:
                intent= new Intent (getApplicationContext(), RegisterEdificio.class);
                startActivityForResult(intent, 10);
                break;
            case R.id.btnEscuela:
                intent= new Intent (getApplicationContext(), RegisterEscuela.class);
                startActivityForResult(intent, 20);
                break;
            case R.id.btnFacultad:
                intent= new Intent (getApplicationContext(), RegisterFacultad.class);
                startActivityForResult(intent, 30);
                break;
        }
    }
}
