package com.example.quizforkids;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {
    public static final String DBNAME = "Login.db";
    public DBHelper(Context context) {
        super(context, DBNAME, null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase MyDB) {
        MyDB.execSQL("create Table users(username TEXT primary key, email TEXT, password TEXT, point INTEGER)");
        MyDB.execSQL("create Table attempt(id INTEGER primary key, username TEXT, category TEXT, time TEXT, point INTEGER)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase MyDB, int oldVersion, int newVersion) {
        MyDB.execSQL("DROP TABLE IF EXISTS users");
        MyDB.execSQL("DROP TABLE IF EXISTS attempt");
        onCreate(MyDB);
    }


    public Boolean insertData(String email, String username, String password){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues= new ContentValues();
        contentValues.put("username", username);
        contentValues.put("email", email);
        contentValues.put("password", password);
        contentValues.put("point", 0);
        long result = MyDB.insert("users", null, contentValues);
        if(result==-1) return false;
        else
            return true;
    }

    public long insertAttempt(String username, String cate, String time, Integer points) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        // Check for null values and handle them appropriately
        if (username != null && cate != null && time != null && points != null) {
            contentValues.put("username", username);
            contentValues.put("category", cate);
            contentValues.put("time", time);
            contentValues.put("point", points);

            return db.insert("attempt", null, contentValues);
        } else {
            // Log an error or handle the case where any parameter is null
            Log.e("DBHelper", "One or more parameters are null in insertAttempt");
            return -1; // You may want to return a specific value indicating an error
        }
    }

    public Boolean checkusername(String username) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from users where username = ?", new String[]{username});
        if (cursor.getCount() > 0)
            return true;
        else
            return false;
    }

    public Boolean checkemail(String email) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from users where email = ?", new String[]{email});
        if (cursor.getCount() > 0)
            return true;
        else
            return false;
    }




    public Boolean checkusernamepassword(String username, String password){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from users where username = ? and password = ?", new String[] {username,password});
        if(cursor.getCount()>0)
            return true;
        else
            return false;
    }

    public boolean updatePassword(String oldPassword, String newPassword) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("password", newPassword);

        int updatedRows = MyDB.update("users", contentValues, "password = ?", new String[]{oldPassword});
        return updatedRows > 0;
    }

    public boolean updateTotalPoints(String username, int newPoints) {
        SQLiteDatabase MyDB = this.getWritableDatabase();

        // Retrieve the existing total points for the user
        int existing = getOverallPoints(username);

        // Add the new points to the existing total points
        int total = existing + newPoints;

        ContentValues contentValues = new ContentValues();
        contentValues.put("point", total); // Update the TOTAL_POINTS column with the new total points
        int affectedRows = MyDB.update("users", contentValues, "username = ?", new String[]{username});
        return affectedRows > 0;
    }



    public int getOverallPoints(String username) {
        SQLiteDatabase MyDB = this.getReadableDatabase();
        int overallPoints = 0;

        if (username != null) {
            Cursor cursor = MyDB.rawQuery("SELECT point FROM users WHERE username = ?", new String[]{username});
            if (cursor != null && cursor.moveToFirst()) {
                overallPoints = cursor.getInt(cursor.getColumnIndex("point"));
                cursor.close();
            }
        } else {
            // Handle the case where username is null
            // For example, you might return a default value or throw an exception
        }

        return overallPoints;
    }


    public Cursor getAttemptsByUsername(String username, String sortOrder) {
        SQLiteDatabase MyDB = this.getReadableDatabase();
        return MyDB.query("attempt", null, "username =?", new String[]{username}, null, null, sortOrder);
    }

}