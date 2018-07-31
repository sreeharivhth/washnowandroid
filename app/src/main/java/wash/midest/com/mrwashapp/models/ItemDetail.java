package wash.midest.com.mrwashapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ItemDetail implements Parcelable {
    @SerializedName("categoryName")
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
    private Integer amount;

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

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.categoryName);
        dest.writeString(this.productName);
        dest.writeString(this.count);
        dest.writeString(this.rate);
        dest.writeString(this.comment);
        dest.writeString(this.meter);
        dest.writeValue(this.amount);
    }

    public ItemDetail() {
    }

    protected ItemDetail(Parcel in) {
        this.categoryName = in.readString();
        this.productName = in.readString();
        this.count = in.readString();
        this.rate = in.readString();
        this.comment = in.readString();
        this.meter = in.readString();
        this.amount = (Integer) in.readValue(Integer.class.getClassLoader());
    }

    public static final Parcelable.Creator<ItemDetail> CREATOR = new Parcelable.Creator<ItemDetail>() {
        @Override
        public ItemDetail createFromParcel(Parcel source) {
            return new ItemDetail(source);
        }

        @Override
        public ItemDetail[] newArray(int size) {
            return new ItemDetail[size];
        }
    };
}
