package com.retail.util;

import java.util.Hashtable;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class DataUtil {
	
	@DataProvider
	public static Object[][]  getData(ReadExcel xls,String testName){
		
		String sheetName="Data";
		
		int testStartRowNum=1;
		while(!xls.getCellData(sheetName,1,testStartRowNum).equals(testName)){
			testStartRowNum++;
		}
		System.out.println("Test starts from row - "+ testStartRowNum);
		
		
		int colStartRowNum = testStartRowNum+1;
		int dataStartRowNum = testStartRowNum+2;
		
		
		int rows = 0;
		
		while(!xls.getCellData(sheetName,1,dataStartRowNum+rows).equals("")){
         rows++;
		}
		
		System.out.println("Test rows of data are - "+ rows);

		
		int totalColumns = 0;
		
		int cols =1;
		
		while(!xls.getCellData(sheetName,cols+1,colStartRowNum).equals("")){
	         cols++;
		}
		
		totalColumns = cols;
		
		System.out.println("Test cols of data are - "+ totalColumns);

		Object[][] data  = new Object[rows][1];
		
		int dataRow = 0;
		
		Hashtable<String,String> table = null;
		
		for(int rNum=dataStartRowNum;rNum<dataStartRowNum+rows;rNum++){
			table = new Hashtable<String,String>();
			
			for(int cNum=1;cNum<totalColumns;cNum++){

				String key = xls.getCellData(sheetName,cNum,colStartRowNum);
				String value = xls.getCellData(sheetName,cNum,rNum);
				table.put(key, value);
			}
			data[dataRow][0] = table;
			dataRow++;
		}
		
		
	return data;

	}
	

}
