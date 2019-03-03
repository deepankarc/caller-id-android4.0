package com.example.testapp.db;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**     
 * Provides functions for creating and copying a pre-populated database. Also contains functions for querying a database. 
 * */
public class DatabaseHelperSQL extends SQLiteOpenHelper {

	private static String DB_NAME;
	private static String TABLE_NAME;
	private static final int DB_VER = 1;
	
	private SQLiteDatabase myDataBase;
	private final Context mDatabaseContext;
	public static final String DB_PATH = "/data/data/com.example.testapp/databases/";
	
	/** Constructor    
	 * Takes and keeps a reference of the passed context in order to access to the application assets and resources.
	 *  @param context     */
	public DatabaseHelperSQL(Context context, String DB_NAME, String TABLE_NAME)	{
			super(context, DB_NAME, null, DB_VER);
			this.mDatabaseContext = context;
			DatabaseHelperSQL.DB_NAME = DB_NAME;
			DatabaseHelperSQL.TABLE_NAME = TABLE_NAME;
	}
		
	/**     
	 * Creates a empty database on the system and rewrites it with your own database.     
	 * */
	 public void createDataBase() throws IOException	{     	
		 boolean dbExist = checkDataBase();     	
		 
		 if(dbExist)	{
			 //the database already exists
		 } else	{
			 //This method creates an empty database in the default system path               
			 //of the application
			 
			 /*File dbPath = new File(DB_PATH);
			 	if (!dbPath.exists()) {
			 		dbPath.mkdirs();
			 	}
			 	
			 try	{
			 	this.getReadableDatabase();
		 		this.close();
			 }	catch(NullPointerException npe){
				 Log.i("db_error", "Unable to retrieve the database.", npe);
			 }*/
			 
			 this.getReadableDatabase();
			 
			 	try {   
			 		copyDataBase();     		
			 	}	catch (IOException e) {         	
			 		throw new Error("Error Copying Database");         	
			 	}    	
			}     
	 }
	 
	 /**     
	  * Check if the database already exist to avoid re-copying the file each time you open the application.     
	  * @return true if it exists, false if it doesn't     */
	 private boolean checkDataBase()	{     	
//		 File dbFile = mDatabaseContext.getDatabasePath(DB_NAME);
		 File dbFile = new File(DB_PATH+DB_NAME);
		 return dbFile.exists();
	 }
	 
	 /** Copies your database from your local assets-folder to the just created empty database in the     
	  * system folder, from where it can be accessed and handled.    
	  * This is done by transferring byte stream.     
	  * */
	 private void copyDataBase() throws IOException	{     	
		 //Open your local database as an input stream    	
		 InputStream myInput = mDatabaseContext.getAssets().open(DB_NAME);     	
		 // Path to the just created empty database    	
		 String outFileName = DB_PATH + DB_NAME;     	
		 //Open the empty database as the output stream    	
		 OutputStream myOutput = new FileOutputStream(outFileName);     	
		 //transfer bytes from the input file to the output file
		 
		 byte[] buffer = new byte[1024];    	
		 int length;    	
		 while ((length = myInput.read(buffer))>0)	{    		
			 myOutput.write(buffer, 0, length);    	
		 } 
		 
		//Close the streams    	
		 myOutput.flush();    	
		 myOutput.close();    	
		 myInput.close();
	 }
	 
		/**     
		 * Opens the pre-populated database.     
		 * */
	 public void openDataBase() throws SQLException	{     	
		 //Open the database        
		 String myPath = DB_PATH + DB_NAME;    	
		 myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
		//myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
	 }
	 
	 @Override	
	 public synchronized void close()	{     	    
		 if(myDataBase != null)    		    
			 myDataBase.close();     	    
		 super.close(); 	
	}
	 
	 @Override	
	 public void onCreate(SQLiteDatabase db) { 	
		 //TODO Auto-generate method STUB
	 } 	
	 
	 @Override	
	 public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { 	
		 //TODO Auto-generate method STUB
	 }
	 
	 @SuppressLint("NewApi")
	public Cursor lookupCountryDatabase(String checkingCode)	{
//		 Log.i(DB_PATH, "DatabaseHelperSQL: Active Database: " + getDatabaseName());
		 
		 //matches the value of the country code in the phoneNumber and stores the value of the country
		 //from which the call is made
		 Cursor cursor = myDataBase.rawQuery("SELECT * FROM CALLING_CODE WHERE _id ='" + checkingCode + "'", null);
		 return cursor;
	 }
	 
	 
	 @SuppressLint("NewApi")
	public Cursor lookupCityDatabase(String checkingCode)	{
//		 Log.i(DB_PATH, "DatabaseHelperSQL: Active Database: " + getDatabaseName());
 		 
		//matches the value of the std code in the phoneNumber and stores the value of the state and
	 	//city from which the call is made
		 Cursor cursor = myDataBase.rawQuery("SELECT * FROM CODES WHERE _id ='" + checkingCode + "'", null);
		 return cursor;
	 }
	 
		public Cursor lookupMobileDatabase(String checkingCode)	{
//			 Log.i(DB_PATH, "DatabaseHelperSQL: Active Database: " + getDatabaseName());
			 
			 //matches the value of the country code in the phoneNumber and stores the value of the country
			 //from which the call is made
			 Cursor cursor = myDataBase.rawQuery("SELECT * FROM CIRCLE_CODES WHERE _id ='" + checkingCode + "'", null);
			 return cursor;
		 }
		
		public Cursor lookupStateDatabase(String checkingCode)	{
//			 Log.i(DB_PATH, "DatabaseHelperSQL: Active Database: " + getDatabaseName());
			 
			 //matches the value of the country code in the phoneNumber and stores the value of the country
			 //from which the call is made
			 Cursor cursor = myDataBase.rawQuery("SELECT * FROM STATE_CODE WHERE _id ='" + checkingCode + "'", null);
			 return cursor;
		 }
		
		public Cursor lookupCarrierDatabase(String checkingCode)	{
//			 Log.i(DB_PATH, "DatabaseHelperSQL: Active Database: " + getDatabaseName());
			 
			 //matches the value of the country code in the phoneNumber and stores the value of the country
			 //from which the call is made
			 Cursor cursor = myDataBase.rawQuery("SELECT * FROM CARRIER_CODE WHERE _id ='" + checkingCode + "'", null);
			 return cursor;
		 }
}