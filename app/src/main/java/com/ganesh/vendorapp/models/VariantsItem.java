package com.ganesh.vendorapp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class VariantsItem implements Serializable {

    @SerializedName("image")
    @Expose
    private List<String> image;

    @SerializedName("variant_id")
    @Expose
    private int variantId;

    @SerializedName("quantity")
    @Expose
    private int quantity;

    @SerializedName("price")
    @Expose
    private int price;

    @SerializedName("variant_name")
    @Expose
    private String variantName;

    public VariantsItem(List<String> image, int variantId, int quantity, int price, String variantName) {
        this.image = image;
        this.variantId = variantId;
        this.quantity = quantity;
        this.price = price;
        this.variantName = variantName;
    }

    public void setImage(List<String> image) {
        this.image = image;
    }

    public List<String> getImage() {
        return image;
    }

    public void setVariantId(int variantId) {
        this.variantId = variantId;
    }

    public int getVariantId() {
        return variantId;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getPrice() {
        return price;
    }

    public void setVariantName(String variantName) {
        this.variantName = variantName;
    }

    public String getVariantName() {
        return variantName;
    }

    @Override
    public String toString() {
        return
                "VariantsItem{" +
                        "image = '" + image + '\'' +
                        ",variant_id = '" + variantId + '\'' +
                        ",quantity = '" + quantity + '\'' +
                        ",price = '" + price + '\'' +
                        ",variant_name = '" + variantName + '\'' +
                        "}";
    }
}
