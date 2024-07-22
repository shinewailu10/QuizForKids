package com.example.quizforkids;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.annotation.Nullable;

import java.io.ByteArrayOutputStream;

public class AnimalDBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "animalQuiz.db";
    public static final String TABLE_NAME = "animals_quiz";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "IMAGE";
    public static final String COL_3 = "ANSWER";

    private Context mContext;

    public AnimalDBHelper(@Nullable Context context, @Nullable String name,
                          @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.mContext = context;
        addDefaultData();
    }

    //creating the table
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME +
                "(ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "IMAGE TEXT, ANSWER TEXT)");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public Cursor viewAllRecords() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        return res;
    }


    public long insertAnimalAnswer(String image, String answer) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, image);
        contentValues.put(COL_3, answer);
        return db.insert(TABLE_NAME, null, contentValues);
    }

    public Cursor viewRecord(int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        //Cursor res = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE ID=" +id, null);

        String[] projection = new String[]{COL_1, COL_2, COL_3};
        String selection = COL_1 + " = ?";
        String[] selectionArgs = new String[]{String.valueOf(id)};
        String sortOrder = COL_1 + " DESC";
        Cursor res = db.query(
                TABLE_NAME, // The table to query
                projection, // The array of columns to return (pass null to get all)
                selection, // The columns for the WHERE clause
                selectionArgs, // The values for the WHERE clause
                null, // don't group the rows
                null, // don't filter by row groups
                sortOrder // The sort order (or null in no need to sort)
        );
        return res;
    }

    private void addDefaultData() {
        SQLiteDatabase db = this.getWritableDatabase();

        // Add sample data for animal quiz answers and image paths
        insertAnimalAnswer("tiger", "tiger");
        insertAnimalAnswer("panda", "panda");
        insertAnimalAnswer("cat", "cat");
        insertAnimalAnswer("dog", "dog");
        insertAnimalAnswer("mouse", "mouse");
        insertAnimalAnswer("lion", "lion");
        insertAnimalAnswer("bat", "bat");
        insertAnimalAnswer("zebra", "zebra");
        insertAnimalAnswer("fish", "fish");
        insertAnimalAnswer("owl", "owl");
    }

}
