package com.example.ventasejemplo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class OperateUsuario extends AppCompatActivity {
    Button btnBuscarUsername;
    EditText buscarUsername;
    RequestQueue requestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operate_usuario);

        buscarUsername = findViewById(R.id.buscarUsername);

        btnBuscarUsername = findViewById(R.id.btnBuscarUsername);



        requestQueue = Volley.newRequestQueue(this);

        btnBuscarUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DeleteWebServiceUsuario();
                onBackPressed();
            }
        });
    }
    private void DeleteWebServiceUsuario() {
        String url=Utilities.url+"/ExamenInter/controllers/Usuario.controlador.php";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if(response.trim().equals("success")){
                    Toast.makeText(getApplicationContext(),"Se elimino USUARIO",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(),"No se elimino USUARIO",Toast.LENGTH_SHORT).show();
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

                HashMap<String,String> parametros = new HashMap<>();
                String nick = buscarUsername.getText().toString().trim();
                parametros.put("nick",nick);
                parametros.put("tipo","eliminar_usuario");
                return parametros;
            }
        };
        requestQueue.add(stringRequest);
    }

}
