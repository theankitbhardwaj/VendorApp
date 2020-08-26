package com.ganesh.vendorapp.models;

import java.util.List;

public class VariantsItem{
	private List<String> image;
	private int variantId;
	private int quantity;
	private int price;
	private String variantName;

	public void setImage(List<String> image){
		this.image = image;
	}

	public List<String> getImage(){
		return image;
	}

	public void setVariantId(int variantId){
		this.variantId = variantId;
	}

	public int getVariantId(){
		return variantId;
	}

	public void setQuantity(int quantity){
		this.quantity = quantity;
	}

	public int getQuantity(){
		return quantity;
	}

	public void setPrice(int price){
		this.price = price;
	}

	public int getPrice(){
		return price;
	}

	public void setVariantName(String variantName){
		this.variantName = variantName;
	}

	public String getVariantName(){
		return variantName;
	}

	@Override
 	public String toString(){
		return 
			"VariantsItem{" + 
			"image = '" + image + '\'' + 
			",variant_id = '" + variantId + '\'' + 
			",quantity = '" + quantity + '\'' + 
			",price = '" + price + '\'' + 
			",variant_name = '" + variantName + '\'' + 
			"}";
		}
}
