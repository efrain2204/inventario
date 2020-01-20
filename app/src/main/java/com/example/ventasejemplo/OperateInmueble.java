package com.example.ventasejemplo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

public class OperateInmueble extends AppCompatActivity {
    public static EditText editCodBuscar;


    private EditText editarNombre, editarMarca,editarModelo,editarTipo,editarColor,editarSerie,editarEstado;
    Button btnBuscar,btnSet,btnActualizar,btnEliminar;


    RequestQueue requestQueue;
    JsonObjectRequest jsonObjectRequest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operate_inmueble);

        editCodBuscar=findViewById(R.id.editCodBuscar);
        editarNombre=findViewById(R.id.editarNombre);
        editarMarca=findViewById(R.id.editarMarca);
        editarModelo=findViewById(R.id.editarModelo);
        editarTipo=findViewById(R.id.editarTipo);
        editarColor=findViewById(R.id.editarColor);
        editarSerie=findViewById(R.id.editarSerie);
        editarEstado=findViewById(R.id.editarEstado);


        btnBuscar = findViewById(R.id.btnBuscar);
        btnSet = findViewById(R.id.btnSet);
        btnActualizar = findViewById(R.id.botonActualizar);
        btnEliminar = findViewById(R.id.botonEliminar);

        requestQueue = Volley.newRequestQueue(this);

        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ScannerActivity.class);
                intent.putExtra("destino","update");
                startActivity(intent);
            }
        });
        btnSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CargarWebService();
                enableEdit();
                btnActualizar.setEnabled(true);
                btnEliminar.setEnabled(true);
            }
        });
        btnActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateWebService();
                onBackPressed();
            }
        });

        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DeleteWebService();
                onBackPressed();
            }
        });
    }

    private void DeleteWebService() {
        String url=Utilities.url+"/ExamenInter/controllers/Elemento.controlador.php";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.trim().equals("success")){
                    Toast.makeText(getApplicationContext(),"Se elimino inmueble",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(),"No se elimino inmueble",Toast.LENGTH_SHORT).show();
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
                String codigo = editCodBuscar.getText().toString().trim();

                parametros.put("codigo",codigo);
                parametros.put("tipo","eliminar_elemento");
                return parametros;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void UpdateWebService() {
        String url=Utilities.url+"/ExamenInter/controllers/Elemento.controlador.php";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.trim().equals("success")){
                    Toast.makeText(getApplicationContext(),"Se actualizo inmueble",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getApplicationContext(),"No se actualizo inmueble",Toast.LENGTH_SHORT).show();
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

                String nombre = editarNombre.getText().toString().trim();
                String codigo = editCodBuscar.getText().toString().trim();
                String marca = editarMarca.getText().toString().trim();
                String modelo = editarModelo.getText().toString().trim();
                String tipo = editarTipo.getText().toString().trim();
                String color = editarColor.getText().toString().trim();
                String serie = editarSerie.getText().toString().trim();
                String estado = editarEstado.getText().toString().trim();

                parametros.put("nombre",nombre);
                parametros.put("codigo",codigo);
                parametros.put("marca",marca);
                parametros.put("modelo",modelo);
                parametros.put("tipoo",tipo);
                parametros.put("color",color);
                parametros.put("serie",serie);
                parametros.put("estado",estado);

                parametros.put("tipo","actualizar_elemento");
                return parametros;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void CargarWebService(){
        String url=Utilities.url+"/ExamenInter/controllers/consultarElementoUrl.php?id="+editCodBuscar.getText().toString().trim();

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray json = response.optJSONArray("elemento");

                JSONObject jsonObject = null;
                String nombre="",marca="",modelo="",tipo="",color="",serie="",estado="";
                try {
                    jsonObject=json.getJSONObject(0);
                    nombre=jsonObject.optString("nombre");
                    marca=jsonObject.optString("marca");
                    modelo=jsonObject.optString("modelo");
                    tipo=jsonObject.optString("tipo");
                    color=jsonObject.optString("color");
                    serie=jsonObject.optString("serie");
                    estado=jsonObject.optString("estado");

                }catch (JSONException e){
                    e.printStackTrace();
                }
                editarNombre.setText(nombre);
                editarMarca.setText(marca);
                editarModelo.setText(modelo);
                editarTipo.setText(tipo);
                editarColor.setText(color);
                editarSerie.setText(serie);
                editarEstado.setText(estado);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    private void enableEdit(){
        editarNombre.setEnabled(true);
        editarMarca.setEnabled(true);
        editarModelo.setEnabled(true);
        editarTipo.setEnabled(true);
        editarColor.setEnabled(true);
        editarSerie.setEnabled(true);
        editarEstado.setEnabled(true);
    }
}
