package com.ganesh.vendorapp.models;

import java.io.Serializable;
import java.util.List;

public class Variants implements Serializable {

    private String variant_name;
    private int quantity;
    private double price;
    private List<String> image;

    public Variants(String variant_name, int quantity, double price, List<String> image) {
        this.variant_name = variant_name;
        this.quantity = quantity;
        this.price = price;
        this.image = image;
    }

    public List<String> getImage() {
        return image;
    }

    public void setImage(List<String> image) {
        this.image = image;
    }

    public String getVariant_name() {
        return variant_name;
    }

    public void setVariant_name(String variant_name) {
        this.variant_name = variant_name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Variants() {
    }

    @Override
    public String toString() {
        return "Variants{" +
                "variant_name='" + variant_name + '\'' +
                ", quantity=" + quantity +
                ", price=" + price +
                ", image=" + image +
                '}';
    }
}
