package com.ganesh.vendorapp.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Products implements Serializable {

    private String product_id;
    private String uid;
    private String title;
    private String company;
    private String description;
    private List<Variants> variants;

    public Products(String product_id, String uid, String title, String company, String description, List<Variants> variants) {
        this.product_id = product_id;
        this.uid = uid;
        this.title = title;
        this.company = company;
        this.description = description;
        this.variants = variants;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Variants> getVariants() {
        return variants;
    }

    public void setVariants(List<Variants> variants) {
        this.variants = variants;
    }

    @Override
    public String toString() {
        return "Products{" +
                "product_id='" + product_id + '\'' +
                ", uid='" + uid + '\'' +
                ", title='" + title + '\'' +
                ", company='" + company + '\'' +
                ", description='" + description + '\'' +
                ", variants=" + variants +
                '}';
    }
}
