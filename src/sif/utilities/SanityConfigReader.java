package sif.utilities;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Used for debugging purposes, to use the sanityTestFacility without a policy in xml
 * @author Wolfgang Kraus
 *
 */
public class SanityConfigReader {

	public ArrayList<String> worksheets = new ArrayList<String>();
	public ArrayList<String[]> restrictions = new ArrayList<String[]>();
	public boolean warnUnknown = true;
	
	public SanityConfigReader(File spreadsheet){
		try {
			File folder = spreadsheet.getParentFile();
			String configName = spreadsheet.getName().substring(0, spreadsheet.getName().lastIndexOf("."));
			configName += "-sanityconfig.txt";
			File configFile = new File(folder, configName);
			Scanner scan = new Scanner(configFile);
			while (scan.hasNextLine()){
				String line = scan.nextLine();
				if (line.contains("Scan")){
					worksheets.add(line.split(":")[1].trim());
				} else if (line.contains("Restrict")){
					restrictions.add(line.split(":")[1].split(";"));
				} else if (line.contains("Warn")){
					warnUnknown = Boolean.parseBoolean(line.split(":")[1]);
				}
			}
			scan.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
