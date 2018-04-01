package wash.midest.com.mrwashapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Sreehari.KV on 3/22/2018.
 */

public class Error implements Parcelable {
    @SerializedName("errCode")
    @Expose
    private Integer errCode;
    @SerializedName("errMessage")
    @Expose
    private String errMessage;

    public Integer getErrCode() {
        return errCode;
    }

    public void setErrCode(Integer errCode) {
        this.errCode = errCode;
    }

    public String getErrMessage() {
        return errMessage;
    }

    public void setErrMessage(String errMessage) {
        this.errMessage = errMessage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.errCode);
        dest.writeString(this.errMessage);
    }

    public Error() {
    }

    protected Error(Parcel in) {
        this.errCode = (Integer) in.readValue(Integer.class.getClassLoader());
        this.errMessage = in.readString();
    }

    public static final Parcelable.Creator<Error> CREATOR = new Parcelable.Creator<Error>() {
        @Override
        public Error createFromParcel(Parcel source) {
            return new Error(source);
        }

        @Override
        public Error[] newArray(int size) {
            return new Error[size];
        }
    };
}
