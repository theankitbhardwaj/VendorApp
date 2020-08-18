package com.ganesh.vendorapp.models;

import java.util.List;

public class ProductListModel {
    private List<Products> products;

    public ProductListModel(List<Products> products) {
        this.products = products;
    }

    public List<Products> getProducts() {
        return products;
    }

    public void setProducts(List<Products> products) {
        this.products = products;
    }
}
