package com.example.testapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.testapp.db.CarrierAndStateCode;
import com.example.testapp.db.SearchCityDatabase;
import com.example.testapp.db.SearchDatabase;
import com.example.testapp.services.ContactMatch;
import com.example.testapp.services.NumberMatch;

public class CollateData extends Activity {

	public static String PHONE_NUMBER;
	public static String NAME_CONTACT;
	public static String CALLING_COUNTRY;
	public static String CALLING_STATE;
	public static String CALLING_CITY;
	public static String MOBILE_CARRIER;
	
	public static final String BROADCAST = "com.example.testapp.android.action.broadcast";
	
	char firstDigit = NumberMatch.getNumber().charAt(0);
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_fragment);
		
		Window thisWindow = this.getWindow();
	    thisWindow.addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
	    thisWindow.addFlags(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
	    thisWindow.addFlags(WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH);
	    thisWindow.setGravity(Gravity.CENTER_VERTICAL);
	    setTitle("Call Details");
	    
	    getIntent();
	    PHONE_NUMBER = NumberMatch.getNumber();
	    NAME_CONTACT = ContactMatch.getName();
		CALLING_COUNTRY = SearchDatabase.getCountry();
		MOBILE_CARRIER = CarrierAndStateCode.getCarrier();
		
		if(firstDigit=='7' || firstDigit=='8' || firstDigit=='9')	{
			CALLING_STATE = CarrierAndStateCode.getState();
		}
		else	{
			CALLING_STATE = SearchCityDatabase.getState();
			CALLING_CITY = SearchCityDatabase.getCity();
		}
		collateDataActivity();
	}
	
	@Override
	  public boolean onTouchEvent(MotionEvent event) {
	    //On receiving a touch notification outside the application,
	    //finish the activity
	    if (MotionEvent.ACTION_OUTSIDE == event.getAction()) {
	      finish();
	      return true;
	    }

	    // Delegate everything else to activity.
	    return super.onTouchEvent(event);
	  }
	
	@SuppressLint("NewApi")
	public void collateDataActivity()	{

        TextView displayCallerIdDetails = (TextView)findViewById(R.id.display_details);
        displayCallerIdDetails.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.textsize));
        displayCallerIdDetails.setTextColor(Color.WHITE);
        
        if(NAME_CONTACT != null)	{
        	displayCallerIdDetails.setText("Caller : " + NAME_CONTACT);
        }
        else	{
        	if(CALLING_COUNTRY.equalsIgnoreCase("India"))	{
        		if(firstDigit=='7' || firstDigit=='8' || firstDigit=='9')	{
           			displayCallerIdDetails.setText("Call From : " + CALLING_COUNTRY + " | " + CALLING_STATE + 
           												"\n Phone Number : " + PHONE_NUMBER + "\n Mobile Carrier : " + MOBILE_CARRIER);
        		}
        		else	{
        			displayCallerIdDetails.setText("Call From : " + CALLING_COUNTRY + " | " + CALLING_STATE + " | " 
        											+ CALLING_CITY + "\n Phone Number : " + PHONE_NUMBER);
        		}
        	}
        	else	{
        		displayCallerIdDetails.setText("Call From : " + CALLING_COUNTRY);
        	}
        }
	}
	
	public void stopDisplay()	{
		finish();
	}
}