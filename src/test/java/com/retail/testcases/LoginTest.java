package com.retail.testcases;

import static io.restassured.RestAssured.given;

import java.util.Hashtable;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;
//import com.aventstack.extentreports.Status;
import com.retail.base.BaseTest;
import com.retail.base.Session;

import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class LoginTest extends BaseTest {

	@Test(dataProvider = "getJsonData")
	public void doLogin(Hashtable<String, String> data) {

		Session s = new Session();

		String username = data.get("username");
		String password = data.get("password");
		String expectedStatus = data.get("expectedstatus");

		log("Logging into app using username :" + username + " and  password: " + password
				+ " The expected login status is " + expectedStatus);

		s.setUsername(username);

		s.setPassword(password);

		// Response resp = given().filter(new
		// RequestLoggingFilter(requestCapture)).contentType(ContentType.JSON).log().all().when().body(s).post();

		// Without logging the request

		Response resp = given().filter(new RequestLoggingFilter(requestCapture)).contentType(ContentType.JSON).log()
				.all().when().body(s).post();

		resp.prettyPrint();

		sessionId = resp.getHeader("sessionId");

		log("The session id is " + sessionId);

		JsonPath extractor = resp.jsonPath();
		String actualStatus = extractor.getString("loginStatus");

		addRequestToLink(this.getClass().getSimpleName() + " Request",
				this.getClass().getSimpleName() + " Request-" + iteration, requestWriter.toString());
		log(resp.prettyPrint());

		if (expectedStatus.equals("Login failed")) {
			String errorMessage = extractor.getString("errMsg");

			if (!actualStatus.equals("failure") && !errorMessage.equals("invalid username/password")) {
				reportFailure(
						"Login should have failed with appropriate message but instead the message we are getting is  "
								+ errorMessage + " and the login status is " + actualStatus,
						false);
			}
		}

		if (expectedStatus.equals("Login succeed") && !sessionId.matches("^\\w+$")) {
			reportFailure("Failed to login into session as sessionId is " + sessionId, false);
		}

	}

}
