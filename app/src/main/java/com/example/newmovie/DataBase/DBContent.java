package com.example.newmovie.DataBase;


public class DBContent {

        public static final String TABLE_NAME = "Movie";
        public static final String ID_COLUMN = "ID";
        public static final String POSTER_PATH_COLUMN = "Poster";
        public static final String OVERVIEW_COLUMN = "Overview";
        public static final String MOVIE_ID_COLUMN = "Movie_ID";
        public static final String TITLE_COLUMN = "Movie_Title";
        public static final String VOTE_AVERAGE_COLUMN = "Vote";
        public static final String RELEASE_DATE_COLUMN = "Release_Year";
        public static final String CREATE_MOVIE_TABLE =
                "CREATE TABLE " + TABLE_NAME + "("
                        + ID_COLUMN + " INTEGER PRIMARYKEY AUTOINCREAMENT, "
                        + POSTER_PATH_COLUMN + " TEXT, "
                        + OVERVIEW_COLUMN + " TEXT, "
                        + MOVIE_ID_COLUMN + " TEXT, "
                        + TITLE_COLUMN + " TEXT, "
                        + VOTE_AVERAGE_COLUMN + " INT, "
                        + RELEASE_DATE_COLUMN + " TEXT"
                        + ")";
    }

