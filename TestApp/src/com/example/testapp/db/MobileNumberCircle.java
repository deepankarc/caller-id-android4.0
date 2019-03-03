package com.example.testapp.db;

import java.io.IOException;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Handler;
import android.util.Log;

import com.example.testapp.CollateData;

public class MobileNumberCircle {
	
	private Context context;
	
	public static String mobileCarrier;
	public static String callingState;
	public static String PASS_CALLING_MOBILE_VALUES = "com.example.testapp.MobileNumberCircle";
	
	public MobileNumberCircle(Context context)	{
		this.context = context;
	}
	
	public boolean searchMobileCircleCode(String checkingCode)	{
		mobileCarrier = null; 
		callingState = null;
		
		DatabaseHelperSQL myDbHelper = new DatabaseHelperSQL(context, "MOBILE_CIRCLES.db", "CIRCLE_CODES");        
        
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
		
		Cursor cursor =  myDbHelper.lookupMobileDatabase(checkingCode);
		
		try	{
			if(cursor.moveToFirst() && cursor.getCount()>0)	{
				//gets the value of the country form which the call is made
				callingState = cursor.getString(cursor.getColumnIndex("STATE"));
				mobileCarrier = cursor.getString(cursor.getColumnIndex("CARRIER"));
			}
		} finally	{
			cursor.close();
		};
		Log.i(PASS_CALLING_MOBILE_VALUES, "MobileNumberCircle : callingState : " + callingState);
		myDbHelper.close();
		
		if(callingState == null)	{
			//returns a No Match Found error, to indicate no such country code exists in the application
			//database
			return false;
		}
		else	{
			CarrierAndStateCode getCodes = new CarrierAndStateCode(context);
			getCodes.searchStateCode(callingState);
			getCodes.searchCarrierCode(mobileCarrier);
			
			//pass the state from which the call is made to the display activity
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
}
