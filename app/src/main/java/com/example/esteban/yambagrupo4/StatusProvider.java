package com.example.esteban.yambagrupo4;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

/**
 * Created by Esteban on 22/11/2017.
 */

public class StatusProvider extends ContentProvider {

    private static final String TAG = StatusProvider.class.getSimpleName();
    private DbHelper dbHelper;
    private static final UriMatcher sURIMatcher;

    static {
        sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sURIMatcher.addURI(StatusContract.AUTHORITY, StatusContract.TABLE, StatusContract.STATUS_DIR);
        sURIMatcher.addURI(StatusContract.AUTHORITY, StatusContract.TABLE + "/#", StatusContract.STATUS_ITEM);
    }

    @Override
    public boolean onCreate() {
        dbHelper = new DbHelper(getContext());
        Log.d(TAG, "onCreated");
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        String where;

        switch (sURIMatcher.match(uri)) {
            case StatusContract.STATUS_DIR:
                where = selection;
                break;
            case StatusContract.STATUS_ITEM:
                long id = ContentUris.parseId(uri);
                where = StatusContract.Column.ID + "=" + id;
                break;
            default:
                throw new IllegalArgumentException("uri incorrecta: " + uri);
        }
        String orderBy = (TextUtils.isEmpty(sortOrder)) ? StatusContract.DEFAULT_SORT : sortOrder;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(StatusContract.TABLE, projection, where, selectionArgs, null, null, orderBy);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        Log.d(TAG, "registros recuperados: " + cursor.getCount());
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (sURIMatcher.match(uri)) {
            case StatusContract.STATUS_DIR:
                return "vnd.android.cursor.dir/vnd.com.example.esteban.yambagrupo4.provider.status";
            case StatusContract.STATUS_ITEM:
                return "vnd.android.cursor.item/vnd.com.example.esteban.yambagrupo4.provider.status";
            default:
                throw new IllegalArgumentException("uri incorrecta: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        Uri ret = null;

        // Nos aseguramos de que la URI es correcta!
        if (sURIMatcher.match(uri) != StatusContract.STATUS_DIR) {
            throw new IllegalArgumentException("uri incorrecta: " + uri);
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long rowId = db.insertWithOnConflict(StatusContract.TABLE, null, values, SQLiteDatabase.CONFLICT_IGNORE);

        // Se inserto correctamente?!
        if (rowId != -1) {
            Log.d(TAG, "uri insertada con id: " + values.getAsLong(StatusContract.Column.ID));

            // Notificar que los datos para la URI han cambiado
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return ret;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        String where;

        switch (sURIMatcher.match(uri)) {
            case StatusContract.STATUS_DIR:
                where = selection;
                break;
            case StatusContract.STATUS_ITEM:
                long id = ContentUris.parseId(uri);
                where = StatusContract.Column.ID + "=" + id;
                break;
            default:
                throw new IllegalArgumentException("uri incorrecta: " + uri);
        }
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int ret = db.delete(StatusContract.TABLE, where, selectionArgs);
        if (ret > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        Log.d(TAG, "registros borrados: " + ret);
        return ret;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        String where;

        switch (sURIMatcher.match(uri)) {
            case StatusContract.STATUS_DIR:
                where = selection;
                break;
            case StatusContract.STATUS_ITEM:
                long id = ContentUris.parseId(uri);
                where = StatusContract.Column.ID + "=" + id;
                break;
            default:
                throw new IllegalArgumentException("uri incorrecta: " + uri);
        }
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int ret = db.update(StatusContract.TABLE, values, where, selectionArgs);
        if (ret > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        Log.d(TAG, "registros actualizados: " + ret);
        return ret;
    }
}
