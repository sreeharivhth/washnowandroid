package wash.midest.com.mrwashapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

import wash.midest.com.mrwashapp.screens.SplashActivity;

public class PromotionData implements Parcelable {

    public List<Data> getPromotionData() {
        return promotionData;
    }

    public void setPromotionData(List<Data> promotionData) {
        this.promotionData = promotionData;
    }

    List<Data> promotionData;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.promotionData);
    }

    public PromotionData() {
    }

    protected PromotionData(Parcel in) {
        this.promotionData = in.createTypedArrayList(Data.CREATOR);
    }

    public static final Parcelable.Creator<PromotionData> CREATOR = new Parcelable.Creator<PromotionData>() {
        @Override
        public PromotionData createFromParcel(Parcel source) {
            return new PromotionData(source);
        }

        @Override
        public PromotionData[] newArray(int size) {
            return new PromotionData[size];
        }
    };
}
