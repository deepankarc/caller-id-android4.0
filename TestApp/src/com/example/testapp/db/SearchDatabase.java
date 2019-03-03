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

public class SearchDatabase	{
	
	private Context context;
	private static String callingCountry;
	
	public static String PASS_CALLING_COUNTRY = "com.example.testapp.db";
	
	public SearchDatabase(Context context){
		this.context = context;
	}
	 
	@SuppressLint("NewApi") 
	public boolean searchCountryActivity(String checkingCode)	{
		callingCountry = null;
		
		DatabaseHelperSQL myDbHelper = new DatabaseHelperSQL(context, "COUNTRY_CODES.db", "CALLING_CODE");        
        
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
		
		Cursor cursor =  myDbHelper.lookupCountryDatabase(checkingCode);
		
		try	{
			if(cursor.moveToFirst() && cursor.getCount()>0)	{
				//gets the value of the country form which the call is made
				callingCountry = cursor.getString(cursor.getColumnIndex("COUNTRY"));		 
			}
		} finally	{
			cursor.close();
		};
		Log.i(PASS_CALLING_COUNTRY, "SearchDatabase : callingCountry : " + callingCountry);
		myDbHelper.close();
		
		if(callingCountry == null)	{
			//returns a No Match Found error, to indicate no such country code exists in the application
			//database
			return false;
		}
		else	{	
			if(!callingCountry.equalsIgnoreCase("India"))	{

				 new Handler().postDelayed(new Runnable() {
	                 @Override
	                 public void run() {

	                     Intent collateDataShow = new Intent(context, CollateData.class);
	                     collateDataShow.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	                     context.startActivity(collateDataShow);
	                     
	                 }
	             }, 1000);
			}
			return true;
		 }
	}
	
	//returns the value of the country from which the call is made
	public static String getCountry()	{
		return callingCountry;
	}
}