package com.example.david.rawr.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.david.rawr.Models.Pet;

import java.util.ArrayList;

/**
 * Created by david on 10/05/2015.
 */
public class PetSQLiteHelper extends SQLiteOpenHelper {


    String sqlCreate = "CREATE TABLE Pet (petName TEXT, petUsername TEXT PRIMARY KEY, petType TEXT)";

    public PetSQLiteHelper(Context context) {
        super(context, "PetDB", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(sqlCreate);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void addPet(Pet pet){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("petName",pet.getPetName());
        values.put("petUsername",pet.getIdPet());
        values.put("petType", pet.getPetType());
        db.insert("Pet",null,values);
        db.close();
    }

    public ArrayList<Pet> getPets(){
        String query = "select * from Pet";
        ArrayList<Pet> pets = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Pet pet;
        if(cursor.moveToFirst()){
            do{
                pet = new Pet();
                pet.setPetName(cursor.getString(0));
                pet.setIdPet(cursor.getString(1));
                pet.setPetType(cursor.getString(2));
                pets.add(pet);
            }while (cursor.moveToNext());
        }
        db.close();
        return pets;
    }

    public void clearDB(){
        String query = "delete from Pet";
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(query);
        db.close();
    }
}
