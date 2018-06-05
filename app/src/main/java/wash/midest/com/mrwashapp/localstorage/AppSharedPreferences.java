package wash.midest.com.mrwashapp.localstorage;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Sreehari.KV on 3/22/2018.
 */

public class AppSharedPreferences {

    private static AppSharedPreferences mAppSharedPreferences;
    private SharedPreferences mSharedPref;
    private SharedPreferences.Editor mEditor;

    public String VERIFIED_STATUS="VERIFIED_STATUS";
    public String ACTIVE_STATUS="ACTIVE_STATUS";
    public String MEMBER_ID="MEMBER_ID";
    public String TOKEN_SESSION="TOKEN_SESSION";
    public String USER_ID="USER_ID";
    public String LOCATION_PRESENT="LOCATION_PRESENT";
    public String ADDRESS="ADDRESS";
    public String COORDINATES_LAT="COORDINATES_LAT";
    public String COORDINATES_LON="COORDINATES_LON";

    public String LAT_SELECTED="LAT_SELECTED";
    public String LON_SELECTED="LON_SELECTED";
    public String SELECTED_ADDERSS="SELECTED_ADDERSS";
    public String HOUSE_FLAT="HOUSE_FLAT";
    public String LANDMARK="LANDMARK";


    private AppSharedPreferences(Context context){
        mSharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        mEditor = mSharedPref.edit();
    }

    public static AppSharedPreferences getInstnace(Context context){
            if(null==mAppSharedPreferences){
                mAppSharedPreferences=new AppSharedPreferences(context);
            }
            return mAppSharedPreferences;
    }

    public boolean setPreferenceInt(String key,int value){
        try {
            mEditor.putInt(key, value);
            mEditor.commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            return false;
        }
    }

    public int getPreferenceInt(String key){
        int intValue = mSharedPref.getInt(key, -1);
        return intValue;
    }

    public boolean setPreferenceString(String key,String value){
        try {
            mEditor.putString(key, value);
            mEditor.commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            return false;
        }
    }

    public String getPreferenceString(String key){
        String stringValue = mSharedPref.getString(key, null);
        return stringValue;
    }

    public boolean setPreferenceBool(String key,boolean value){
        try {
            mEditor.putBoolean(key, value);
            mEditor.commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            return false;
        }
    }

    public boolean getPreferenceBool(String key){
        boolean value = mSharedPref.getBoolean(key, false);
        return value;
    }


    public double getPreferenceDouble(final String key, final double defaultValue) {
        if ( !mSharedPref.contains(key))
            return defaultValue;

        return Double.longBitsToDouble(mSharedPref.getLong(key, 0));
    }

    public boolean setPreferenceDouble(String key,double value){
        try {
            mEditor.putLong(key, Double.doubleToRawLongBits(value));
            mEditor.commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            return false;
        }
    }
}
