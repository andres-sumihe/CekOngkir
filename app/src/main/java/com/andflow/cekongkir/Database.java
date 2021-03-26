package com.andflow.cekongkir;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

public class Database extends SQLiteOpenHelper {
    // static variable
    private static final int DATABASE_VERSION = 2;

    // Database name
    private static final String DATABASE_NAME = "CekOngkir";

    // table name
    private static final String TABLE_COST = "cost";
    private static final String TABLE_NOTIF = "notifikasi";

    // column tables
    private static final String KEY_NO_ID = "id";
    private static final String KEY_ORIGIN_ID = "origin";
    private static final String KEY_DESTINATION_ID = "destination";
    private static final String KEY_WEIGHT = "weight";
    private static final String KEY_COURIER = "courier";
    private static final String KEY_ORIGIN_NAME = "origin_name";
    private static final String KEY_DESTINATION_NAME = "destination_name";

    private static final String KEY_NOTIF_NO_ID = "notif_no";
    private static final String KEY_JUDUL = "judul";
    private static final String KEY_ISI = "isi";

    public Database(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //Create table
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_NOTIF_TABLE = "CREATE TABLE " + TABLE_NOTIF + "("
                + KEY_NOTIF_NO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_JUDUL+ " TEXT,"
                + KEY_ISI + " TEXT" + ")";
        db.execSQL(CREATE_NOTIF_TABLE);

        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_COST + "("
                + KEY_NO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_ORIGIN_ID + " TEXT,"
                + KEY_DESTINATION_ID + " TEXT,"
                + KEY_WEIGHT + " INTEGER,"
                + KEY_COURIER+ " TEXT,"
                + KEY_ORIGIN_NAME+ " TEXT,"
                + KEY_DESTINATION_NAME+ " TEXT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    // on Upgrade database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " +TABLE_NOTIF);
        db.execSQL("DROP TABLE IF EXISTS " +TABLE_COST);
        onCreate(db);
    }

    public void addRecord(costModels costModels){
        SQLiteDatabase db  = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ORIGIN_ID, costModels.getOrigin_id());
        values.put(KEY_DESTINATION_ID, costModels.getDestination_id());
        values.put(KEY_WEIGHT, costModels.getWeight());
        values.put(KEY_COURIER, costModels.getCourier());
        values.put(KEY_ORIGIN_NAME, costModels.getOrigin_name());
        values.put(KEY_DESTINATION_NAME, costModels.getDestination_name());
        db.insert(TABLE_COST, null, values);
        Log.d("ROW Add", "Berhasil Upload");
        db.close();
    }

    // get All Record
    public ArrayList<HashMap<String, String>> getAllRecord() {
        // Select All Query
        ArrayList<HashMap<String, String>> costList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_COST;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                HashMap<String,String> cost = new HashMap<>();
                cost.put("id",cursor.getString(cursor.getColumnIndex(KEY_NO_ID)));
                cost.put("origin-id",cursor.getString(cursor.getColumnIndex(KEY_ORIGIN_ID)));
                cost.put("destination-id", cursor.getString(cursor.getColumnIndex(KEY_DESTINATION_ID)));
                cost.put("weight",cursor.getString(cursor.getColumnIndex(KEY_WEIGHT)));
                cost.put("courier", cursor.getString(cursor.getColumnIndex(KEY_COURIER)));
                cost.put("origin-name",cursor.getString(cursor.getColumnIndex(KEY_ORIGIN_NAME)));
                cost.put("destination-name", cursor.getString(cursor.getColumnIndex(KEY_DESTINATION_NAME)));
                costList.add(cost);
            } while (cursor.moveToNext());
        }
        // return cost list

        return costList;
    }

    public void deleteCost(int no_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_COST, KEY_NO_ID + " = ?",
                new String[] { String.valueOf(no_id)});
        db.close();
    }

    public void addRecordNotif(notifModels notifModels){
        SQLiteDatabase db  = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_JUDUL, notifModels.getJudul());
        values.put(KEY_ISI, notifModels.getIsi());
        db.insert(TABLE_NOTIF, null, values);
        Log.d("ROW Add", "Berhasil Upload");
        db.close();
    }

    // get All Record
    public ArrayList<HashMap<String, String>> getAllRecordNotif() {
        // Select All Query
        ArrayList<HashMap<String, String>> notifList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_NOTIF;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                HashMap<String,String> cost = new HashMap<>();
                cost.put("notif_no",cursor.getString(cursor.getColumnIndex(KEY_NOTIF_NO_ID)));
                cost.put("judul",cursor.getString(cursor.getColumnIndex(KEY_JUDUL)));
                cost.put("isi", cursor.getString(cursor.getColumnIndex(KEY_ISI)));
                notifList.add(cost);
            } while (cursor.moveToNext());
        }
        // return cost list

        return notifList;
    }


    public void deleteNotif(int notif_no_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NOTIF, KEY_NOTIF_NO_ID+ " = ?",
                new String[] { String.valueOf(notif_no_id)});
        db.close();
    }

    public void deleteAllNotif(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ TABLE_NOTIF);
    }
}
