package com.example.ssalu.cashcollect;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class DatabaseHelper extends SQLiteOpenHelper {

    //Database helper DB is handling db stuff
    //Variables
    private static final String DATABASE_NAME = "artgallery1.db";
    private static final String TAG = "DatabaseHelper";
    private static final String TABLE_NAME = "user";
    private static final String COL1 = "id";
    private static final String COL2 = "email";
    private static final String COL3 = "name";
    private static final String COL4 = "password";

    //Constructor
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        SQLiteDatabase db = this.getWritableDatabase();
  }


  //Creating user table
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" + COL2 + " TEXT PRIMARY KEY, " +
                COL3 + " TEXT, " + COL4 + " TEXT)";
        db.execSQL(createTable);
    }

    //Is required
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            String dropTable = "DROP TABLE IF EXISTS " + TABLE_NAME;
            db.execSQL(dropTable);
            onCreate(db);
    }

    //Add data - email is PK
    public boolean addData(String email, String name, String password){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cval = new ContentValues();
        cval.put(COL2, email);
        cval.put(COL3, name);
        cval.put(COL4, password);

        long result = db.insert(TABLE_NAME, null, cval);

        if(result == -1){
            return false;
        }else{
            return true;
        }
    }

    //Get all data from user table
    public Cursor getData(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        return db.rawQuery(query, null);
    }

    //Checks if email address is already in db
    public boolean isPresent(String table, String column, String value) {
        SQLiteDatabase db = this.getWritableDatabase();
        String Query = "SELECT * FROM " + table + " WHERE " + column + " = '" + value + "';";
        Cursor cursor = db.rawQuery(Query, null);
        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    //Login validations
    public boolean validate(String email, String pw){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COL2 + " = '" + email + "' AND " +
                COL4 + " = '" + pw + "'";
        Cursor cursor = db.rawQuery(query,null);

        return cursor.getCount() > 0 ? true : false;

    }
}
