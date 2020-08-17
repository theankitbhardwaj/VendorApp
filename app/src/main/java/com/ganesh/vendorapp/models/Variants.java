package com.ganesh.vendorapp.models;

import java.io.Serializable;

public class Variants implements Serializable {

    private String variant_name;
    private int quantity;
    private double price;

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

    public Variants(String variant_name, int quantity, double price) {
        this.variant_name = variant_name;
        this.quantity = quantity;
        this.price = price;
    }
}
