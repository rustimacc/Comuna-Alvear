package a111rosario.comunaalvear;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.renderscript.ScriptGroup;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Formulario extends AppCompatActivity {


    EditText edt_idmedidor;
    EditText edt_consumo;
    EditText edt_observaciones;

    Button btn_leercodigo;
    Button btn_enviar;

   Spinner spn_novedades;
    String novedad[]={"Opcion 1","Opcion 2","Opcion 3","Opcion 4",};

    Bundle Codigodevuelto;//el Bundle sirve para poder mover información de un activity a otro

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        //Instanciando elementos
        edt_consumo=findViewById(R.id.edt_consumo);
        edt_idmedidor=findViewById(R.id.edt_idmedidor);
        edt_observaciones=findViewById(R.id.edt_observaciones);
        btn_leercodigo=findViewById(R.id.btn_leercodigo);
        btn_enviar=findViewById(R.id.btn_enviar);
        //En este caso al Spinner hay que agregarle el array String que cree en values/arreglonovedades
        spn_novedades=findViewById(R.id.spn_novedades);
        ArrayAdapter<String> adapter=new ArrayAdapter(this,android.R.layout.simple_spinner_item,novedad);
        spn_novedades.setAdapter(adapter);
        Codigodevuelto=this.getIntent().getExtras();
        if(Codigodevuelto!=null){
            String codigoDebarra=Codigodevuelto.getString("codigo");
            edt_idmedidor.setText(codigoDebarra);
        }

    }

    public void save(View v) {
        edt_consumo=findViewById(R.id.edt_consumo);
        edt_idmedidor=findViewById(R.id.edt_idmedidor);
        String myconsumo = edt_consumo.getText().toString();
        String medidor = edt_idmedidor.getText().toString();
        //Se obtiene la fecha
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Date date = new Date();
        String actFecha = dateFormat.format(date);
        //
        Backgroundtask b1 = new Backgroundtask();

        b1.execute(myconsumo,medidor,actFecha);



    }

    public void obtenercodigo(View v){
        Intent intent=new Intent(this,LeerCodigodeBarras.class);
        startActivity(intent);
    }
    public void set_Codigo(String codigo){
        edt_idmedidor.setText(codigo);
    }
    class Backgroundtask extends AsyncTask<String,Void,String> {
        String myurl;

       public boolean vaciar_formulario=false;
        @Override
        protected String doInBackground(String... voids) {
            String consumo = voids[0];
            String medidor = voids[1];
            String fecha = voids[2];

            try {
                URL url = new URL(myurl);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                String my_data = URLEncoder.encode(consumo, "UTF-8");
                String my_data2 = URLEncoder.encode(medidor, "UTF-8");
                String my_data_fecha = URLEncoder.encode(fecha, "UTF-8");
                String params = "consumo=" + my_data + "&" + "medidor=" + my_data2 + "&" + "fecha=" + my_data_fecha;
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                bw.write(params);
                bw.flush();
                bw.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                inputStream.close();
                httpURLConnection.disconnect();
                vaciar_formulario=true;
                return "Se hizo el update xd";
            }catch (MalformedURLException e) {
                vaciar_formulario=false;
                e.printStackTrace();
            }catch (IOException e) {
                vaciar_formulario=false;
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPreExecute() {
            myurl = "http://comunaalvear.eu5.org/db/agregar.php";
        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();


            if(result=="Se hizo el update xd") {//Si se pudo hacer el update, se limpia el formulario
                edt_idmedidor.getText().clear();
                edt_consumo.getText().clear();
                edt_observaciones.getText().clear();
            }
            else{//Si hay algún error al conectar se mantienen los datos en el formulario para intentar de nuevo
                Toast.makeText(getApplicationContext(), "error al conectar", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }
}

