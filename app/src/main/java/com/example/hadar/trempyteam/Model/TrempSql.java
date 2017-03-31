package com.example.hadar.trempyteam.Model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.Date;

/**
 * Created by מנור on 27/03/2017.
 */

public class TrempSql {
    private static final String TREMP = "TREMP";
    private static final String ST_ID = "stId";
    private static final String CAR_MODEL = "carModel";
    private static final String IMAGE_URL = "imageUrl";
    private static final String SOURCE = "source";
    private static final String DEST = "destination";
    private static final String SEETS = "freeSeets";
    private static final String DATE = "TrempDate";
    private static final String PHONE = "PhoneNumber";

    public static void addTremp(SQLiteDatabase writableDatabase, Tremp tremp) {
        ContentValues values = new ContentValues();

        values.put(ST_ID, tremp.Id);
        values.put(SOURCE, tremp.getSourceAddress());
        values.put(DEST, tremp.getDestAddress());
        values.put(SEETS, tremp.getSeets());
        values.put(CAR_MODEL, tremp.getCarModel());
        values.put(DATE, tremp.getTrempDate().toString());
        values.put(PHONE, tremp.getPhoneNumber());
        values.put(IMAGE_URL, tremp.getImageName());

        long rowId = writableDatabase.insert(TREMP, ST_ID, values);
        if (rowId <= 0) {
            Log.e("TAG","fail to insert into student");
        }
    }

    public static Tremp getTrempById(SQLiteDatabase readableDatabase, String id) {
        String[] selectionArgs = {id};
        Cursor cursor = readableDatabase.query(TREMP,null, ST_ID + " = ?",selectionArgs ,null,null,null);        Tremp tremp = null;
        if (cursor.moveToFirst() == true){
            String stId = cursor.getString(cursor.getColumnIndex(ST_ID));
            String source = cursor.getString(cursor.getColumnIndex(SOURCE));
            String dest = cursor.getString(cursor.getColumnIndex(DEST));
            String seets = cursor.getString(cursor.getColumnIndex(SEETS));
            String carModel = cursor.getString(cursor.getColumnIndex(CAR_MODEL));
            String date = cursor.getString(cursor.getColumnIndex(DATE));
            String imageUrl = cursor.getString(cursor.getColumnIndex(IMAGE_URL));
            String phoneNum = cursor.getString(cursor.getColumnIndex(PHONE));

            tremp = new Tremp(Long.getLong(seets),stId,Date.class.cast(date),source, dest,phoneNum,carModel, imageUrl);

        }

        return tremp;
    }

    public static void create(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + TREMP + " (" + ST_ID + " TEXT, " + SOURCE + " TEXT, " + DEST + " TEXT, "  + SEETS + " TEXT, " + CAR_MODEL + " TEXT, " + DATE + " TEXT, " + PHONE + " TEXT, " + IMAGE_URL + " TEXT)");    }

    public static void dropTable(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("DROP TABLE " + TREMP);
    }
}