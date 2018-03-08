package wash.midest.com.mrwashapp.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * Created by Sreehari.KV on 3/6/2018.
 */

public class AppUtils {

            /*
            (?=.*\d)		#   must contains one digit from 0-9
            (?=.*[a-z])		#   must contains one lowercase characters
            (?=.*[A-Z])		#   must contains one uppercase characters
            (?=.*[@#$%])	#   must contains one special symbols in the list ===== '*()=@#$%!^&_"
                .		    #     match anything with previous condition checking
              {8,12}	    #        length at least 8 characters and maximum of 12
            */

    public boolean isValidPassword(String password){
        Pattern pattern;
        Matcher matcher;
        String PASSWORD_PATTERN ="((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*['*()=@#$%!^&_\"]).{8,12})";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);
        return matcher.matches();
    }



}
