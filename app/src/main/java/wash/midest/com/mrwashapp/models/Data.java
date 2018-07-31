package wash.midest.com.mrwashapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

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

    @SerializedName("pickupTime")
    @Expose
    private String pickupTime;

    public String getPickupTime() {
        return pickupTime;
    }

    public void setPickupTime(String pickupTime) {
        this.pickupTime = pickupTime;
    }

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

    @SerializedName("Gents")
    @Expose
    private List<Gents> gents = null;

    @SerializedName("Others")
    @Expose
    private List<Others> others = null;

    @SerializedName("Ladies")
    @Expose
    private List<Ladies> ladies = null;

    public List<Ladies> getLadies() {
        return ladies;
    }

    public void setLadies(List<Ladies> ladies) {
        this.ladies = ladies;
    }

    public List<Gents> getGents() {
        return gents;
    }

    public void setGents(List<Gents> gents) {
        this.gents = gents;
    }

    public List<Others> getOthers() {
        return others;
    }

    public void setOthers(List<Others> others) {
        this.others = others;
    }

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

    //=================== My Order ========================//
    @SerializedName("orderId")
    @Expose
    private String orderId;

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("orderDate")
    @Expose
    private String orderDate;

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    //=================== My Offers ========================//

    @SerializedName("promoCodeId")
    @Expose
    private String promoCodeId;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("offer")
    @Expose
    private String offer;

    @SerializedName("code")
    @Expose
    private String code;

    public String getPromoCodeId() {
        return promoCodeId;
    }

    public void setPromoCodeId(String promoCodeId) {
        this.promoCodeId = promoCodeId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOffer() {
        return offer;
    }

    public void setOffer(String offer) {
        this.offer = offer;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    //=================== Order details ========================//

    @SerializedName("itemDetails")
    @Expose
    private List<ItemDetail> itemDetails;

    @SerializedName("netAmount")
    @Expose
    private Integer netAmount;

    @SerializedName("orderDetails")
    @Expose
    private List<OrderDetail> orderDetails = null;

    public List<ItemDetail> getItemDetails() {
        return itemDetails;
    }

    public void setItemDetails(List<ItemDetail> itemDetails) {
        this.itemDetails = itemDetails;
    }

    public Integer getNetAmount() {
        return netAmount;
    }

    public void setNetAmount(Integer netAmount) {
        this.netAmount = netAmount;
    }

    public List<OrderDetail> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OrderDetail> orderDetails) {
        this.orderDetails = orderDetails;
    }

    /*@SerializedName("categoryName")
    @Expose
    private String categoryName;

    @SerializedName("productName")
    @Expose
    private String productName;

    @SerializedName("count")
    @Expose
    private String count;

    @SerializedName("rate")
    @Expose
    private String rate;

    @SerializedName("comment")
    @Expose
    private String comment;

    @SerializedName("meter")
    @Expose
    private String meter;

    @SerializedName("amount")
    @Expose
    private String amount;

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getMeter() {
        return meter;
    }

    public void setMeter(String meter) {
        this.meter = meter;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }*/


    public Data() {
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
        dest.writeString(this.pickupTime);
        dest.writeString(this.firstName);
        dest.writeString(this.lastName);
        dest.writeString(this.mobile);
        dest.writeString(this.dialingCode);
        dest.writeString(this.phone);
        dest.writeString(this.address);
        dest.writeString(this.itemName);
        dest.writeString(this.price);
        dest.writeList(this.gents);
        dest.writeList(this.others);
        dest.writeList(this.ladies);
        dest.writeString(this.orderId);
        dest.writeString(this.status);
        dest.writeString(this.orderDate);
        dest.writeString(this.promoCodeId);
        dest.writeString(this.title);
        dest.writeString(this.offer);
        dest.writeString(this.code);
        dest.writeTypedList(this.itemDetails);
        dest.writeValue(this.netAmount);
        dest.writeTypedList(this.orderDetails);
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
        this.pickupTime = in.readString();
        this.firstName = in.readString();
        this.lastName = in.readString();
        this.mobile = in.readString();
        this.dialingCode = in.readString();
        this.phone = in.readString();
        this.address = in.readString();
        this.itemName = in.readString();
        this.price = in.readString();
        this.gents = new ArrayList<Gents>();
        in.readList(this.gents, Gents.class.getClassLoader());
        this.others = new ArrayList<Others>();
        in.readList(this.others, Others.class.getClassLoader());
        this.ladies = new ArrayList<Ladies>();
        in.readList(this.ladies, Ladies.class.getClassLoader());
        this.orderId = in.readString();
        this.status = in.readString();
        this.orderDate = in.readString();
        this.promoCodeId = in.readString();
        this.title = in.readString();
        this.offer = in.readString();
        this.code = in.readString();
        this.itemDetails = in.createTypedArrayList(ItemDetail.CREATOR);
        this.netAmount = (Integer) in.readValue(Integer.class.getClassLoader());
        this.orderDetails = in.createTypedArrayList(OrderDetail.CREATOR);
    }

    public static final Creator<Data> CREATOR = new Creator<Data>() {
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
