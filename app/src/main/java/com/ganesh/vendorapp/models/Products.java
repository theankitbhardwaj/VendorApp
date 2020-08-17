package com.ganesh.vendorapp.models;

import java.io.Serializable;
import java.util.ArrayList;

public class Products implements Serializable{
    private String title;
    private String company;
    private String description;
    private ArrayList<Variants> variants;

    public Products() {
    }

    public Products(String title, String company, String description, ArrayList<Variants> variants) {
        this.title = title;
        this.company = company;
        this.description = description;
        this.variants = variants;
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

    public ArrayList<Variants> getVariants() {
        return variants;
    }

    public void setVariants(ArrayList<Variants> variants) {
        this.variants = variants;
    }
}
