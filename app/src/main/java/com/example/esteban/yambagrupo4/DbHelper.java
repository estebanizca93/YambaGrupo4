package com.example.esteban.yambagrupo4;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Esteban on 15/11/2017.
 */

public class DbHelper extends SQLiteOpenHelper {

    private static final String TAG = DbHelper.class.getSimpleName();

    // Constructor
    public DbHelper(Context context) {
        super(context, StatusContract.DB_NAME, null, StatusContract.DB_VERSION);
    }

    //Llamado para crear la tabla!
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = String.format("create table %s (%s int primary key, %s text, %s text, %s int)",
                StatusContract.TABLE,
                StatusContract.Column.ID,
                StatusContract.Column.USER,
                StatusContract.Column.MESSAGE,
                StatusContract.Column.CREATED_AT);

        Log.d(TAG, "onCreate con SQL: " + sql);

        db.execSQL(sql);
    }

    // Llamado siempre que tengamos una nueva version!
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
// Aqui irían las sentencias del tipo ALTER TABLE, de momento lo hacemos mas sencillo:
        db.execSQL("drop table if exists " + StatusContract.TABLE); // borra la vieja base de datos
        onCreate(db); // crea una base de datos nueva!
        Log.d(TAG, "onUpgrade");
    }
}
