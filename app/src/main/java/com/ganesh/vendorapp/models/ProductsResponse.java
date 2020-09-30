package com.ganesh.vendorapp.models;

import java.util.List;

public class ProductsResponse{
	private boolean error;
	private String message;
	private List<ProductsItem> products;

	public void setError(boolean error){
		this.error = error;
	}

	public boolean isError(){
		return error;
	}

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}

	public void setProducts(List<ProductsItem> products){
		this.products = products;
	}

	public List<ProductsItem> getProducts(){
		return products;
	}

	@Override
 	public String toString(){
		return 
			"ProductsResponse{" + 
			"error = '" + error + '\'' + 
			",message = '" + message + '\'' + 
			",products = '" + products + '\'' + 
			"}";
		}
}