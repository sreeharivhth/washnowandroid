package wash.midest.com.mrwashapp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Sreehari.KV on 5/17/2018.
 */

public class Gents {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("itemName")
    @Expose
    private String itemName;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("categoryName")
    @Expose
    private String categoryName;
    @SerializedName("isMeter")
    @Expose
    private String isMeter;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getIsMeter() {
        return isMeter;
    }

    public void setIsMeter(String isMeter) {
        this.isMeter = isMeter;
    }
}
