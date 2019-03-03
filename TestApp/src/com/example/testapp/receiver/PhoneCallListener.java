package com.example.testapp.receiver;

import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.example.testapp.CollateData;
import com.example.testapp.services.ContactMatch;


public class PhoneCallListener extends PhoneStateListener {

    private final Context context;
    
	public static String LOG_TAG = "com.example.testapp.receiver";
	public static String phoneNumber;
	public static boolean callEnded = false;
	
	//explicitly assign the context to the constructor
	public PhoneCallListener(Context context)	{
	this.context = context;
	}
    
    @Override
    public void onCallStateChanged(int state, String incomingNumber) {
    
        switch(state)	{
        case TelephonyManager.CALL_STATE_RINGING: phoneNumber = incomingNumber.substring(1);
        										  Log.i(LOG_TAG, "PhoneCallListener: phoneNumber: " + phoneNumber);
        										  //starts the ContactMatch class which searches for the incoming
        										  //phone number in the contact list
        										  ContactMatch contactMatch = new ContactMatch(context);
        										  contactMatch.contactListNumberSearch();
        	break;
        	
        case TelephonyManager.CALL_STATE_OFFHOOK: Log.i(LOG_TAG, "OFFHOOK");
        	break;
        	
        case TelephonyManager.CALL_STATE_IDLE: CollateData collatedata = new CollateData();
        									   collatedata.stopDisplay();
        	break;
        }
    }
}