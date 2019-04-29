package a111rosario.comunaalvear;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Debug;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText edt_usuario;
    EditText edt_pass;

    Button btn_login;
    //ImageView img_logo;

    private Cursor fila;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //relacionar los elementos del XML con la lógica en Java

        edt_usuario=findViewById(R.id.edt_usuario);
        edt_pass=findViewById(R.id.edt_pass);
        btn_login=findViewById(R.id.btn_login);
        //img_logo=findViewById(R.id.img_logo);
    }

public void onClick(View v) {//metodo para el login




        if(!edt_usuario.getText().toString().isEmpty() && !edt_pass.getText().toString().isEmpty()) {


            BasedeDatosUsuarios conexion=new BasedeDatosUsuarios(this,"conectar",null,1);
            SQLiteDatabase db=conexion.getWritableDatabase();
            String nomusuario=edt_usuario.getText().toString();
            String pass=edt_pass.getText().toString();
            fila=db.rawQuery("select nombre,pass from usuarios where nombre='"+nomusuario+"' and pass='"+pass+"'",null);

            if(fila.moveToFirst()){
                String tempusuario=fila.getString(0);
                String temppass=fila.getString(1);

                if(nomusuario.equals(tempusuario)&&pass.equals(temppass)){//si el usuario y contraseña es igual al de la base de datos entonces puede ir al otro activity

                    Intent cambiandoAct = new Intent(MainActivity.this, Formulario.class);//una vez creado el intent hay que poner el contexto en el que se está, en este caso Main Activity y al que se quiere ir FotoActivity
                    startActivity(cambiandoAct);//empieza el activity al que se quiere ir

                }

            }
            else{
                Toast.makeText(this,"Usuario/contraseña incorrecto",Toast.LENGTH_SHORT).show();
            }



        }
        else{//si usuario y contraseña están vacíos, no pueden ir al activity formulario
            Toast.makeText(this,"Falta usuario y/o contraseña",Toast.LENGTH_SHORT).show();
        }

}


}
