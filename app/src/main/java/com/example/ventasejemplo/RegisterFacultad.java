package com.example.ventasejemplo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class RegisterFacultad extends AppCompatActivity {
    private EditText edFacu;
    private Button btnRegistroFacu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_facultad);

        edFacu= (EditText) findViewById(R.id.editFacultad);

        btnRegistroFacu= (Button) findViewById(R.id.btnRegistroFacu);
        btnRegistroFacu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegisterFacu();
                onBackPressed();
            }
        });
    }

    private void RegisterFacu() {
        String url=Utilities.url+"/ExamenInter/controllers/Facultad.controlador.php";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.trim().equals("success")){
                    Toast.makeText(getApplicationContext(),"Se registro la facultad exitosamente",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(),"No se registro la facultad",Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"error: ="+error.toString(),Toast.LENGTH_SHORT).show();
                Log.e("ErrorEfrain",error.toString());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> params= new HashMap<>();
                params.put("facultad",edFacu.getText().toString().trim());
                params.put("tipo","insertar_facultad");
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
}
