package a111rosario.comunaalvear;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BasedeDatosUsuarios extends SQLiteOpenHelper {

    public BasedeDatosUsuarios(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE  TABLE usuarios(id integer primary key,nombre text,pass text)");
        db.execSQL("insert into usuarios values (1,'carlos','1111'),(2,'lino','1111'),(3,'nicolas','2222'),(4,'ezequiel','2222'),(5,'alvaro','3333'),(6,'lisandro','3333');");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("CREATE  TABLE usuarios(id integer primary key,nombre text,pass text)");
        db.execSQL("insert into usuarios values (1,'carlos','1111'),(2,'lino','1111'),(3,'nicolas','2222'),(4,'ezequiel','2222'),(5,'alvaro','3333'),(6,'lisandro','3333');");
    }
}
