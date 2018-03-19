package wash.midest.com.mrwashapp.appservices;

import java.util.HashMap;

/**
 * Created by Sreehari.KV on 3/14/2018.
 */

public class RegistrationObj {

    public String firstName;
    public String lastName;
    public String email;
    public String password;
    public String mobile;
    public String imei;
    public String appId;
    public String dialingCode;

    public RegistrationObj(String firstName,String lastName,String email,
            String password,String mobile,String imei,String appId,String dialingCode){
        this.firstName=firstName;
        this.lastName=lastName;
        this.email=email;
        this.password=password;
        this.mobile=mobile;
        this.imei=imei;
        this.appId=appId;
        this.dialingCode = dialingCode;
    }
}
