package wash.midest.com.mrwashapp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Sreehari.KV on 3/14/2018.
 */

public class Data {

    //============== Registration =====================//
    @SerializedName("memberId")
    @Expose
    private String memberId;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("isVerified")
    @Expose
    private String isVerified;
    @SerializedName("active")
    @Expose
    private String active;

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIsVerified() {
        return isVerified;
    }

    public void setIsVerified(String isVerified) {
        this.isVerified = isVerified;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    //==============Login =====================//

    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("token")
    @Expose
    private String token;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    //=================== Services ========================//
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    /*@SerializedName("active")
    @Expose
    private String active;*/
    @SerializedName("deliveryTime")
    @Expose
    private String deliveryTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /*public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }*/

    public String getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = deliveryTime;
    }
}
