package com.retail.testcases;



import static io.restassured.RestAssured.given;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;
import com.retail.base.*;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class AddProduct extends BaseTest{
		
		
	@Test(dataProvider = "getJsonData")
	public void addProduct(Hashtable<String, String> data) {


		String categoryName = data.get("categoryname");
		String productName = data.get("productname");
		String price = data.get("price");
		String quantity = data.get("quantity");

		log("Adding product named : " + productName + " under category : " + categoryName + " .Quantity to be added is "
				+ quantity + " at a price of " + price);

		Product product = new Product();
		product.setName(productName);
		product.setQuantity(quantity);
		product.setPrice(price);

		//Create a list of Products
		List<Product> productList  = new ArrayList<Product>();
		productList.add(product);
		
		Map<String,List<Product>> productDetails = new HashMap<String,List<Product>>();

		productDetails.put(categoryName,productList);
		
		ProductPojo p = new ProductPojo();
		
		p.setProductDetails(productDetails);

		
		
	Response resp = given().filter(new RequestLoggingFilter(requestCapture)).contentType(ContentType.JSON).headers("sessionid",sessionId).log().body().body(p).post();
		   
    
	//Log the response in extent reports

    test.log(Status.INFO,resp.prettyPrint());
			
   //Pass the filtered post request to the below method. Same procedure as we have used in AddCategory and LoginTest classes.

   addRequestToLink(this.getClass().getSimpleName()+" Request",this.getClass().getSimpleName()+" Request-"+iteration,requestWriter.toString());

   JsonPath extractor = resp.jsonPath();
    HashMap responseMap  = extractor.getJsonObject("productDetails");
    String actualStatus = (String) responseMap.get("status");
    

	if (!actualStatus.equals("product added successfully")) {
	    String errorMessage = (String) responseMap.get("productName");
		reportFailure("Failed to add product named   " + productName + " to db.The status is --> " + actualStatus
				+ " and the error message we are getting is --> " + errorMessage, true);

	   }
	
	//If control reaches here i.e test did not fail
	testPass();
	
	}



}
