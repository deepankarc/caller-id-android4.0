package com.example.testapp.services;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.provider.ContactsContract;
import android.util.Log;

import com.example.testapp.CollateData;
import com.example.testapp.receiver.PhoneCallListener;

public class ContactMatch	{
	
	private Context context;
	private static String nameContact;
	
    public static String PASS_CONTACT_NAME = "com.example.testapp.services";
    
    public ContactMatch(Context context)	{
    	this.context = context;
    }
    
    // Search for contact in the contact list
    public void contactListNumberSearch()	{
    	Log.i(PASS_CONTACT_NAME,"We are here!");
    	nameContact = null;
    	
    	boolean FLAG_SEARCH = false;
    	
    	ContentResolver cr = context.getContentResolver();
    	//URI filters and encapsulates the phoneNumber into the URI object uri.
    	Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(PhoneCallListener.phoneNumber));
    	//generates an array which contains the name of the columns required from the database
    	Cursor numberSearch = cr.query(uri, null, null, null, null);
    	  	
    	try	{
    		if (numberSearch.moveToFirst() && numberSearch.getCount()>0) {
    			//retrieves the name and phone number of the contact present in the contact list
    			Log.i(PASS_CONTACT_NAME, numberSearch.getString(numberSearch.getColumnIndex("number")));
    			nameContact = numberSearch.getString(numberSearch.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
    			
    				if(nameContact != null)	{
    					FLAG_SEARCH = true;
    				}
    			}
    	} finally	{
    		//closes the cursor if a matching contact is found
    		if(numberSearch != null)	{
    			numberSearch.close();
    		}
    	}
    	
    	if(FLAG_SEARCH)	{
    		//if the contact is found in the contact list the name of the contact is displayed by the application
    		new Handler().postDelayed(new Runnable() {
        		@Override
        		public void run() {

        			Intent collateDataShow = new Intent(context, CollateData.class);
        			collateDataShow.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        			context.startActivity(collateDataShow);
            
        		}
    		}, 1000);
    	}
    	else	{
    		//passes the incoming number to the NumberMatch class
    		NumberMatch throwNum = new NumberMatch(context);
    		throwNum.codeCheck(PhoneCallListener.phoneNumber);
    	}
    }
    
    public static String getName()	{
    	return nameContact;
    }
    
}