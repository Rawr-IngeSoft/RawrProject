package com.example.david.rawr.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.util.Log;
import android.util.Pair;

import com.example.david.rawr.Models.Friend;
import com.example.david.rawr.Models.FriendRequest;
import com.example.david.rawr.Models.Message;
import com.example.david.rawr.Models.Pet;

import java.util.ArrayList;

/**
 * Created by david on 10/05/2015.
 */
public class SQLiteHelper extends SQLiteOpenHelper {


    String sqlCreatePET = "CREATE TABLE Pet (petName TEXT, petUsername TEXT PRIMARY KEY, petType TEXT, petGender TEXT, selected TEXT,path TEXT)";
    String sqlCreateFriend ="CREATE TABLE Friend (petName TEXT, petUsername TEXT PRIMARY KEY, profilePicture TEXT)";
    String sqlCreateMessagesHistory = "CREATE TABLE Message (message TEXT, sender TEXT, receiver TEXT, status TEXT, date TEXT, pictureUri TEXT)";
    String sqlCreateFriendRequests = "CREATE TABLE FriendRequest (petUsername TEXT, petType TEXT, petRace TEXT, petGender TEXT, petBirthday TEXT, ownerName TEXT, ownerLastname TEXT, petName TEXT, ownerUsername TEXT, petPicture TEXT, ownerPicture TEXT)";
    String sqlCreateSearchedFriend = "CREATE TABLE SearchedFriend (username TEXT PRIMARY KEY, path TEXT)";

    public SQLiteHelper(Context context) {
        super(context, "PetDB", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(sqlCreatePET);
        db.execSQL(sqlCreateFriend);
        db.execSQL(sqlCreateMessagesHistory);
        db.execSQL(sqlCreateFriendRequests);
        db.execSQL(sqlCreateSearchedFriend);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public ArrayList<Message> getMessagesOf(String friend){
        String query = "select * from Message where sender = '" + friend + "' or " + "receiver = '" + friend + "'" ;
        ArrayList<Message> messages = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Message message;
        if(cursor.moveToFirst()){
            do{
                message = new Message(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5));
                messages.add(messages.size(),message);
            }while (cursor.moveToNext());
        }
        db.close();
        return messages;
    }

    public ArrayList<Message> getMyMessages(String petUsername){
        String query = "select * from Message where sender = '" + petUsername + "' and " + "receiver = '" + petUsername + "'" ;
        ArrayList<Message> messages = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Message message;
        if(cursor.moveToFirst()){
            do{
                message = new Message(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5));
                messages.add(messages.size(),message);
            }while (cursor.moveToNext());
        }
        db.close();
        return messages;
    }

    public String getFriendPhotoPath(String sender){
        String path = "null";
        String query = "select profilePicture from Friend where petUsername = '" + sender + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if(cursor.moveToFirst()){
            do{
                path = cursor.getString(0);
            }while (cursor.moveToNext());
        }
        return path;
    }

    public int getUnreadMessagesOf(String petUsername){
        String query = "select * from Message where sender = '" + petUsername + "' and status = 'unread'" ;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        int cantity = 0;
        if(cursor.moveToFirst()){
            do{
                cantity++;
            }while (cursor.moveToNext());
        }
        db.close();
        return cantity;
    }
    public void addMessage(Message message){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("message", message.getMessage());
        values.put("sender", message.getSender());
        values.put("receiver", message.getReceiver());
        values.put("status", message.getStatus());
        values.put("date", message.getDate());
        db.insert("Message", null,values );
        db.close();
    }

