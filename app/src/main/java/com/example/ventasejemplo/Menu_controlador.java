package com.example.ventasejemplo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Menu_controlador extends AppCompatActivity {
    Button btnInmuebles;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_controlador);

        btnInmuebles= findViewById(R.id.btnInmuebles);
    }

    public void Onclick(View view) {
        Intent intent;
        switch (view.getId()){
            case R.id.btnInmuebles:
                intent= new Intent (getApplicationContext(), Menu_Inmuebles.class);
                startActivityForResult(intent, 0);
                break;
        }
    }
}
