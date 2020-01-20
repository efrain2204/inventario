package com.example.ventasejemplo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class Menu extends AppCompatActivity {
    Button btnInmuebles,btnAmbiente,btnUsuario;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        btnInmuebles= findViewById(R.id.btnInmuebles);
        btnAmbiente= findViewById(R.id.btnAmbiente);
        btnUsuario= findViewById(R.id.btnUsuario);
    }


    public void Onclick(View view) {
        Intent intent;
        switch (view.getId()){
            case R.id.btnInmuebles:
                 intent= new Intent (getApplicationContext(), Menu_Inmuebles.class);
                startActivityForResult(intent, 0);
                break;
            case R.id.btnAmbiente:
                intent= new Intent (getApplicationContext(), Menu_Ambiente.class);
                startActivityForResult(intent, 1);
                break;
            case R.id.btnUsuario:
                intent= new Intent (getApplicationContext(), Menu_Usuario.class);
                startActivityForResult(intent, 2);
                break;
        }
    }
}
