package com.example.ventasejemplo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegisterAmbiente extends AppCompatActivity {
    private Spinner spinner ;
    private EditText editAmbiente,editCodAmbiente;
    private Button btnRegistrarAmbi;
    public String idd;
    List<Map<String, Object>> rData ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_ambiente);

        editAmbiente = findViewById(R.id.editAmbiente);
        editCodAmbiente = findViewById(R.id.editCodAmbiente);

        btnRegistrarAmbi = findViewById(R.id.btnRegistrarAmbiente);
        rData = new ArrayList<>();
        spinner = (Spinner) findViewById(R.id.sppAmbiente);
        listar();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Map<String, Object> selectedItem = rData.get(spinner.getSelectedItemPosition());
                String name = selectedItem.get("name").toString();
                String id = selectedItem.get("id").toString();
                idd=id;
                //Toast.makeText(getApplicationContext(),"Antes: "+idd,Toast.LENGTH_LONG).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // DO Nothing here
            }
        });

        btnRegistrarAmbi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Register();
                onBackPressed();
            }
        });
    }
    public void listar(){
        RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest=new StringRequest(Request.Method.POST, Utilities.url+"/ExamenInter/controllers/Edificio.controlador.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject=new JSONObject(response);

                            JSONArray jsonArray=jsonObject.getJSONArray("edificios");
                            for(int i=0;i<jsonArray.length();i++){
                                JSONObject jsonObject1=jsonArray.getJSONObject(i);

                                Map<String, Object> item = new HashMap<>();
                                item.put("id", jsonObject1.getInt("id_edif"));
                                item.put("name", jsonObject1.getString("nombre"));

                                rData.add(item);
                            }
                            SimpleAdapter arrayAdapter = new SimpleAdapter(getApplicationContext(), rData,android.R.layout.simple_spinner_dropdown_item,
                                    new String[]{"name"}, new int[]{android.R.id.text1});
                            spinner.setAdapter(arrayAdapter);
                        }catch (JSONException e){e.printStackTrace();}
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Log.e("VALOR",idd);
                Map<String,String> params= new HashMap<>();

                params.put("tipo","consulta_edificio");
                return params;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);
    }

    private void Register() {
        String url=Utilities.url+"/ExamenInter/controllers/Ambiente.controlador.php";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if(response.trim().equals("success")){
                    Toast.makeText(getApplicationContext(),"Se registro Ambiente",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(),"No se registro el Ambiente",Toast.LENGTH_SHORT).show();
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
                //Log.e("VALOR",idd);
                Map<String,String> params= new HashMap<>();

                params.put("ambi",editAmbiente.getText().toString().trim());
                params.put("codAmbiente",editCodAmbiente.getText().toString().trim());
                params.put("id",idd);
                params.put("tipo","insertar_ambiente");
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
}
