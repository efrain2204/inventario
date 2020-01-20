package com.example.ventasejemplo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Menu_Inmuebles extends AppCompatActivity {
    Button btnNuevo,btnActualizar,btnEliminar,btnMover;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu__inmuebles);

        btnNuevo= findViewById(R.id.btnNuevo);
        btnActualizar= findViewById(R.id.btnActualizarEliminar);
        btnMover= findViewById(R.id.btnMover);
    }

    public void Onclick(View view) {
        Intent intent;
        switch (view.getId()){
            case R.id.btnNuevo:
                intent= new Intent (getApplicationContext(), Inmueble_new.class);
                startActivityForResult(intent, 213);
                break;
            case R.id.btnActualizarEliminar:
                intent= new Intent (getApplicationContext(), OperateInmueble.class);
                startActivityForResult(intent, 89);
                break;
            case R.id.btnMover:
                intent= new Intent (getApplicationContext(), RegisterMovimiento.class);
                startActivityForResult(intent, 110);
                break;
        }
    }
}
