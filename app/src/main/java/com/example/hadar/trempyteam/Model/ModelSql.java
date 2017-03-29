package com.example.hadar.trempyteam.Model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.hadar.trempyteam.MainAactivity;
import com.example.hadar.trempyteam.TrempyApp;

/**
 * Created by מנור on 27/03/2017.
 */

public class ModelSql {
    private SQLiteOpenHelper trempHelper;
    private SQLiteOpenHelper userHelper;
    private int version = 2;

    public ModelSql(){
        trempHelper = new TrempHelper(TrempyApp.getAppContext());
        userHelper = new UserHelper(TrempyApp.getAppContext());
    }

    public void addTremp(Tremp tremp)
    {
        TrempSql.addTremp(trempHelper.getWritableDatabase(),tremp);
    }

    public Tremp getTrempById(String id){
        return TrempSql.getTrempById(trempHelper.getReadableDatabase(), id);
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