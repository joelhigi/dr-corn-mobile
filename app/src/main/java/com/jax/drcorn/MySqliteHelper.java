package com.jax.drcorn;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class MySqliteHelper extends SQLiteOpenHelper{
    private Context context;
    private static final String DATABASE_NAME = "CornDB.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "records";
    private static final String COLUMN_ID = "record_id";
    private static final String COLUMN_NAME = "disease_name";
    private static final String COLUMN_DATE = "record_date";
    private static final String COLUMN_PRED = "pred_value";

    MySqliteHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE "+TABLE_NAME+ " ("+COLUMN_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+ COLUMN_NAME+" TEXT, "+ COLUMN_PRED+" TEXT, "+ COLUMN_DATE +" TEXT)";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);

    }

    void addRecord(String name, String date, String pred){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();


        cv.put(COLUMN_NAME, name);
        cv.put(COLUMN_DATE, date);
        cv.put(COLUMN_PRED, pred);


        long result = db.insert(TABLE_NAME, null, cv);
        if(result == -1){
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "Added Successfully!", Toast.LENGTH_SHORT).show();
        }




    }

    Cursor readAllData(){
        String query = "SELECT * FROM "+TABLE_NAME+" ORDER BY "+COLUMN_ID+" DESC";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }


    void deleteOneRow(String row_id){
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_NAME, "record_id=?", new String[]{row_id});
        if(result == -1){
            Toast.makeText(context, "Delete Failed", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "Delete Successful", Toast.LENGTH_SHORT).show();
        }
    }

    void deleteAllRows(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM "+TABLE_NAME);
    }
}
