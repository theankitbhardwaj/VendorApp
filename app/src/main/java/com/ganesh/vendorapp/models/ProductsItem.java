package com.ganesh.vendorapp.models;

public class ProductsItem{
	private String productId;
	private String description;
	private String company;
	private String title;

	public void setProductId(String productId){
		this.productId = productId;
	}

	public String getProductId(){
		return productId;
	}

	public void setDescription(String description){
		this.description = description;
	}

	public String getDescription(){
		return description;
	}

	public void setCompany(String company){
		this.company = company;
	}

	public String getCompany(){
		return company;
	}

	public void setTitle(String title){
		this.title = title;
	}

	public String getTitle(){
		return title;
	}

	@Override
 	public String toString(){
		return 
			"ProductsItem{" + 
			"product_id = '" + productId + '\'' + 
			",description = '" + description + '\'' + 
			",company = '" + company + '\'' + 
			",title = '" + title + '\'' + 
			"}";
		}
}
