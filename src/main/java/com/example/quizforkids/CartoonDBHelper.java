package com.example.quizforkids;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class CartoonDBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "cartoonsQuiz.db";
    public static final String TABLE_NAME = "cartoonQuiz";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "IMAGE";
    public static final String COL_3 = "ANSWER";

    public static final String COL_4 = "CHOICE1";
    public static final String COL_5 = "CHOICE2";
    private Context mContext;

    public CartoonDBHelper(@Nullable Context context, @Nullable String name,
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
                "IMAGE TEXT, ANSWER TEXT, CHOICE1 TEXT, CHOICE2 TEXT, CHOICE3 TEXT)");
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



    public long insertCartoonData(String image, String answer, String choice1, String choice2) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, image);
        contentValues.put(COL_3, answer);
        contentValues.put(COL_4, choice1);
        contentValues.put(COL_5, choice2);

        return db.insert(TABLE_NAME, null, contentValues);
    }

    public Cursor viewRecord(int id) {
        SQLiteDatabase db = this.getWritableDatabase();


        String[] projection = new String[]{COL_1, COL_2, COL_3, COL_4, COL_5};
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
        insertCartoonData("batman", "BatMan", "BirdMan", "BlackMan");
        insertCartoonData("pikachu", "Pikachu", "Charmandder", "Pichu");
        insertCartoonData("aang", "Aang", "Killua", "Dororo");
        insertCartoonData("tom", "Tom", "Jerry", "Puss in boots");
        insertCartoonData("doraemon", "Doraemon", "Garfield", "BOBO");
        insertCartoonData("goku", "Goku", "Doku", "Deku");
        insertCartoonData("luffy", "Luffy", "Zoro", "Saitama");
        insertCartoonData("naruto", "Naruto", "Ichigo", "Boruto");
        insertCartoonData("scoobydoo", "Scooby-Doo", "Droopy", "Bolt");
        insertCartoonData("thorfinn", "Thorfinn", "Thor", "Thorkell");


    }

}
