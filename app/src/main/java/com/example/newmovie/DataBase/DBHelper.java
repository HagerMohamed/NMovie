package com.example.newmovie.DataBase;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.newmovie.Details.Movie;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Movies";
    private static final int VERSION = 1 ;


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DBContent.CREATE_MOVIE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF  IT EXISTS " + DBContent.TABLE_NAME);
        onCreate(db);
    }

    public long add_Item (Movie movie) {

        SQLiteDatabase sqDb = getWritableDatabase();
        ContentValues value = new ContentValues();
        value.put(DBContent.POSTER_PATH_COLUMN, movie.getPoster_path());
        value.put(DBContent.OVERVIEW_COLUMN,movie.getOverview());
        value.put(DBContent.ID_COLUMN,movie.getId());
        value.put(DBContent.TITLE_COLUMN,movie.getOriginal_title());
        value.put(DBContent.VOTE_AVERAGE_COLUMN,movie.getVote_average());
        value.put(DBContent.RELEASE_DATE_COLUMN, movie.getRelease_date());
        long id =  sqDb.insert(DBContent.TABLE_NAME,null,value);
        return id;
    }

    public ArrayList <Movie>  getItem (){
        SQLiteDatabase sqDb = getReadableDatabase();
        ArrayList<Movie> arrMovie = new ArrayList<Movie>();
        Cursor cursor = sqDb.query(DBContent.TABLE_NAME, null, null, null, null, null, null);
        ArrayList<String> poster = new ArrayList<String>();

        if (cursor != null){

            while (cursor.moveToNext()){

                Movie m = new Movie();
                m.setPoster_path (cursor.getString(cursor.getColumnIndex(DBContent.POSTER_PATH_COLUMN)));
                m.setOverview(cursor.getString(cursor.getColumnIndex(DBContent.OVERVIEW_COLUMN)));
                m.setId(cursor.getInt(cursor.getColumnIndex(DBContent.ID_COLUMN)));
                m.setOriginal_title(cursor.getString(cursor.getColumnIndex(DBContent.TITLE_COLUMN)));
                m.setVote_average(Double.parseDouble(cursor.getString(cursor.getColumnIndex(DBContent.VOTE_AVERAGE_COLUMN))));
                m.setRelease_date(cursor.getString(cursor.getColumnIndex(DBContent.RELEASE_DATE_COLUMN)));
                poster.add(cursor.getString((cursor.getColumnIndex(DBContent.POSTER_PATH_COLUMN))));
                arrMovie.add(m);

            }
        }

        return arrMovie;
    }

    public long deleteItem (String id){
        SQLiteDatabase sqDb = getWritableDatabase();
        long deleteRow = sqDb.delete(DBContent.TABLE_NAME, DBContent.ID_COLUMN + " =?", new String[]{id});
        return deleteRow;
    }

    public void delet(){
        SQLiteDatabase sqDb = getWritableDatabase();
        sqDb.delete(DBContent.TABLE_NAME, null, null);
    }
}
