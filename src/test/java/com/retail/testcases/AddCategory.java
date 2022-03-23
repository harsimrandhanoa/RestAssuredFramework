package com.retail.testcases;

import static io.restassured.RestAssured.given;

import java.util.Hashtable;

import org.testng.annotations.Test;

import com.retail.base.BaseTest;
import com.retail.base.Category;

import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class AddCategory extends BaseTest {

	@Test(dataProvider = "getJsonData")
	public void addCategory(Hashtable<String, String> data) {

		String categoryName = data.get("categoryname");

		Category c = new Category();

		c.setCategoryname(categoryName);

		log("Adding category named " + categoryName);

		log("The session id in AddCategory tests is  " + sessionId);

		Response resp = given().filter(new RequestLoggingFilter(requestCapture)).contentType(ContentType.JSON)
				.headers("sessionid", sessionId).log().all().when().body(c).post();

		// .log().all().when().body(s).post();

		//resp.prettyPrint();

		log(resp.prettyPrint());

		addRequestToLink(this.getClass().getSimpleName() + " Request",
				this.getClass().getSimpleName() + " Request-" + iteration, requestWriter.toString());

		JsonPath extractor = resp.jsonPath();
		String actualStatus = extractor.getString("status");

		if (!actualStatus.equals("success")) {
			String errorMessage = extractor.getString("errMsg");
			reportFailure("Failed to add category named   " + categoryName + " to db.The status is --> " + actualStatus
					+ " and the error message we are getting is --> " + errorMessage, true);

		}
		
		//If control reaches here i.e test did not fail
		testPass();

	}

}
