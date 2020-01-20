package com.example.ventasejemplo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {
    Button btnLogin;
    TextInputEditText editUsername,editPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnLogin= findViewById(R.id.btnLogin);
        editUsername= findViewById(R.id.edtU);
        editPassword= findViewById(R.id.edtPassword);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Login();
            }
        });

    }
    private void Login(){
        String url=Utilities.url+"/ExamenInter/controllers/Usuario.controlador.php";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    String tipo=jsonObject.getString("usuario");

                    if(tipo.trim().equals("Administrador")){
                        Toast.makeText(getApplicationContext(),"Login exitoso Admin",Toast.LENGTH_SHORT).show();

                        Intent i = new Intent(getApplicationContext(),Menu.class);
                        startActivity(i);
                    }else if(tipo.trim().equals("Controladores")){
                        Toast.makeText(getApplicationContext(),"Login exitoso Controlador",Toast.LENGTH_SHORT).show();

                        Intent i = new Intent(getApplicationContext(),Menu_controlador.class);
                        startActivity(i);
                    }else{
                        Toast.makeText(getApplicationContext(),"Login fallido",Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {

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
                params.put("UserName",editUsername.getText().toString().trim());
                params.put("Password",editPassword.getText().toString().trim());
                params.put("tipo","login_usuario");
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
}
