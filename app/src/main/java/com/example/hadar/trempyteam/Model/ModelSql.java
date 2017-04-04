package com.example.hadar.trempyteam.Model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.hadar.trempyteam.MainAactivity;
import com.example.hadar.trempyteam.TrempyApp;

import java.util.Date;
import java.util.List;

/**
 * Created by מנור on 27/03/2017.
 */

public class ModelSql {
    private SQLiteOpenHelper trempHelper;
    private SQLiteOpenHelper userHelper;
    private int version = 2;
    private static ModelSql modelSql;

    public ModelSql(){
        try {
            trempHelper = new TrempHelper(TrempyApp.getAppContext());
            userHelper = new UserHelper(TrempyApp.getAppContext());
        }
        catch(Exception e){
            Log.d("exception", "exception while trying to install the local db " + e.getMessage());
        }
    }

    public static ModelSql getInstance(){
        if(modelSql == null)
            modelSql = new ModelSql();

        return modelSql;
    }

    public void addTremp(Tremp tremp, boolean isCreated)
    {
        try {
            TrempSql.addTremp(trempHelper.getWritableDatabase(), tremp, isCreated);
        }
        catch (Exception e)
        {
            Log.d("exception:" , "exception while trying add tremp to local db " + e.getMessage());
        }
    }

    public void updateTremp(String id, String dest, String source, String phone, String date){
        TrempSql.UpdateTremp(trempHelper.getWritableDatabase(), id, dest, source, phone, date);
    }


    public Tremp getTrempById(String id){
        return TrempSql.getTrempById(trempHelper.getReadableDatabase(), id);
    }

    public List<Tremp> getAllTremps(boolean isCreated) {
        return TrempSql.GetAllTremps(trempHelper.getReadableDatabase(), isCreated);
    }

    public void deleteTremp(String id){
        TrempSql.DeleteTremp(trempHelper.getWritableDatabase(), id);
    }


    class TrempHelper extends SQLiteOpenHelper{


        public TrempHelper(Context context) {
            super(context, "database.db", null, version);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            TrempSql.create(sqLiteDatabase);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            TrempSql.dropTable(sqLiteDatabase);
            onCreate(sqLiteDatabase);
        }
    }

    class UserHelper extends SQLiteOpenHelper{


        public UserHelper(Context context) {
            super(context, "database.db", null, version);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            UserSql.create(sqLiteDatabase);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            UserSql.dropTable(sqLiteDatabase);
            onCreate(sqLiteDatabase);
        }
    }
}