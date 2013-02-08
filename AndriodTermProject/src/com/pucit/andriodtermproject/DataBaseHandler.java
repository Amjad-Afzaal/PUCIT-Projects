/**
 * 
 */
package com.pucit.andriodtermproject;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * @author Amjad Afzaal
 *
 */
public class DataBaseHandler extends SQLiteOpenHelper {

	/**
	 * @param context
	 * @param name
	 * @param factory
	 * @param version
	 */
	private static final int DATABASE_VERSION = 2;
	private static final String DATABASE_NAME = "SCORE_DATABASE";
	private static final String TABLE_NAME = "SCORES_TABLE";
	private static final String ID_COLUMN = "ID";
	private static final String NAME_COLUMN = "NAME";
	private static final String SCORE_COLUMN = "SCORE";
	
	public DataBaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite.SQLiteDatabase)
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		Log.i("DataBaseHandler---->","----->Insert Successfully");
		String CREATE_SCORES_TABLE = "CREATE TABLE " + TABLE_NAME + "(" + ID_COLUMN + " INTEGER PRIMARY KEY, " + NAME_COLUMN + " TEXT, "
				+ SCORE_COLUMN + " INTEGER)";
		db.execSQL(CREATE_SCORES_TABLE);
	}

	/* (non-Javadoc)
	 * @see android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite.SQLiteDatabase, int, int)
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		Log.i("DataBaseHandler---->","----->Upgrade Successfully");
		onCreate(db);
	}

	public void addTask(String name, long score)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues row = new ContentValues();
		row.put(NAME_COLUMN, name);
		row.put(SCORE_COLUMN, score);
		db.insert(TABLE_NAME, null, row);
		db.close();
	}
	
	public ArrayList<String> getScores()
	{
		ArrayList<String> scores = new ArrayList<String>();
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " ORDER BY " + SCORE_COLUMN + " DESC", null);
		if(cursor.moveToFirst())
		{
			do
			{
				scores.add(cursor.getString(1) + "\t\t\t\t\t" + cursor.getString(2));
				//scores.add(cursor.getString(2));
			}while(cursor.moveToNext());
		}
		db.close();
		return scores;
	}
	
	/**
	 * @return the databaseVersion
	 */
	public static int getDatabaseVersion() {
		return DATABASE_VERSION;
	}

	/**
	 * @return the databaseName
	 */
	public static String getDatabaseName() {
		return DATABASE_NAME;
	}

	/**
	 * @return the tableName
	 */
	public static String getTableName() {
		return TABLE_NAME;
	}

	/**
	 * @return the dateColumn
	 */
	public static String getNameColumn() {
		return NAME_COLUMN;
	}

	/**
	 * @return the timeColumn
	 */
	public static String getScoreColumn() {
		return SCORE_COLUMN;
	}

	/**
	 * @return the idColumn
	 */
	public static String getIdColumn() {
		return ID_COLUMN;
	}

}
