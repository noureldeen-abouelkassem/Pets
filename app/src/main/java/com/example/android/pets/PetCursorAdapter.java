package com.example.android.pets;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by noureldeen on 8/14/2017.
 */

public class PetCursorAdapter extends CursorAdapter {

    public PetCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView petName = (TextView) view.findViewById(R.id.petItemName);
        TextView petBreed = (TextView) view.findViewById(R.id.petItemBreed);
        String petNameValue = cursor.getString(cursor.getColumnIndexOrThrow("name"));
        String petBreedValue;
        if (cursor.getString(cursor.getColumnIndexOrThrow("breed")).isEmpty()) {
            petBreedValue = "Unknown Breed !";
        } else {
            petBreedValue = cursor.getString(cursor.getColumnIndexOrThrow("breed"));
        }
        petName.setText(petNameValue);
        petBreed.setText(petBreedValue);
    }
}
