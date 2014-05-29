package com.recipes.app;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
public class DBHelper extends SQLiteOpenHelper{
	 
	// path of database when app installed on device
	// if you change your package name, make sure to also change this package name
    private static String DB_PATH = "/data/data/com.recipes.app/databases/";
 
    // create database name and version
    private final static String DB_NAME = "db_recipes";
	public final static int DB_VERSION = 1;
    public static SQLiteDatabase db; 
 
    private final Context context;
    
    // create table name and fields
	private final String TABLE_NAME = "tbl_recipes";
	private final String ID = "id";
	private final String RECIPE_NAME = "recipe_name";
	private final String IMAGE_PREVIEW = "image_preview";
	private final String PREP_TIME = "prepare_time";
	private final String COOK_TIME = "cook_time";
	private final String SERVES = "serves";
	private final String SUMMARY = "summary";
	private final String INGREDIENTS = "ingredients";
	private final String DIRECTIONS = "directions";
	
	
    public DBHelper(Context context) {
    	super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }	
 
    // method to create database
    public void createDataBase() throws IOException{
 
    	boolean dbExist = checkDataBase();
    	SQLiteDatabase db_Read = null;

    	// if database exist delete database and copy the new one
    	if(dbExist){
    		deleteDataBase();
    		try {
    			copyDataBase();
    		} catch (IOException e) {
        		throw new Error("Error copying database");
        	}
    	}else{
    		db_Read = this.getReadableDatabase();
    		db_Read.close();
 
        	try {
    			copyDataBase();
    		} catch (IOException e) {
        		throw new Error("Error copying database");
        	}
    	}
 
    }
 
    // method to delete database
    private void deleteDataBase(){
    	File dbFile = new File(DB_PATH + DB_NAME);
    	dbFile.delete();
    }
   
    // method to check database on path
    private boolean checkDataBase(){
    	File dbFile = new File(DB_PATH + DB_NAME);
    	return dbFile.exists();
    }
 
    // method to copy database from app to db path
    private void copyDataBase() throws IOException{
    	
    	InputStream myInput = context.getAssets().open(DB_NAME);
 
    	String outFileName = DB_PATH + DB_NAME;
 
    	OutputStream myOutput = new FileOutputStream(outFileName);
    	
    	byte[] buffer = new byte[1024];
    	int length;
    	while ((length = myInput.read(buffer))>0){
    		myOutput.write(buffer, 0, length);
    	}
 
    	myOutput.flush();
    	myOutput.close();
    	myInput.close();
 
    }
 
    // method to open database and read it
    public void openDataBase() throws SQLException{
    	String myPath = DB_PATH + DB_NAME;
    	db = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
    }
 
    // close database after it is used
    @Override
	public void close() {
    	db.close();
	}
 
	@Override
	public void onCreate(SQLiteDatabase db) {}
 
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}
	
	
	// method to get all data from database based on keyword
 	public ArrayList<ArrayList<Object>> getAllData(){
		ArrayList<ArrayList<Object>>  dataArrays= new ArrayList<ArrayList<Object>>();
 
		Cursor cursor = null;
 
		try{
			cursor = db.query(
					TABLE_NAME,
					new String[]{ID, RECIPE_NAME, IMAGE_PREVIEW, PREP_TIME, COOK_TIME},
					null, null, null, null, null);
			cursor.moveToFirst();
	
			if (!cursor.isAfterLast()){
				do{
					ArrayList<Object> dataList = new ArrayList<Object>();
					
					dataList.add(cursor.getLong(0));
					dataList.add(cursor.getString(1));
					dataList.add(cursor.getString(2));
					dataList.add(cursor.getString(3));
					dataList.add(cursor.getString(4));
	 
					dataArrays.add(dataList);
				}
				
				while (cursor.moveToNext());
			}
			cursor.close();
		}catch (SQLException e){
			Log.e("DB Error", e.toString());
			e.printStackTrace();
		}
		
		return dataArrays;
	}
 	
 // method to get all data from database based on keyword
  	public ArrayList<ArrayList<Object>> getDataByName(String recipeNameKeyword){
 		ArrayList<ArrayList<Object>>  dataArrays= new ArrayList<ArrayList<Object>>();
  
 		Cursor cursor = null;
  
 		try{
 			cursor = db.query(
 					TABLE_NAME,
 					new String[]{ID, RECIPE_NAME, IMAGE_PREVIEW, PREP_TIME, COOK_TIME},
 					RECIPE_NAME +" LIKE '%"+recipeNameKeyword+"%'",
 					null, null, null, null);
 			cursor.moveToFirst();
 	
 			if (!cursor.isAfterLast()){
 				do{
 					ArrayList<Object> dataList = new ArrayList<Object>();
 	
 					dataList.add(cursor.getLong(0));
 					dataList.add(cursor.getString(1));
 					dataList.add(cursor.getString(2));
 					dataList.add(cursor.getString(3));
 					dataList.add(cursor.getString(4));
 	 
 					dataArrays.add(dataList);
 				}
 				
 				while (cursor.moveToNext());
 			}
 			cursor.close();
 		}catch (SQLException e){
 			Log.e("DB Error", e.toString());
 			e.printStackTrace();
 		}
 		
 		return dataArrays;
 	}
	
 	// get recipe data based on the id
 	public ArrayList<Object> getDetail(String id){
		
		ArrayList<Object> rowArray = new ArrayList<Object>();
		Cursor cursor;
 
		try{
			cursor = db.query(
					TABLE_NAME,
					new String[] {RECIPE_NAME, IMAGE_PREVIEW, PREP_TIME, COOK_TIME, SERVES, SUMMARY, INGREDIENTS, DIRECTIONS},
					ID + "=" + id,
					null, null, null, null, null);
 
			cursor.moveToFirst();
 
			if (!cursor.isAfterLast()){
				do{
					rowArray.add(cursor.getString(0));
					rowArray.add(cursor.getString(1));
					rowArray.add(cursor.getString(2));
					rowArray.add(cursor.getString(3));
					rowArray.add(cursor.getString(4));
					rowArray.add(cursor.getString(5));
					rowArray.add(cursor.getString(6));
					rowArray.add(cursor.getString(7));
				}
				while (cursor.moveToNext());
			}
 
			cursor.close();
		}
		catch (SQLException e) 
		{
			Log.e("DB ERROR", e.toString());
			e.printStackTrace();
		}
 
		return rowArray;
	}
}