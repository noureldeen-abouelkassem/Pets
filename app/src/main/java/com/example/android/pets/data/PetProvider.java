package com.example.android.pets.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import static com.example.android.pets.data.PetContract.CONTENT_AUTHORITY;
import static com.example.android.pets.data.PetContract.PATH_PETS;

/**
 * Created by noureldeen on 8/12/2017.
 */

public class PetProvider extends ContentProvider {
    private static final String LOG_TAG = PetProvider.class.getName();
    public PetDbHelper petDbHelper;
    UriMatcher uriMatcher;
    public static final int PETS = 100, PETS_ID = 101;

    public boolean onCreate() {
        petDbHelper = new PetDbHelper(getContext());
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(CONTENT_AUTHORITY, PATH_PETS, PETS);
        uriMatcher.addURI(CONTENT_AUTHORITY, PATH_PETS + "/#", PETS_ID);
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {

        SQLiteDatabase sqLiteDatabase = petDbHelper.getReadableDatabase();
        Cursor cursor = null;
        int matcher = uriMatcher.match(uri);
        switch (matcher) {
            case PETS:
                cursor = sqLiteDatabase.query(PetContract.PetEntry.TABLE_NAME, strings, null, null, null, null, null);
                break;
            case PETS_ID:
                s = PetContract.PetEntry._ID + "=?";
                strings1 = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = sqLiteDatabase.query(PetContract.PetEntry.TABLE_NAME, strings, s, strings1, null, null, null);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = uriMatcher.match(uri);
        switch (match) {
            case PETS:
                return PetContract.PetEntry.CONTENT_LIST_TYPE;
            case PETS_ID:
                return PetContract.PetEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final int matcher = uriMatcher.match(uri);
        switch (matcher) {
            case PETS:
                return insertPet(uri, contentValues);
            default:
                throw new IllegalArgumentException("inavlid uri " + uri);
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        SQLiteDatabase db = petDbHelper.getWritableDatabase();
        getContext().getContentResolver().notifyChange(uri, null);
        int noOfRows = db.delete(PetContract.PetEntry.TABLE_NAME, s, strings);
        return noOfRows;

    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        final int match = uriMatcher.match(uri);
        switch (match) {
            case PETS:
                return updatePet(uri, contentValues, s, strings);
            case PETS_ID:
                return updatePet(uri, contentValues, s, strings);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    private Uri insertPet(Uri uri, ContentValues contentValues) {
        SQLiteDatabase db = petDbHelper.getWritableDatabase();
        String name = contentValues.getAsString(PetContract.PetEntry.COLUMN_PET_NAME);
        String breed = contentValues.getAsString(PetContract.PetEntry.COLUMN_PET_BREED);
        Integer gender = contentValues.getAsInteger(PetContract.PetEntry.COLUMN_PET_GENDER);
        Integer weight = contentValues.getAsInteger(PetContract.PetEntry.COLUMN_PET_WEIGHT);
        if (name == null) {
            throw new IllegalArgumentException("Pet requires a name");
        }
        if (breed == null) {
            throw new IllegalArgumentException("Pet requires a breed");
        }
        if (gender == null || !PetContract.PetEntry.isValidGender(gender)) {
            throw new IllegalArgumentException("Pet requires a gender");
        }
        if (weight == null) {
            throw new IllegalArgumentException("Pet requires a weight");
        }
        long _id = 0;
        _id = db.insert(PetContract.PetEntry.TABLE_NAME, null, contentValues);
        if (_id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, _id);
    }

    private int updatePet(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {

        SQLiteDatabase db = petDbHelper.getWritableDatabase();
        String name = contentValues.getAsString(PetContract.PetEntry.COLUMN_PET_NAME);
        String breed = contentValues.getAsString(PetContract.PetEntry.COLUMN_PET_BREED);
        Integer gender = contentValues.getAsInteger(PetContract.PetEntry.COLUMN_PET_GENDER);
        Integer weight = contentValues.getAsInteger(PetContract.PetEntry.COLUMN_PET_WEIGHT);
        if (name == null) {
            throw new IllegalArgumentException("Pet requires a name");
        }
        if (breed == null) {
            throw new IllegalArgumentException("Pet requires a breed");
        }
        if (gender == null || !PetContract.PetEntry.isValidGender(gender)) {
            throw new IllegalArgumentException("Pet requires a gender");
        }
        if (weight == null) {
            throw new IllegalArgumentException("Pet requires a weight");

        }
        int numberOfRows = db.update(PetContract.PetEntry.TABLE_NAME, contentValues, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return numberOfRows;
    }
}
