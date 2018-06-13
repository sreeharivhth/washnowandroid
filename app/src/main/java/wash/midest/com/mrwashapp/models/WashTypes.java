package wash.midest.com.mrwashapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Sreehari.KV on 3/13/2018.
 */

public class WashTypes implements Parcelable {

    @SerializedName("washType")
    @Expose
    String washType;
    @SerializedName("time")
    @Expose
    String time;
    @SerializedName("id")
    @Expose
    int id;

    @SerializedName("deliveryTime")
    @Expose
    int deliveryTime;

    @SerializedName("pickupTime")
    @Expose
    int pickupTime;


    public int getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(int deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public int getPickupTime() {
        return pickupTime;
    }

    public void setPickupTime(int pickupTime) {
        this.pickupTime = pickupTime;
    }


    public int getId() { return id;     }

    public void setId(int id) { this.id = id;     }

    public String getWashType() {
        return washType;
    }

    public void setWashType(String washType) {
        this.washType = washType;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.washType);
        dest.writeString(this.time);
        dest.writeInt(this.id);
        dest.writeInt(this.deliveryTime);
        dest.writeInt(this.pickupTime);
    }

    public WashTypes() {
    }

    protected WashTypes(Parcel in) {
        this.washType = in.readString();
        this.time = in.readString();
        this.id = in.readInt();
        this.deliveryTime = in.readInt();
        this.pickupTime = in.readInt();
    }

    public static final Parcelable.Creator<WashTypes> CREATOR = new Parcelable.Creator<WashTypes>() {
        @Override
        public WashTypes createFromParcel(Parcel source) {
            return new WashTypes(source);
        }

        @Override
        public WashTypes[] newArray(int size) {
            return new WashTypes[size];
        }
    };
}
