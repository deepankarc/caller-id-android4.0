package com.example.testapp.db;

import java.io.IOException;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Handler;
import android.util.Log;

import com.example.testapp.CollateData;

public class SearchCityDatabase {

	private Context context;
	
	public static String callingCity;
	public static String callingState;
	public static final String TAG_DATABASE_CONTENTS = "com.example.testapp.db";
	
	public SearchCityDatabase(Context context)	{
		this.context = context;
	}

	@SuppressLint("NewApi")
	public boolean searchCityActivity(String checkingCode)	{
		callingState = null;
		callingCity = null;

		DatabaseHelperSQL myDbHelper = new DatabaseHelperSQL(context, "STD_CODES.db", "CODES");        
       
		try	{         	
			myDbHelper.createDataBase();  	
		}	catch (IOException ioe)	{  		
				throw new Error("Unable To Create Database.");
			}
		
		try	{  		
			myDbHelper.openDataBase();
		}	catch(SQLException sqle)	{
				throw sqle;
			}
		
		Cursor cursor =  myDbHelper.lookupCityDatabase(checkingCode);
		
		try	{
			if(cursor.moveToFirst() && cursor.getCount()>0)	{
				callingState = cursor.getString(cursor.getColumnIndex("STATE"));
				callingCity = cursor.getString(cursor.getColumnIndex("CITY"));
			}
			Log.i(TAG_DATABASE_CONTENTS, "SearchCityDatabase : callingState : " + callingState + "\n" + "callingCity : " + callingCity);
		} finally	{
			cursor.close();
		};
		myDbHelper.close();

	 	if(callingState == null)	{
		 	//returns a No Match Found error, to indicate no such std code exists in the application
		 	//database
		 	return false;
		}
		 else	{
			 new Handler().postDelayed(new Runnable() {
	                 @Override
	                 public void run() {

	                     Intent collateDataShow = new Intent(context, CollateData.class);
	                     collateDataShow.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	                     context.startActivity(collateDataShow);
	                     
	                 }
			 }, 1000);
			return true;
		}
	}
	
	public static String getState()	{
		return callingState;
	}
	
	public static String getCity()	{
		return callingCity;
	}
}