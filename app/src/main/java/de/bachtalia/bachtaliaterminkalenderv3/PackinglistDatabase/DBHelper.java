package de.bachtalia.bachtaliaterminkalenderv3.PackinglistDatabase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.support.annotation.Nullable;

/*
 * Created by Manuel Lanzinger on 21. Dezember 2018.
 * For the project: BachtaliaTerminkalenderV3.
 */

public class DBHelper extends SQLiteOpenHelper {

    //Database Version and Name of the Database
    public static final int DATABASE_VERSION = 3;
    public static final String DATABASE_NAME = "Packinglist.db";

    // Naming the Table and the Collumns
    public static final String TABLE_PACKINGLIST = "packinglist";
    public static final String COL_ITEM = "item";
    public static final String COL_CHECKED = "checked";

    // Writing the statement for creating a new Table
    private static final String SQL_CREATE_ENTRIES = "CREATE TABLE " + TABLE_PACKINGLIST + " ("
            + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COL_ITEM + " TEXT, "
            + COL_CHECKED + " INTEGER)";

    // Writing the statement for deleting a table
    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + TABLE_PACKINGLIST;

    // Constructor for the DBHelper
    public DBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /*
     * Method for the creation of a Database
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    /*
     * Method for updating the database:
     * First delete the old one, than creating a new one
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);

        onCreate(db);
    }
}
