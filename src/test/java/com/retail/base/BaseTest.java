package com.retail.base;

import static io.restassured.RestAssured.given;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.StringWriter;
import java.util.Properties;

import org.apache.commons.io.output.WriterOutputStream;
import org.testng.Reporter;
//import org.apache.commons.io.output.WriterOutputStream;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.asserts.SoftAssert;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.retail.reporting.ExtentManager;
//import com.aventstack.extentreports.ExtentReports;
//import com.aventstack.extentreports.ExtentTest;
import com.retail.util.DataUtil;
//import com.retail.util.ExtentManager;
import com.retail.util.ReadExcel;
import com.retail.util.ReadJsonData;

import io.restassured.RestAssured;

public class BaseTest {

	public static String sessionId = "asd";
	ReadExcel xls;
	ReadJsonData jsonData;
	public static SoftAssert softAssert = new SoftAssert();
	public Properties testProp;

	public ExtentReports rep;
	public static String reportFolder;
	public ExtentTest test;
	public int iteration;
	String testname;
	public static String jsonDataPath;

	public StringWriter requestWriter;
	public PrintStream requestCapture;

	@BeforeTest
	public void init() {
		try {
			testProp = new Properties();
			FileInputStream fs = new FileInputStream(
					System.getProperty("user.dir") + "//src//test//resources//project.properties");
			testProp.load(fs);
		} catch (Exception e) {
			e.printStackTrace();
		}

		jsonDataPath = System.getProperty("user.dir") + "//src//test//resources//json//Data.json";

		// if we weant to read data from excel file
		// xls = new
		// ReadExcel(System.getProperty("user.dir")+"//src//test//resources//xls//"+testProp.getProperty("xlspath"));

		jsonData = new ReadJsonData();
		RestAssured.baseURI = testProp.getProperty("baseurl");
		testname = this.getClass().getSimpleName().toLowerCase();
		RestAssured.basePath = testProp.getProperty(testname);

		// rep = ExtentManager.getInstance(testProp.getProperty("reportPath"));

		rep = ExtentManager.getReports(); // made object of rep

	}

	@BeforeMethod
	public void before() {
		iteration++;
		String newTestName = testname.substring(0, 1).toUpperCase() + testname.substring(1, testname.length());
		test = rep.createTest(newTestName + " " + iteration);
		requestWriter = new StringWriter();
		requestCapture = new PrintStream(new WriterOutputStream(requestWriter), true);
	}

	@AfterMethod
	public void after() {
		rep.flush();
	}

	@DataProvider
	public Object[][] getData() {
		return DataUtil.getData(xls, this.getClass().getSimpleName());
	}

	@DataProvider
	public Object[][] getJsonData() {
		Object[][] obj = null;
		try {
			obj = jsonData.getTestData(jsonDataPath, this.getClass().getSimpleName());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return obj;
	}

	protected void reportFailure(String errorMsg, boolean stopOnFailure) {

		test.log(Status.FAIL, errorMsg);// failure in extent reports

		softAssert.fail(errorMsg);

		if (stopOnFailure) {
			softAssert.assertAll();
		}

	}

	public void log(String msg) {
		test.log(Status.INFO, msg);
	}
	
	public void testPass(){
		test.log(Status.PASS,this.getClass().getSimpleName() + " has passed");
	}

	public void addRequestToLink(String message, String fileName, String Content)

	{

		try {
			new File(reportFolder + "\\log\\" + fileName + ".html").createNewFile();
			FileWriter fw = new FileWriter(reportFolder + "\\log\\" + fileName + ".html");
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(Content);
			bw.close();
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
