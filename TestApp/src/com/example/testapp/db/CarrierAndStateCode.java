package com.example.testapp.db;

import java.io.IOException;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.util.Log;

public class CarrierAndStateCode {
	
	private Context context;
	private static String callingState;
	private static String mobileCarrier;
	
	public CarrierAndStateCode(Context context)	{
		this.context = context;
	}
	
	//gets the value of the state to which the mobile number belongs
	public void searchStateCode(String checkingCode)	{
		callingState = null;
		
		DatabaseHelperSQL myDbHelper = new DatabaseHelperSQL(context, "MOB_CODES.db", "STATE_CODE");        
        
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
		
			Cursor cursor =  myDbHelper.lookupStateDatabase(checkingCode);
		
			try	{
				if(cursor.moveToFirst() && cursor.getCount()>0)	{
					//gets the value of the country form which the call is made
					callingState = cursor.getString(cursor.getColumnIndex("STATE"));
				}
			} finally	{
				cursor.close();
			};
			Log.i("###", "MobileNumberCircle : callingState : " + callingState);
			myDbHelper.close();
	}
	
	//gets the value of the incoming call's network operator
	public void searchCarrierCode(String checkingCode)	{
		mobileCarrier = null;
		
		DatabaseHelperSQL myDbHelper = new DatabaseHelperSQL(context, "MOB_CODES.db", "CARRIER_CODE");        
        
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
	
		Cursor cursor =  myDbHelper.lookupCarrierDatabase(checkingCode);
	
		try	{
			if(cursor.moveToFirst() && cursor.getCount()>0)	{
				//gets the value of the country form which the call is made
				mobileCarrier = cursor.getString(cursor.getColumnIndex("CARRIER"));
			}
		} finally	{
			cursor.close();
		};
		Log.i("###", "MobileNumberCircle : callingState : " + mobileCarrier);
		myDbHelper.close();
		
	}
	
	//returns the state from which call is made
	public static String getState()	{
		return callingState;
	}
	
	//returns the network operator of the mobile number of the incoming call
	public static String getCarrier()	{
		return mobileCarrier;
	}
}
