package wash.midest.com.mrwashapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sreehari.KV on 3/26/2018.
 */

public class GeneralListDataPojo implements Parcelable {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("statusCode")
    @Expose
    private Integer statusCode;
    @SerializedName("data")
    @Expose
    private List<Data> data;
    @SerializedName("error")
    @Expose
    private List<Error> error;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public List<Error> getError() {
        return error;
    }

    public void setError(List<Error> error) {
        this.error = error;
    }

    public List<Data> getData() {return data; }

    public void setData(List<Data> data) { this.data = data; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.status);
        dest.writeValue(this.statusCode);
        dest.writeList(this.data);
        dest.writeList(this.error);
    }

    public GeneralListDataPojo() {
    }

    protected GeneralListDataPojo(Parcel in) {
        this.status = in.readString();
        this.statusCode = (Integer) in.readValue(Integer.class.getClassLoader());
        this.data = new ArrayList<Data>();
        in.readList(this.data, Data.class.getClassLoader());
        this.error = new ArrayList<Error>();
        in.readList(this.error, Error.class.getClassLoader());
    }

    public static final Parcelable.Creator<GeneralListDataPojo> CREATOR = new Parcelable.Creator<GeneralListDataPojo>() {
        @Override
        public GeneralListDataPojo createFromParcel(Parcel source) {
            return new GeneralListDataPojo(source);
        }

        @Override
        public GeneralListDataPojo[] newArray(int size) {
            return new GeneralListDataPojo[size];
        }
    };
}
