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
}
