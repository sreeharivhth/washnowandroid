package wash.midest.com.mrwashapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderDetail implements Parcelable {
    @SerializedName("orderId")
    @Expose
    private Integer orderId;
    @SerializedName("createdOn")
    @Expose
    private Integer createdOn;
    @SerializedName("pickupDate")
    @Expose
    private Integer pickupDate;
    @SerializedName("deliveryDate")
    @Expose
    private Integer deliveryDate;

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Integer createdOn) {
        this.createdOn = createdOn;
    }

    public Integer getPickupDate() {
        return pickupDate;
    }

    public void setPickupDate(Integer pickupDate) {
        this.pickupDate = pickupDate;
    }

    public Integer getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Integer deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.orderId);
        dest.writeValue(this.createdOn);
        dest.writeValue(this.pickupDate);
        dest.writeValue(this.deliveryDate);
    }

    public OrderDetail() {
    }

    protected OrderDetail(Parcel in) {
        this.orderId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.createdOn = (Integer) in.readValue(Integer.class.getClassLoader());
        this.pickupDate = (Integer) in.readValue(Integer.class.getClassLoader());
        this.deliveryDate = (Integer) in.readValue(Integer.class.getClassLoader());
    }

    public static final Parcelable.Creator<OrderDetail> CREATOR = new Parcelable.Creator<OrderDetail>() {
        @Override
        public OrderDetail createFromParcel(Parcel source) {
            return new OrderDetail(source);
        }

        @Override
        public OrderDetail[] newArray(int size) {
            return new OrderDetail[size];
        }
    };
}