    public void addFriendRequest(FriendRequest friendRequest){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("petName", friendRequest.getPetName());
        values.put("petType", friendRequest.getPetType());
        values.put("petBirthday", friendRequest.getPetBirthday());
        values.put("petPicture", friendRequest.getPetPicture());
        values.put("petUsername", friendRequest.getPetUsername());
        values.put("petRace", friendRequest.getPetRace());
        values.put("petGender", friendRequest.getPetGender());
        values.put("ownerName", friendRequest.getOwnerName());
        values.put("ownerLastname", friendRequest.getOwnerLastName());
        values.put("ownerPicture", friendRequest.getOwnerPicture());
        values.put("ownerUsername", friendRequest.getOwnerUsername());
        db.insert("FriendRequest", null, values);
        db.close();
    }

    public ArrayList<FriendRequest> getFriendRequests(){
        String query = "select * from FriendRequest";
        ArrayList<FriendRequest> friendRequests = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        FriendRequest friendRequest;
        if(cursor.moveToFirst()){
            do{
                friendRequest = new FriendRequest(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6),cursor.getString(7),cursor.getString(8),cursor.getString(9),cursor.getString(10));
                friendRequests.add(friendRequest);
            }while (cursor.moveToNext());
        }
        db.close();
        return friendRequests;
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

    public void addSearchedFriend(String username, String path){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username",username);
        values.put("path",path);
        db.insert("SearchedFriend",null,values);
        db.close();
    }
    public void addFriend(Friend friend){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("petName",friend.getPetName());
        values.put("petUsername",friend.getPetUsername());
        values.put("profilePicture",friend.getProfilePicture());
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

    public ArrayList<Pair<String,String>> getSearchedFriends(){
        String query = "select * from SearchedFriend";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        ArrayList<Pair<String,String>> searchedFriendList = new ArrayList<>();
        if(cursor.moveToFirst()){
            do{
                searchedFriendList.add(new Pair<>(cursor.getString(0), cursor.getString(1)));
            }while (cursor.moveToNext());
        }
        return searchedFriendList;
    }
    public Pet getPet(String petUsername){
        String query = "select * from Pet where petUsername="+"'"+petUsername+"'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Pet pet = null;
        if(cursor.moveToFirst()){
            do{
                pet = new Pet();
                pet.setPetName(cursor.getString(0));
                pet.setIdPet(cursor.getString(1));
                pet.setPetType(cursor.getString(2));
                pet.setPetGender(cursor.getString(3));
                pet.setSelected(cursor.getString(4));
                pet.setPetPictureUri(cursor.getString(5));
            }while (cursor.moveToNext());
        }
        return pet;
    }

    public void answerRequest(String sender){
        // Update db
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("FriendRequest", "petUsername=" + "'" + sender + "'",null);
    }
    public void selectPet(String petUsername, SharedPreferences sharedPreferences){

        // Update db
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("selected","'true'");
        db.update("Pet", contentValues, "petUsername=" + "'" + petUsername + "'", null);
        contentValues.put("selected", "'false'");
        db.update("Pet", contentValues, "petUsername<>" + "'" + petUsername + "'", null);

        // Getting the pet selected
        Pet pet = getPet(petUsername);
        // Update shared preferences
        if(pet != null) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("petUsername", petUsername);
            editor.putString("petName", pet.getPetName());
            editor.putString("petType",pet.getPetType());
            editor.putString("petGender",pet.getPetGender());
            editor.putString("petPicture", pet.getPetPictureUri());
            editor.commit();
        }
    }


    public void clearDB(){
        String query;
        SQLiteDatabase db = this.getWritableDatabase();
        query = "delete from Pet";
        db.execSQL(query);
        query = "delete from Friend";
        db.execSQL(query);
        query = "delete from FriendRequest";
        db.execSQL(query);
        query = "delete from Message";
        db.execSQL(query);
        query = "delete from SearchedFriend";
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

    public void clearFriendRequests(){
        String query;
        SQLiteDatabase db = this.getWritableDatabase();
        query = "delete from FriendRequest";
        db.execSQL(query);
        db.close();
    }

    public void clearSearchedFriends(){
        String query;
        SQLiteDatabase db = this.getWritableDatabase();
        query = "delete from SearchedFriend";
        db.execSQL(query);
        db.close();
    }
}
