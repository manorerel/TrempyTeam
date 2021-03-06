package com.example.hadar.trempyteam.Model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

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
    private static final String DRIVER_ID = "driverId";
    private static final String SEETS = "freeSeets";
    private static final String DATE = "trempDateTime";
    private static final String PHONE = "PhoneNumber";
    private static final String IS_CREATED = "isCreated";

    public static void addTremp(SQLiteDatabase writableDatabase, Tremp tremp, boolean isCreated) {
        ContentValues values = new ContentValues();

        values.put(ST_ID, tremp.id);
        values.put(DRIVER_ID, tremp.getDriverId());
//        values.put(SOURCE, tremp.getSourceAddress());
//        values.put(DEST, tremp.getDestAddress());
        values.put(SEETS, tremp.getSeets());
        values.put(CAR_MODEL, tremp.getCarModel());
        values.put(DATE, tremp.getTrempDateTime());
        values.put(PHONE, tremp.getPhoneNumber());
        values.put(IMAGE_URL, tremp.getImageName());

        if(isCreated)
        values.put(IS_CREATED, "true");
        else values.put(IS_CREATED, "false");

        long rowId = writableDatabase.insert(TREMP, ST_ID, values);
        if (rowId <= 0) {
            Log.e("TAG","fail to insert into student");
        }
    }

    public static Tremp getTrempById(SQLiteDatabase readableDatabase, String id) {
        String[] selectionArgs = {id};
        Cursor cursor = readableDatabase.query(TREMP,null, ST_ID + " = ?",selectionArgs ,null,null,null);
        Tremp tremp = null;
        if (cursor.moveToFirst() == true){
            String stId = cursor.getString(cursor.getColumnIndex(ST_ID));
            String driverId = cursor.getString(cursor.getColumnIndex(DRIVER_ID));
            String source = cursor.getString(cursor.getColumnIndex(SOURCE));
            String dest = cursor.getString(cursor.getColumnIndex(DEST));
            String seets = cursor.getString(cursor.getColumnIndex(SEETS));
            String carModel = cursor.getString(cursor.getColumnIndex(CAR_MODEL));
            String date = cursor.getString(cursor.getColumnIndex(DATE));
            String imageUrl = cursor.getString(cursor.getColumnIndex(IMAGE_URL));
            String phoneNum = cursor.getString(cursor.getColumnIndex(PHONE));

            long trempSeets = Long.parseLong(seets);

//            tremp = new Tremp(stId, trempSeets,driverId,date,, null,phoneNum,carModel, imageUrl, null);

        }

        return tremp;
    }

    public static List<Tremp> GetAllTremps(SQLiteDatabase readableDatabase, boolean isCreated){
        Tremp tremp = null;
        Cursor cursor;

        if(isCreated){
            String[] selectionArgs = {"true"};
            cursor = readableDatabase.query(TREMP,null, IS_CREATED + " = ?",selectionArgs, null, null, null, null);}
        else {
            String[] selectionArgs = {"false"};
            cursor = readableDatabase.query(TREMP,null, IS_CREATED + " = ?",selectionArgs, null, null, null, null);}
        List<Tremp> tremps = new LinkedList<Tremp>();

        if (cursor.moveToFirst() == true){
            do {
                String stId = cursor.getString(cursor.getColumnIndex(ST_ID));
                String driverId = cursor.getString(cursor.getColumnIndex(DRIVER_ID));
                String source = cursor.getString(cursor.getColumnIndex(SOURCE));
                String dest = cursor.getString(cursor.getColumnIndex(DEST));
                String seets = cursor.getString(cursor.getColumnIndex(SEETS));
                String carModel = cursor.getString(cursor.getColumnIndex(CAR_MODEL));
                String date = cursor.getString(cursor.getColumnIndex(DATE));
                String imageUrl = cursor.getString(cursor.getColumnIndex(IMAGE_URL));
                String phoneNum = cursor.getString(cursor.getColumnIndex(PHONE));

                long trempSeets = Long.parseLong(seets);
                tremp = new Tremp(stId, trempSeets, driverId, date, source, dest, phoneNum, carModel, imageUrl, null);

                tremps.add(tremp);
            }
            while (cursor.moveToNext());
        }
        return tremps;
    }



    public static void create(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + TREMP + " (" + ST_ID + " TEXT, "+ DRIVER_ID + " TEXT, " + SOURCE + " TEXT, " + DEST + " TEXT, "  + SEETS + " TEXT, " + CAR_MODEL + " TEXT, " + DATE + " TEXT, " + PHONE + " TEXT, " + IMAGE_URL +" TEXT, " + IS_CREATED+ " TEXT)");    }

    public static void dropTable(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("DROP TABLE " + TREMP);
    }

    public static void DeleteTremp(SQLiteDatabase sqLiteDatabase, String trempId){
        String[] selectionArgs = {trempId};
        try {
            sqLiteDatabase.delete(TREMP,ST_ID + "= ?", selectionArgs);
        }
        catch (Exception e){}

    }

    public static void UpdateTremp(SQLiteDatabase database, String id, String dest, String source, String phone, String date, String car){
        String[] selectionArgs = {id};
        ContentValues cv = new ContentValues();
        cv.put(DEST,dest);
        cv.put(SOURCE,source);
        cv.put(PHONE,phone);
        cv.put(DATE,date);
        cv.put(CAR_MODEL,car);
        database.update(TREMP,cv, ST_ID+"=?",selectionArgs);


    }

    private static String[] getTableColumn(){
        String[] column = {ST_ID,SOURCE,DEST,PHONE,CAR_MODEL,DATE,IMAGE_URL};
        return column;
    }
}