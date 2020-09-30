package com.ganesh.vendorapp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ProductsItem implements Serializable {
    @SerializedName("product_id")
    @Expose
    private String productId;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("company")
    @Expose
    private String company;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("variants")
    @Expose
    private List<VariantsItem> variants;

    public ProductsItem(String productId, String description, String company, String title, List<VariantsItem> variants) {
        this.productId = productId;
        this.description = description;
        this.company = company;
        this.title = title;
        this.variants = variants;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }


    public String getProductId() {
        return productId;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getCompany() {
        return company;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setVariants(List<VariantsItem> variants) {
        this.variants = variants;
    }

    public List<VariantsItem> getVariants() {
        return variants;
    }

    @Override
    public String toString() {
        return "ProductsItem{" +
                "productId='" + productId + '\'' +
                ", description='" + description + '\'' +
                ", company='" + company + '\'' +
                ", title='" + title + '\'' +
                ", variants=" + variants +
                '}';
    }
}
