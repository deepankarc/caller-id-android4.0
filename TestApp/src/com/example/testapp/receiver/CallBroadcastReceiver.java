package com.example.testapp.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

public class CallBroadcastReceiver extends BroadcastReceiver {

	//Get the base context of the application
	Context context;

	public static String phoneNumber;
	public static String PHONE_NO = "com.example.testapp.receiver";
	
	@Override    
	public void onReceive(Context context, Intent intent) {
		
		//retrieves reference of telephony state
		TelephonyManager telephony = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
		PhoneCallListener customPhoneListener = new PhoneCallListener(context); 
		telephony.listen(customPhoneListener, PhoneStateListener.LISTEN_CALL_STATE);
		//retrieves the value of incoming phone number
		phoneNumber = intent.getStringExtra("incoming_number");         
		Log.i(PHONE_NO, "CallBroadcastReceiver: phoneNumber: " + phoneNumber);

	}
}
