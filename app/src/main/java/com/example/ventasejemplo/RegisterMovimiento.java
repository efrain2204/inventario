package com.example.ventasejemplo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.Spinner;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegisterMovimiento extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{
    private Spinner spinner1,spinner2 ;
    EditText editComentarios,editFecha;

    private Button btnRegisterMov,btnObtenerFecha;

    public String idd,idd2;

    List<Map<String, Object>> rData,rData2 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_movimiento);

        editComentarios = findViewById(R.id.editObsevacion);
        editFecha = findViewById(R.id.editDate);

        btnRegisterMov= findViewById(R.id.btnRegisterMov);
        btnObtenerFecha= findViewById(R.id.btnObtenerFecha);

        spinner1 = findViewById(R.id.sppElemee);
        spinner2 = findViewById(R.id.sppAmbii);

        rData = new ArrayList<>();
        rData2 = new ArrayList<>();

        listar();
        listar2();


        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Map<String, Object> selectedItem = rData.get(spinner1.getSelectedItemPosition());
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

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Map<String, Object> selectedItem2 = rData2.get(spinner2.getSelectedItemPosition());
                String name = selectedItem2.get("name").toString();
                String id = selectedItem2.get("id").toString();
                idd2=id;
                //Toast.makeText(getApplicationContext(),"Antes: "+idd2,Toast.LENGTH_LONG).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // DO Nothing here
            }
        });

        btnRegisterMov.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Register();
                onBackPressed();
            }
        });
        btnObtenerFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDailog();
            }
        });
    }

    private void Register() {
        String url=Utilities.url+"/ExamenInter/controllers/registerMovimiento.php";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.trim().equals("success")){
                    Toast.makeText(getApplicationContext(),"Se registro movimiento",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(),"No se registro el movimiento",Toast.LENGTH_SHORT).show();
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
                params.put("cod_ele",idd);
                params.put("cod_am",idd2);
                params.put("fecha",editFecha.getText().toString().trim());
                params.put("comentario",editComentarios.getText().toString());
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    public void listar(){
        RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest=new StringRequest(Request.Method.POST, Utilities.url+"/ExamenInter/controllers/Elemento.controlador.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject=new JSONObject(response);

                            JSONArray jsonArray=jsonObject.getJSONArray("elementos");
                            for(int i=0;i<jsonArray.length();i++){
                                JSONObject jsonObject1=jsonArray.getJSONObject(i);

                                Map<String, Object> item = new HashMap<>();
                                item.put("id", jsonObject1.getInt("id_elem"));
                                item.put("name", jsonObject1.getString("nombre"));

                                rData.add(item);
                            }
                            SimpleAdapter arrayAdapter = new SimpleAdapter(getApplicationContext(), rData,android.R.layout.simple_spinner_dropdown_item,
                                    new String[]{"name"}, new int[]{android.R.id.text1});
                            spinner1.setAdapter(arrayAdapter);
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

                params.put("tipo","consulta_elemento");

                return params;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);
    }
    public void listar2(){
        RequestQueue requestQueue=Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest=new StringRequest(Request.Method.POST, Utilities.url+"/ExamenInter/controllers/Ambiente.controlador.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try{
                            JSONObject jsonObject=new JSONObject(response);
                            JSONArray jsonArray=jsonObject.getJSONArray("ambientes");

                            for(int i=0;i<jsonArray.length();i++){
                                JSONObject jsonObject1=jsonArray.getJSONObject(i);

                                Map<String, Object> item2 = new HashMap<>();

                                item2.put("id", jsonObject1.getInt("id_amb"));
                                item2.put("name", jsonObject1.getString("tipo"));

                                rData2.add(item2);
                            }
                            SimpleAdapter arrayAdapter2 = new SimpleAdapter(getApplicationContext(), rData2,android.R.layout.simple_spinner_dropdown_item,
                                    new String[]{"name"}, new int[]{android.R.id.text1});
                            spinner2.setAdapter(arrayAdapter2);
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

                params.put("tipo","consulta_ambiente");
                return params;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);
    }

    private void showDatePickerDailog(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String date= year + "-"+ month + "-" + dayOfMonth;
        editFecha.setText(date);
    }
}
