package com.ganesh.vendorapp.models;

import java.util.List;

public class ProductsItem{
	private String productId;
	private String description;
	private String company;
	private String title;
	private List<VariantsItem> variants;

	public ProductsItem(String productId, String description, String company, String title, List<VariantsItem> variants) {
		this.productId = productId;
		this.description = description;
		this.company = company;
		this.title = title;
		this.variants = variants;
	}

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

	public void setVariants(List<VariantsItem> variants){
		this.variants = variants;
	}

	public List<VariantsItem> getVariants(){
		return variants;
	}

	@Override
 	public String toString(){
		return 
			"ProductsItem{" + 
			"product_id = '" + productId + '\'' + 
			",description = '" + description + '\'' + 
			",company = '" + company + '\'' + 
			",title = '" + title + '\'' + 
			",variants = '" + variants + '\'' + 
			"}";
		}
}
