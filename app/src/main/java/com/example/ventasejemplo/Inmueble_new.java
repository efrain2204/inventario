package com.example.ventasejemplo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Base64;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class Inmueble_new extends AppCompatActivity {
    private Bitmap bitmap;
    public static EditText edCodigo;
    private EditText edNombre,edMarca,edModelo,edTipo,edColor,edSerie,edEstado;

    /*Imagen*/
    ImageView imagen;
    Button btnTake;
    private final String CARPETA_RAIZ="misImagenesPrueba/";
    private final String RUTA_IMAGEN=CARPETA_RAIZ+"MisFotos";
    String path="";
    final int COD_SELECCIONA=10;
    final int COD_FOTO=20;

    /*Scanner*/
    private Button btnScanner;

    /*PROCESAR BOTON */
    private Button btnEnviar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inmueble__new);

        /*Protocolos para URL  con seguridad*/
        handleSSLHandshake();

        edNombre= (EditText)findViewById(R.id.edNombre);
        edCodigo= (EditText)findViewById(R.id.edCodigo);
        edMarca= (EditText)findViewById(R.id.edMarca);
        edModelo= (EditText)findViewById(R.id.edModelo);
        edTipo= (EditText)findViewById(R.id.edTipo);
        edColor= (EditText)findViewById(R.id.edColor);
        edSerie= (EditText)findViewById(R.id.edSerie);
        edEstado= (EditText)findViewById(R.id.edEstado);

        imagen = (ImageView)findViewById(R.id.photo);
        btnTake = (Button) findViewById(R.id.btnTake);

        btnEnviar = (Button) findViewById(R.id.btnEnviar);


        /*Scanner*/
        btnScanner = (Button) findViewById(R.id.btnScanner);

        /*Permisos*/
        if(validaPermisos()){
            btnTake.setEnabled(true);
        }else{
            btnTake.setEnabled(false);
        }

    }
    public  void onclick(View view){
        switch (view.getId()){
            case R.id.btnScanner:
                Intent intent = new Intent(getApplicationContext(), ScannerActivity.class);
                intent.putExtra("destino","new");
                startActivity(intent);
                break;
            case R.id.btnTake:
                cargarImagen();
                break;
            case R.id.btnEnviar:
                cargarWebService();
                onBackPressed();
                break;
        }

    }

    private void cargarWebService() {
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        StringRequest request= new StringRequest(Request.Method.POST, Utilities.url+"/ExamenInter/controllers/Elemento.controlador.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("msj",response);
                        if(response.trim().equals("success")){
                            imagen.setImageResource(R.drawable.camara);
                            Toast.makeText(getApplicationContext(),"Se registro",Toast.LENGTH_SHORT).show();

                        }else{
                            Toast.makeText(getApplicationContext(),"No se  registro",Toast.LENGTH_SHORT).show();
                        }
                    }
                },new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),"No se a podido conectar: "+ error,Toast.LENGTH_SHORT).show();
                    }
                }){
                protected Map<String,String> getParams() throws AuthFailureError{
                    HashMap<String,String> parametros = new HashMap<>();

                    String nombre = edNombre.getText().toString().trim();
                    String codigo = edCodigo.getText().toString().trim();
                    String marca = edMarca.getText().toString().trim();
                    String modelo = edModelo.getText().toString().trim();
                    String tipo = edTipo.getText().toString().trim();
                    String color = edColor.getText().toString().trim();
                    String serie = edSerie.getText().toString().trim();
                    String estado = edEstado.getText().toString().trim();

                    bitmap = ((BitmapDrawable)imagen.getDrawable()).getBitmap();
                    String imagen = convertirImgString(bitmap);

                    parametros.put("nombre",nombre);
                    parametros.put("codigo",codigo);
                    parametros.put("marca",marca);
                    parametros.put("modelo",modelo);
                    parametros.put("tipoo",tipo);
                    parametros.put("color",color);
                    parametros.put("serie",serie);
                    parametros.put("estado",estado);

                    parametros.put("imagen",imagen);

                    parametros.put("tipo","insertar_elemento");

                    return parametros;
                }
        };
        requestQueue.add(request);
    }

    private String convertirImgString(Bitmap bitmap) {
        ByteArrayOutputStream array = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,array);
        byte[] imagenByte = array.toByteArray();
        String imagenString = Base64.encodeToString(imagenByte,Base64.DEFAULT);
        return imagenString;
    }

    private void cargarDialogoRecomendacion() {
        AlertDialog.Builder dialogo = new AlertDialog.Builder(Inmueble_new.this);
        dialogo.setTitle("Permisos Desactivados");
        dialogo.setMessage("Debe aceptar los permisos para la App");
        dialogo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE,CAMERA},100);
            }
        });
        dialogo.show();
    }

    private void cargarImagen() {
        final CharSequence[] opciones = {"Tomar Foto","Cargar Imagen","Cancelar"};
        final AlertDialog.Builder alertOpciones = new AlertDialog.Builder(Inmueble_new.this);
        alertOpciones.setTitle("Seleccione una opcion");
        alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(opciones[i].equals("Tomar Foto")){
                    TomarFotografia();
                }else{
                    if(opciones[i].equals("Cargar Imagen")){
                        Intent intent= new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image/");
                        startActivityForResult(intent.createChooser(intent,"Seleccione la Aplicacion"),10);
                    }else{
                        dialogInterface.dismiss();
                    }
                }
            }
        });
        alertOpciones.show();


    }

    private void TomarFotografia() {
        File fileImagen = new File(Environment.getExternalStorageDirectory(), RUTA_IMAGEN);
        boolean isCreada = fileImagen.exists();
        String nombreImagen = "";
        if (isCreada == false) {
            isCreada = fileImagen.mkdirs();
        }
        if (isCreada == true) {
            nombreImagen = (System.currentTimeMillis() / 1000) + ".jpg";
        }

        path = Environment.getExternalStorageDirectory()
                + File.separator + RUTA_IMAGEN + File.separator + nombreImagen;
        File imagen = new File(path);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.N){
            String authorities = getApplicationContext().getPackageName()+".provider";
            Uri imageUri = FileProvider.getUriForFile(this,authorities,imagen);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        }else{
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imagen));
        }
        startActivityForResult(intent,COD_FOTO);
    }

    private boolean validaPermisos() {
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            return true;
        }
        if( (checkSelfPermission(CAMERA)== PackageManager.PERMISSION_GRANTED) &&
                (checkSelfPermission(WRITE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED)){
            return true;
        }
        if( (shouldShowRequestPermissionRationale(CAMERA))
                || (shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE))){
            cargarDialogoRecomendacion();
        }else{
            requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE,CAMERA},100);
        }
        return false;
    }

    private void solicitarPermisosManual() {
        final CharSequence[] opciones = {"si","no"};
        final AlertDialog.Builder alertOpciones = new AlertDialog.Builder(Inmueble_new.this);
        alertOpciones.setTitle("Configuracion Manual");
        alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(opciones[i].equals("si")){
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package",getPackageName(),null);
                    intent.setData(uri);
                    startActivity(intent);
                }else{
                    Toast.makeText(getApplicationContext(),"Permisos denegados",Toast.LENGTH_SHORT).show();
                    dialogInterface.dismiss();
                }
            }
        });
        alertOpciones.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==100){
            if(grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1]==PackageManager.PERMISSION_GRANTED){
                btnTake.setEnabled(true);
            }else{
                solicitarPermisosManual();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode==RESULT_OK){
            switch (requestCode){
                case COD_SELECCIONA:
                    Uri miPath = data.getData();
                    imagen.setImageURI(miPath);
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),miPath);
                        imagen.setImageBitmap(bitmap);
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                    break;
                case COD_FOTO:
                    MediaScannerConnection.scanFile(this, new String[]{path}, null, new MediaScannerConnection.OnScanCompletedListener() {
                                @Override
                                public void onScanCompleted(String s, Uri uri) {
                                    Log.i("Ruta de almacenamiento","Path:"+path);
                                }
                            });
                    Bitmap bitmap = BitmapFactory.decodeFile(path);
                    imagen.setImageBitmap(bitmap);
                    break;
            }
        }


    }

    @SuppressLint("TrulyRandom")
    public static void handleSSLHandshake() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }

                @Override
                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }};

            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String arg0, SSLSession arg1) {
                    return true;
                }
            });
        } catch (Exception ignored) {
        }
    }
}
