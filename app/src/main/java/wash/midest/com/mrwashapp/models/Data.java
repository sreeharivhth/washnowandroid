package wash.midest.com.mrwashapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Sreehari.KV on 3/14/2018.
 */

public class Data implements Parcelable {

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
    @SerializedName("question")
    @Expose
    private String question;
    @SerializedName("answer")
    @Expose
    private String answer;

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

    //=================== FAQ ========================//
    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
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


    //=================== MyProfile ========================//
    @SerializedName("firstName")
    @Expose
    private String firstName;
    @SerializedName("lastName")
    @Expose
    private String lastName;
    /*@SerializedName("email")
    @Expose
    private Object email;*/
    @SerializedName("mobile")
    @Expose
    private String mobile;
    @SerializedName("dialingCode")
    @Expose
    private String dialingCode;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getDialingCode() {
        return dialingCode;
    }

    public void setDialingCode(String dialingCode) {
        this.dialingCode = dialingCode;
    }

    //=================== MyProfile ========================//
    @SerializedName("phone")
    @Expose
    private String phone;

    @SerializedName("address")
    @Expose
    private String address;


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    //=================== Price List ========================//
    @SerializedName("itemName")
    @Expose
    private String itemName;

    @SerializedName("price")
    @Expose
    private String price;

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }





    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.memberId);
        dest.writeString(this.email);
        dest.writeString(this.isVerified);
        dest.writeString(this.active);
        dest.writeString(this.question);
        dest.writeString(this.answer);
        dest.writeString(this.userId);
        dest.writeString(this.token);
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.deliveryTime);
        dest.writeString(this.firstName);
        dest.writeString(this.lastName);
        dest.writeString(this.mobile);
        dest.writeString(this.dialingCode);
        dest.writeString(this.phone);
        dest.writeString(this.address);
        dest.writeString(this.itemName);
        dest.writeString(this.price);
    }

    public Data() {
    }

    protected Data(Parcel in) {
        this.memberId = in.readString();
        this.email = in.readString();
        this.isVerified = in.readString();
        this.active = in.readString();
        this.question = in.readString();
        this.answer = in.readString();
        this.userId = in.readString();
        this.token = in.readString();
        this.id = in.readString();
        this.name = in.readString();
        this.deliveryTime = in.readString();
        this.firstName = in.readString();
        this.lastName = in.readString();
        this.mobile = in.readString();
        this.dialingCode = in.readString();
        this.phone = in.readString();
        this.address = in.readString();
        this.itemName = in.readString();
        this.price = in.readString();
    }

    public static final Parcelable.Creator<Data> CREATOR = new Parcelable.Creator<Data>() {
        @Override
        public Data createFromParcel(Parcel source) {
            return new Data(source);
        }

        @Override
        public Data[] newArray(int size) {
            return new Data[size];
        }
    };
}
