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

    @SerializedName("current_quantity")
    @Expose
    private int currentQuantity;

    @SerializedName("price")
    @Expose
    private int price;

    @SerializedName("variant_name")
    @Expose
    private String variantName;

    public VariantsItem(List<String> image, int variantId, int quantity, int currentQuantity, int price, String variantName) {
        this.image = image;
        this.variantId = variantId;
        this.quantity = quantity;
        this.currentQuantity = currentQuantity;
        this.price = price;
        this.variantName = variantName;
    }

    public List<String> getImage() {
        return image;
    }

    public void setImage(List<String> image) {
        this.image = image;
    }

    public int getVariantId() {
        return variantId;
    }

    public void setVariantId(int variantId) {
        this.variantId = variantId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getCurrentQuantity() {
        return currentQuantity;
    }

    public void setCurrentQuantity(int currentQuantity) {
        this.currentQuantity = currentQuantity;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getVariantName() {
        return variantName;
    }

    public void setVariantName(String variantName) {
        this.variantName = variantName;
    }

    @Override
    public String toString() {
        return "VariantsItem{" +
                "image=" + image +
                ", variantId=" + variantId +
                ", quantity=" + quantity +
                ", currentQuantity=" + currentQuantity +
                ", price=" + price +
                ", variantName='" + variantName + '\'' +
                '}';
    }
}
