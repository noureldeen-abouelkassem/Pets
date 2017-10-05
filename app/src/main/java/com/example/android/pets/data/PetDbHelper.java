package com.example.android.pets.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by noureldeen on 8/12/2017.
 */

public class PetDbHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "pets.db";

    public PetDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String SQL_CREATE_PETS_TABLE = "CREATE TABLE "
                + PetContract.PetEntry.TABLE_NAME + " (  "
                + PetContract.PetEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT , "
                + PetContract.PetEntry.COLUMN_PET_NAME + " TEXT NOT NULL , "
                + PetContract.PetEntry.COLUMN_PET_BREED + " TEXT , "
                + PetContract.PetEntry.COLUMN_PET_GENDER + " INTEGER NOT NULL , "
                + PetContract.PetEntry.COLUMN_PET_WEIGHT + " INTEGER NOT NULL DEFAULT 0 ); ";
        Log.v(PetDbHelper.class.getName(),SQL_CREATE_PETS_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_PETS_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

    }
}
