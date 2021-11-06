package com.retail.base;
import java.util.*;

public class CategoryList {
	
	
	public Map<String,List<Product>>  productMap;
	public List<Product> productList;
	public String categoryName;
	

	public CategoryList(){
		System.out.println("In constructor of Category List");
		if(productMap == null){
			
			System.out.println("As productMap is null so will create a new one");
            productMap  = new HashMap<String,List<Product>>();
		}
	};
	
	public void createNewList(String categoryName){
		System.out.println("In create new list class.adding key to map"+categoryName+" and will also instantiate"
				+ " an empty list and add it to our map with key as "+categoryName);
		this.categoryName = categoryName;
		productList = new ArrayList<Product>();
		productMap.put(categoryName,productList);
		
	}
	
   public Map<String,List<Product>> addProduct(Product p ){
	   
		System.out.println("In addProduct method . Here we will add product p to out list");

		productList.add(p);
		return productMap;
		
	}
}
