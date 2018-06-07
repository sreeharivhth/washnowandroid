package wash.midest.com.mrwashapp.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import wash.midest.com.mrwashapp.models.DateDifference;

import static android.content.Context.CONNECTIVITY_SERVICE;

/**
 * Created by Sreehari.KV on 3/6/2018.
 */

public class AppUtils {

    private static final String TAG="AppUtils";
            /*
            (?=.*\d)		#   must contains one digit from 0-9
            (?=.*[a-z])		#   must contains one lowercase characters
            (?=.*[A-Z])		#   must contains one uppercase characters
            (?=.*[@#$%])	#   must contains one special symbols in the list ===== '*()=@#$%!^&_"
                .		    #     match anything with previous condition checking
              {8,12}	    #        length at least 8 characters and maximum of 12
            */

    public boolean isValidPassword(String password) {
        Pattern pattern;
        Matcher matcher;
        String PASSWORD_PATTERN = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*['*()=@#$%!^&_\"]).{8,12})";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);
        return matcher.matches();
    }

    public boolean isVersionGreaterThanM(Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
            return true;
        } else {
            return false;
        }
    }

    public String getDeviceIMEI(Context context) {
        TelephonyManager mTelephonyManager;
        mTelephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return null;
        }
        String deviceid = mTelephonyManager.getDeviceId();

        return deviceid;
    }

    public boolean isNetworkConnected(Context context){
        boolean status=false;
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = manager.getActiveNetworkInfo();
        if(activeNetwork != null && activeNetwork.isConnectedOrConnecting()){
            status=true;
        }
        return status;
    }

    public DateDifference printDifference(Date startDate, Date endDate) {

        Log.d(TAG,"startDate : " + startDate);
        Log.d(TAG,"endDate : "+ endDate);

        //milliseconds
        long different = endDate.getTime() - startDate.getTime();

        long diffInHours = TimeUnit.MILLISECONDS.toHours(different);

        Log.d(TAG,"diffInHours : " + diffInHours);

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        /*long elapsedSeconds = different / secondsInMilli;*/
        Log.d(TAG,elapsedDays+" days,"+elapsedHours+" hours, "+elapsedMinutes+"minutes " );

        DateDifference dateDifference=new DateDifference();
        dateDifference.days=elapsedDays;
        dateDifference.hours=elapsedHours;
        dateDifference.minutes=elapsedMinutes;
        dateDifference.differenceInHours=diffInHours;

        return dateDifference;
    }
}
