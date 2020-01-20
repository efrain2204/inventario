package com.example.ventasejemplo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScannerActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    ZXingScannerView ScannerView;
    String valor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        valor= getIntent().getStringExtra("destino");
        ScannerView =new ZXingScannerView(this);

        setContentView(ScannerView);

    }

    @Override
    public void handleResult(Result result) {
        if(valor.equalsIgnoreCase("new")){
            Inmueble_new.edCodigo.setText(result.getText());
        }else if(valor.equalsIgnoreCase("update")){
            OperateInmueble.editCodBuscar.setText(result.getText());

        }
        onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        ScannerView.stopCamera();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ScannerView.setResultHandler(this);
        ScannerView.startCamera();
    }
}
