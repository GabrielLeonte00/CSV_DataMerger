package app;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * File class which contains functions utilized in the main file class
 * 
 * @author GABRIEL LEONTE
 *
 */
public class functions {

	/**
	 * The function importCSV_File checks the availability of the file and imports
	 * it
	 * 
	 * @param input The scanner for the console
	 * @return The imported CSV file
	 */
	public static File importCSV_File(Scanner input) {
		File csv_File = null;

		Path path = Paths.get(input.nextLine());
		String aux = path.toString();
		String file_type = aux.substring(aux.length() - 3); // get the last 3 characters
		while (Files.notExists(path) || !file_type.equals("csv")) { // check if the file exist or if it's a CSV file
			System.out.println("Incorrect path/type, insert the path to the csv file:");
			// retry until a valid path to an existing CSV is inserted
			path = Paths.get(input.nextLine());
			aux = path.toString();
			file_type = aux.substring(aux.length() - 3);
		}

		csv_File = new File(path.toString()); // import a valid CSV file

		return csv_File;// return the CSV file
	}

	/**
	 * The function createData is used to import data from a CSV file
	 * 
	 * @param csv_File The CSV file from which we will extract data
	 * @return A list of string arrays which contains the extracted data
	 */
	public static List<String[]> createData(File csv_File) {

		List<String[]> data = new ArrayList<String[]>();

		try (BufferedReader br = new BufferedReader(new FileReader(csv_File.getAbsolutePath()))) {
			String line;
			while ((line = br.readLine()) != null) { // read a line in the file and add it in the list
				String[] values = line.split(",");
				data.add(values);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return data; // return list of data
	}

	/**
	 * The function findIndexOfColumn finds the index of a column
	 * 
	 * @param list   The data list of a CSV file
	 * @param column The column which we want to omit
	 * @return The index of the common column
	 */
	public static int findIndexOfColumn(String[] list, String column) {
		for (int i = 0; i < list.length; i++) {
			if (list[i].equals(column)) // check if i is the index of our common column
				return i;
		}
		return -1;
	}

	/**
	 * The function mergedObject merges data from 2 String arrays omitting common
	 * data
	 * 
	 * @param data1    First array of data
	 * @param data2    Second array of data
	 * @param indexes2 The indexes which will be omitted in the second array
	 * @return A merged array of Strings from data1 and data2
	 */
	public static String[] mergedObject(String[] data1, String[] data2, int[] indexes2) {
		List<String> mergedObjectArray = new ArrayList<String>();
		for (int i = 0; i < data1.length; i++) {
			mergedObjectArray.add(data1[i]); // add the data from the first array
		}
		for (int i = 0; i < data2.length; i++) { // add the data from the second array
			int OK = 1;
			for (int k : indexes2) { // omit the common data from the second array
				if (i == k) {
					OK = 0;
					break;
				}
			}
			if (OK == 1) {
				mergedObjectArray.add(data2[i]); // add data from the second array
			}
		}
		String[] mergedObject = new String[mergedObjectArray.size()];
		for (int i = 0; i < mergedObjectArray.size(); i++) {
			mergedObject[i] = mergedObjectArray.get(i); // converts the list into an array
		}
		return mergedObject;
	}

	/**
	 * The function organizeData extracts the common data from the 2 CSV files and
	 * the different data: the data which exist in the first CSV file but not in the
	 * second one and the data which exist in the second CSV file but not in the
	 * first one. Also gives the option to avoid or not duplicate data, which means
	 * that in a CSV file there are rows of data which have the common columns the
	 * same but the other columns are different
	 * 
	 * @param data1          Data from the first CSV file
	 * @param data2          Data from the second CSV file
	 * @param mergedData     Merged data from both CSV file, omitting the common
	 *                       data
	 * @param common_columns The header of columns for the mergedData
	 * @param indexes1       Indexes of the common columns in the first CSV file
	 * @param indexes2       Indexes of the common columns in the second CSV file
	 * @param diffData1      Copy of data1 used for differences in the first CSV
	 *                       file
	 * @param diffData2      Copy of data2 used for differences in the second CSV
	 *                       file
	 * @param duplicates     This variable will decide if duplicates will be treated
	 *                       or not;
	 */
	static void organizeData(List<String[]> data1, List<String[]> data2, List<String[]> mergedData,
			List<String> common_columns, int[] indexes1, int[] indexes2, List<String[]> diffData1,
			List<String[]> diffData2, int duplicates) {
		for (int i = 0; i < data1.size(); i++) {
			for (int j = 0; j < data2.size(); j++) {
				int OK = 1;
				// check if all common columns have the same data
				for (int k = 0; k < common_columns.size(); k++) {
					// check 2 common columns from the CSV files if they have the same data
					if (!(data1.get(i)[indexes1[k]].equals(data2.get(j)[indexes2[k]]))) {
						OK = 0;
						break;
					}

					// check if valid data was found
					// check if duplicates should be treated
					if (OK == 1 && duplicates == 1) {
						// create the String array for the merged data for the common CSV file
						String[] mergedColumnsData = functions.mergedObject(data1.get(i), data2.get(j), indexes2);
						mergedData.add(mergedColumnsData); // add data in the mergedData
						diffData1.remove(data1.get(i)); // remove data from the copy of data1
						diffData2.remove(data2.get(j)); // remove data from the copy of data2
						break;
					}

				}
				// check if valid data was found
				// check if duplicates should not be treated
				if (OK == 1 && duplicates == 0) {
					// create the String array for the merged data for the common CSV file
					String[] mergedColumnsData = functions.mergedObject(data1.get(i), data2.get(j), indexes2);
					mergedData.add(mergedColumnsData); // add data in the mergedData
					diffData1.remove(data1.get(i)); // remove data from the copy of data1
					diffData2.remove(data2.get(j)); // remove data from the copy of data2
					break;
				}
			}
		}
	}

	/**
	 * The function createStringLine merges a String array into a simple String
	 * 
	 * @param row An array of strings with data on a row from each column
	 * @return A string with all data merged and with the separator ',' in between
	 *         them
	 */
	static String createStringLine(String[] row) {

		String line = "";

		for (int i = 0; i < row.length; i++) {
			if (i != row.length - 1)
				line = new String(line + row[i] + ','); // add data and the delimiter ','
			else
				line = new String(line + row[i]); // add the last data
		}

		return line; // returns the String line
	}

	/**
	 * The function createNewFile_CSV will creates a new CSV file
	 * 
	 * @param fileName The name of the file which will be created
	 * @param header   The row which contains the header of the CSV file
	 * @param data     The data which will be inserted into the CSV file
	 */
	public static void createNewFile_CSV(String fileName, String[] header, List<String[]> data) {
		// combine the fileName with other strings to create the path for the CSV file
		String path = "src\\results\\" + fileName + ".csv";
		File csvFile = new File(path); // create a new file using the String path from the previous line
		try {
			FileWriter csvFileWriter = new FileWriter(csvFile);
			csvFileWriter.write(createStringLine(header)); // add the header row in the CSV file
			for (String[] row : data) {
				csvFileWriter.write("\n" + createStringLine(row)); // add data to the CSV file
			}
			csvFileWriter.close();
		} catch (IOException e) {
			// Auto-generated catch block
			e.printStackTrace();
		}
	}
}
