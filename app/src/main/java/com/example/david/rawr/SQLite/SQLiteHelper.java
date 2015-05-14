package com.example.david.rawr.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.david.rawr.Models.Friend;
import com.example.david.rawr.Models.Pet;

import java.util.ArrayList;

/**
 * Created by david on 10/05/2015.
 */
public class SQLiteHelper extends SQLiteOpenHelper {


    String sqlCreatePET = "CREATE TABLE Pet (petName TEXT, petUsername TEXT PRIMARY KEY, petType TEXT, petGender TEXT, selected TEXT,path TEXT)";
    String sqlCreateFriend ="CREATE TABLE Friend (petName TEXT, petUsername TEXT PRIMARY KEY)";
    public SQLiteHelper(Context context) {
        super(context, "PetDB", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(sqlCreatePET);
        db.execSQL(sqlCreateFriend);
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
        values.put("petGender", pet.getPetGender());
        values.put("selected", "false");
        values.put("path",pet.getPetPictureUri());
        db.insert("Pet",null,values);
        db.close();
    }

    public void addFriend(Friend friend){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("petName",friend.getPetName());
        values.put("petUsername",friend.getPetUsername());
        db.insert("Friend",null,values);
        db.close();
    }

    public ArrayList<Friend> getFriends(){
        String query = "select * from Friend";
        ArrayList<Friend> friends = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Friend friend;
        if(cursor.moveToFirst()){
            do{
                friend = new Friend();
                friend.setPetName(cursor.getString(0));
                friend.setPetUsername(cursor.getString(1));
                friends.add(friend);
            }while (cursor.moveToNext());
        }
        db.close();
        return friends;
    }
    public ArrayList<Pet> getPets(){
        String query = "select * from Pet";
        Log.e("getting","pets");
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
                pet.setPetGender(cursor.getString(3));
                pet.setSelected(cursor.getString(4));
                pet.setPetPictureUri(cursor.getString(5));
                pets.add(pet);
            }while (cursor.moveToNext());
        }
        db.close();
        return pets;
    }

    public void selectPet(String petUsername){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("selected","'true'");
        db.update("Pet",contentValues,"petUsername="+"'"+petUsername+"'",null);
        contentValues.put("selected","'false'");
        db.update("Pet",contentValues,"petUsername<>"+"'"+petUsername+"'",null);
    }


    public void clearDB(){
        String query;
        SQLiteDatabase db = this.getWritableDatabase();
        query = "delete from Pet";
        db.execSQL(query);
        query = "delete from Friend";
        db.execSQL(query);
        db.close();
    }

    public void clearFriends(){
        String query;
        SQLiteDatabase db = this.getWritableDatabase();
        query = "delete from Friend";
        db.execSQL(query);
        db.close();
    }
}
