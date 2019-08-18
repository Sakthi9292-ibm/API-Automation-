package restAPITesting;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.testng.annotations.DataProvider;

import jxl.Cell;
import jxl.CellType;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class DataProviderclass {

	
	public static String[][] read() throws IOException, BiffException {

		/*
		 * file
		 * workbook
		 * sheet
		 * create object
		 * iterate thru rows and columns 
		 * 
		 */
		File inputWorkbook = new File("src\\main\\resources\\DataProvider.xls"); // Create input excel

		Workbook w = Workbook.getWorkbook(inputWorkbook);
		// Get the first sheet
		Sheet sheet = w.getSheet(0);

		int u = w.getSheet(0).getRows();

		String[][] data = new String[sheet.getRows()-1][sheet.getColumns()];

		int column1 = sheet.getColumns();

		for (int i = 1; i < sheet.getRows(); i++) {

			for (int g = 0; g < sheet.getColumns(); g++) {

				Cell cell = sheet.getCell(g, i);

				CellType type = cell.getType();

				data[i-1][g] = cell.getContents();

			}

		}
		return data;

	}

}
