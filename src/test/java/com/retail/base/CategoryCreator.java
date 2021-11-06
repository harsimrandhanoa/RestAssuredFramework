package com.retail.base;

public class CategoryCreator {
	
	
	
public CategoryList categoriesList;
	

	public CategoryList addCategory(String catName){
		if(categoriesList==null){
		System.out.println("In addCategory method of Category list class as categorieslist is null. will instantiate an object of "
				+ "CategoryList");
		categoriesList = new CategoryList();
	}
		
		System.out.println("object of CategoryList is created so will now call createNewList method of same class");
		categoriesList.createNewList(catName);
	    return categoriesList;
	}

}
