package com.retail.testcases;

import static io.restassured.RestAssured.given;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;
import com.retail.base.*;

import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class AddProduct extends BaseTest{
	
	
@Test(dataProvider = "getJsonData")
public void addProduct(Hashtable<String,String> data){
	
	ProductDetails prod = new ProductDetails();
	 
	String categoryName = data.get("categoryname");
	String productName = data.get("productname");
	String price = data.get("price");
	String quantity = data.get("quantity");
	
	log("Adding product named : "+productName+ " under category : "+categoryName + " .Quantity to be added is "+quantity + " at a price of "+price);

    Product product = new Product();
	product.setName(productName);
	product.setQuantity(quantity);
	product.setPrice(price);
	
		
	
	
	CategoryCreator cc = new CategoryCreator();
	
	CategoryList catlist = cc.addCategory(categoryName);
	
	
   
	Map<String,List<Product>> productdetailsMap  = catlist.addProduct(product);
	
	prod.setProductDetails(productdetailsMap);
	
    Response resp = given().filter(new RequestLoggingFilter(requestCapture)).contentType(ContentType.JSON).headers("sessionid",sessionId).log().body().body(prod).post();
   
    
 //   Response resp = given().filter(new RequestLoggingFilter(requestCapture)).contentType(ContentType.JSON).headers("sessionid",sessionId).log().all().when().body(cc).post();

    log(resp.prettyPrint());
    System.out.println("-----------------------------------------------");
    System.out.println(resp.prettyPrint());
    System.out.println("-----------------------------------------------");

	
    addRequestToLink(this.getClass().getSimpleName()+" Request",this.getClass().getSimpleName()+" Request-"+iteration,requestWriter.toString());

 
    JsonPath extractor = resp.jsonPath();
    
    
 	String actualStatus = extractor.get("productDetails.status");

  if(!actualStatus.equals("product added successfully")){
      String errorMessage =  	extractor.get("productDetails.productName");

    reportFailure("Failed to add product  named   " +productName +" under cateogory"
         		+ categoryName + " to db.The status is --> "+actualStatus +" and the error message we are getting is --> "+errorMessage, false);
         
       }
		
}

}
