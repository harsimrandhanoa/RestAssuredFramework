package com.retail.base;

import java.util.List;
import java.util.Map;

public class ProductDetails {
	public Map<String, List<Product>> productDetails;

	public Map<String, List<Product>> getProductDetails() {
		return productDetails;
	}

	public void setProductDetails(Map<String, List<Product>> productdetails) {
		// System.out.println("Finally in setProductDetails method. will set the
		// Map passed as argument "
		// + " equal to the Map in this class. and the map passed as argument is
		// "+productdetails);

		this.productDetails = productdetails;
	}

}
