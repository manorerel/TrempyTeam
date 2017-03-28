package com.example.hadar.trempyteam.Model;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by מנור on 27/03/2017.
 */

public class UserSql {

    public static void create(SQLiteDatabase sqLiteDatabase) {
        // sqLiteDatabase.execSQL("CREATE TABLE " + STUDENT + " (" + ST_ID + " TEXT, " + NAME + " TEXT, " + IMAGE_URL + " TEXT)");
    }

    public static void dropTable(SQLiteDatabase sqLiteDatabase) {
        //sqLiteDatabase.execSQL("DROP TABLE " + STUDENT);
    }
}
