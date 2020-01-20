package com.example.ventasejemplo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Menu_Usuario extends AppCompatActivity {
    Button btnNuevoU,btnActualizarU,btnEliminarU;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu__usuario);

        btnNuevoU= findViewById(R.id.btnNuevoU);
        btnActualizarU= findViewById(R.id.btnActEliUsuario);
    }
    public void Onclick(View view) {
        Intent intent;
        switch (view.getId()){
            case R.id.btnNuevoU:
                intent= new Intent (getApplicationContext(), RegisterUser.class);
                startActivityForResult(intent, 0);
                break;
            case R.id.btnActEliUsuario:
                intent= new Intent (getApplicationContext(), OperateUsuario.class);
                startActivityForResult(intent, 0);
                break;
        }
    }
}
