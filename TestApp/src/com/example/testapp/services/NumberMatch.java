package com.example.testapp.services;

//import com.example.testapp.db.SearchCityDatabase;
import android.content.Context;
import android.util.Log;

import com.example.testapp.db.MobileNumberCircle;
import com.example.testapp.db.SearchCityDatabase;
import com.example.testapp.db.SearchDatabase;

public class NumberMatch	{
	
	private Context context;
	private static String matchedNumber;
	
	public static String mobileCircleCode;
	public static String countryCode;
	public static String stdCode;
	public int countryCodeInt;
	public static String TAG_DATABASE_CONTENTS = "com.example.testapp.services";
	//gets phone number from Listener class

	public NumberMatch(Context context)	{
		this.context = context;
	}
	
	void codeCheck(String matchedNumber)	{
		
		boolean FOUND = false;
		int FLAG_CC = 4;
		int numberLength = matchedNumber.length();
	
		while(!FOUND || FLAG_CC==0)	{
			//extracting first FLAG_CC+1 digits
			countryCode = matchedNumber.substring(0,FLAG_CC);
			//the country corresponding to the code is SearchDatabase.callingCountry
			SearchDatabase cc_code = new SearchDatabase(context);
			
			//calls SQLite Database matching function
			if(!cc_code.searchCountryActivity(countryCode))
				//length of the country code decremented
				FLAG_CC--;		
			else
				FOUND = true;
			
		}
		
		if(!FOUND && FLAG_CC==0)	{
			Log.i(TAG_DATABASE_CONTENTS, "No such country code exists in the database.");
		}
		else	{
			FOUND = false;
			countryCode = matchedNumber.substring(0,FLAG_CC);
			
			if(countryCode.equalsIgnoreCase("91"))	{
			//update phone number value to exclude country code
				matchedNumber = matchedNumber.substring(FLAG_CC);
				NumberMatch.matchedNumber = matchedNumber;
				char firstDigit = matchedNumber.charAt(0);
				FLAG_CC = 4;

				if(firstDigit=='7' || firstDigit=='8' || firstDigit=='9')	{
					
					//get the first four digits of the incoming number
					mobileCircleCode = matchedNumber.substring(0,4);
					MobileNumberCircle mnc_code = new MobileNumberCircle(context);
					
					if(!mnc_code.searchMobileCircleCode(mobileCircleCode))	{	
						Log.i(TAG_DATABASE_CONTENTS, "Incoming number is not a mobile number.");
					}
				}
				else	{
					FLAG_CC = 4;
					
					while(!FOUND || FLAG_CC==0)	{
						//extracting first FLAG_CC digits
						stdCode = matchedNumber.substring(0,FLAG_CC);
						Log.i("TEST", "#### " + stdCode);
						SearchCityDatabase cc_code = new SearchCityDatabase(context);
						
						//calls SQLite Database matching function
						if(!cc_code.searchCityActivity(stdCode))
							//length of the std code decremented
							FLAG_CC--;		
						else
							FOUND = true;	 
			
					}
					
					if(!FOUND && FLAG_CC==0)	{
						Log.i(TAG_DATABASE_CONTENTS, "No such std code exists in the database.");
					}
					else	{
						NumberMatch.matchedNumber = matchedNumber.substring(FLAG_CC);
					}
				}
			}
			else	{
				if(numberLength<=10)	{
					FLAG_CC = 4;
					
					while(!FOUND || FLAG_CC==0)	{
						//extracting first FLAG_CC digits
						stdCode = matchedNumber.substring(0,FLAG_CC);
						SearchCityDatabase cc_code = new SearchCityDatabase(context);
								
						//calls SQLite Database matching function
						if(!cc_code.searchCityActivity(stdCode))
							//length of the std code decremented
							FLAG_CC--;		
						else
							FOUND = true;	 
						}
					
						if(!FOUND && FLAG_CC==0)	{
							Log.i(TAG_DATABASE_CONTENTS, "No such std code exists in the database.");
						}
						else	{
							NumberMatch.matchedNumber = matchedNumber.substring(FLAG_CC);
						}
					}
				else	{
					Log.i(TAG_DATABASE_CONTENTS, "No value matching the current number exists in the database." + matchedNumber + " ### " + mobileCircleCode);
				}
			}
		}
	}
	
    public static String getNumber()	{
    	return matchedNumber;
    }
}		