package com.retail.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ReadExcel {
	public String path;
	public FileInputStream fis = null;
	public FileOutputStream fileOut = null;
	public XSSFWorkbook workbook = null;
	public XSSFSheet sheet = null;
	public XSSFRow row = null;
	public XSSFCell cell = null;

	public ReadExcel(String path) {

		this.path = path;
		try {
			fis = new FileInputStream(path);
			workbook = new XSSFWorkbook(fis);
			sheet = workbook.getSheetAt(0);
			fis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public int getTotalRowCount(XSSFWorkbook workbook, String sheetName) {

		XSSFSheet sheet = workbook.getSheet(sheetName);
		if (sheet == null) {
			return -1;
		}

		int rows = sheet.getLastRowNum() + 1;
		return rows;

	}

	// Below method is to find data at a particular row and cell.
	// we are passed a colNum and rowNum as integers for that.

	public String getCellData(String sheetName, int colNum, int rowNum) {
		try {

			// So we first check if colNum passed is not -1 or not less than 0
			// .If yes return empty String
			if (colNum < 0)
				return "";

			// Same way check for rowNum passed
			if (rowNum < 0)
				return "";
			// Then check if sheetName passed exists. Else return empty String
			XSSFSheet sheet = workbook.getSheet(sheetName);
			if (sheet == null) {
				return "";
			}

			// Then don’t know why we are checking for rowNum-1.
			// Maybe we are checking because we are passing row no starting from
			// 1 in called method
			// But in actual excel file row starts from 0.
			// We are checking if row exists. If not return empty String. But
			// why checking rowNum-1 . No idea.
			Row row = sheet.getRow(rowNum - 1);
			if (row == null)
				return "";

			// Now we have rowObject. Lets create cell object for colNum-1
			// if its null that is col does not exist then return empty String
			// Same logics for colNum-1 as in calling method we pass col num as
			// 1 ,but in excel reader we read
			// from col 0

			Cell cell = row.getCell(colNum - 1);
			if (cell == null)
				return "";

			// Then once we have check that row object and cell object are not
			// null ,then we get the data and if data
			// is String return String, if its numeric return numeric, if its
			// Blank return blank.

			if (cell.getCellType() == CellType.STRING) {
				return cell.getStringCellValue();
			}

			else if (cell.getCellType() == CellType.NUMERIC || cell.getCellType() == CellType.FORMULA) {
				return String.valueOf(cell.getNumericCellValue());
			}

			else if (cell.getCellType() == CellType.BLANK) {
				return "";
			}

		} catch (Exception e) {
			// if we reached the catch block by any chance ,return empty String
			e.printStackTrace();
		}
		return "";

	}

	public int getRowCount(String sheetName) {

		try {
			int index = workbook.getSheetIndex(sheetName);
			if (index == -1)
				return 0;
			else {
				XSSFSheet sheet = workbook.getSheetAt(index);
				int number = sheet.getLastRowNum() + 1;
				return number;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}

	}

}