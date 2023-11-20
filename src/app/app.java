package app;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * The main file class in which the program will start
 * 
 * @author GABRIEL LEONTE
 * 
 */
public class app {

	public static void main(String[] args) throws FileNotFoundException {

		File csv_no1, csv_no2;

		Scanner input = new Scanner(System.in); // scanner to get user's input in the console

		System.out.println("Insert path to the first CSV file (example: D:\\Eclipse Workspace\\CSV_Extractor\\src\\testCSV\\books1.csv):");
		csv_no1 = functions.importCSV_File(input); // import the first CSV file
		System.out.println("Insert path to the second CSV file(example: C:\\Users\\[user]\\Desktop\\[file name].csv):");
		csv_no2 = functions.importCSV_File(input); // import the second CSV file

		input.close();

		List<String[]> data1 = functions.createData(csv_no1); // import the data from the first CSV file
		List<String[]> data2 = functions.createData(csv_no2); // import the data from the second CSV file
		List<String[]> mergedData = new ArrayList<String[]>(); // prepare for the merged data

		String[] columns1 = null, columns2 = null;
		
		try {
			columns1 = data1.get(0); // get the header columns of the first CSV file
			data1.remove(0); // remove the header columns from the data1

			columns2 = data2.get(0); // get the header columns of the second CSV file
			data2.remove(0); // remove the header columns from data2

		List<String[]> diffData1 = new ArrayList<String[]>(data1); // copy data1
		List<String[]> diffData2 = new ArrayList<String[]>(data2); // copy data2

		List<String> common_columns = new ArrayList<String>(); // preparation for the common columns of the 2 CSV files

		for (String column1 : columns1) { // check columns and add the common columns of the 2 CSV files
			for (String column2 : columns2) {
				if (column1.equals(column2)) {
					common_columns.add(column1);
				}
			}
		}

		int[] indexes1 = new int[common_columns.size()]; // INT array of indexes for the common columns in data1
		int[] indexes2 = new int[common_columns.size()]; // INT array of indexes for the common columns in data2

		// add the indexes in which the common columns are located for data1 and data2
		for (int i = 0; i < common_columns.size(); i++) {
			indexes1[i] = functions.findIndexOfColumn(columns1, common_columns.get(i));
			indexes2[i] = functions.findIndexOfColumn(columns2, common_columns.get(i));
		}

		// create an array of the merged columns
		String[] mergedColumns = functions.mergedObject(columns1, columns2, indexes2);
		// organize the data by extracting the different and common data
		functions.organizeData(data1, data2, mergedData, common_columns, indexes1, indexes2, diffData1, diffData2);

		// create csv files which contains the extracted data: common, diff1 and diff2
		functions.createNewFile_CSV("common", mergedColumns, mergedData);
		functions.createNewFile_CSV("diff1", columns1, diffData1);
		functions.createNewFile_CSV("diff2", columns2, diffData2);
		
		} catch (Exception e) {
			System.out.println("The data in one or both of the csv files is invalid");
		}
	}
}
