package sif.main;

import java.io.File;
import java.util.Arrays;

import sif.IO.ReportFormat;

/***
 * This is the application class of the Spreadsheet Inspection Framework.
 * 
 * 
 * @author Ehssan Doust
 * 
 */
public class Application {
	private static boolean DEBUG = false;
	private static File parentFolder = null;
	private static final String DEBUGFILENAME = "debug";
	private static final String MODESOCKET = "socket";
	private static final String MODEFILE = "file";
	
	/**
	 * Wheter debug messages should be shown
	 * @return
	 */
	public static boolean isDebug(){
		return DEBUG;
	}
	
	/**
	 * Tries and sets the parent folder of the jar
	 */
	private static void checkParentFolder(){
		try {
			parentFolder = new File(
					Application.class.getProtectionDomain()
					.getCodeSource().getLocation()
					.toURI().getPath()
			).getParentFile();
		} catch (Exception e) {
			// Security / URI or other exceptions
			e.printStackTrace();
		}
	}
	
	/**
	 * Checks for a file "debug" in the jar folder and sets the DEBUG flag when appropriate
	 */
	private static void checkDebug(){
		File debugFile = new File(parentFolder, DEBUGFILENAME);
		if (debugFile.exists()){
			DEBUG = true;
		}
	}
	/**
	 * @param args
	 * 
	 */
	public static void main(String[] args) {
		checkParentFolder();
		checkDebug();
		if (args.length < 2){
			printUsageAndExit("Not enough parameters.");
		}


		if (args[0].equalsIgnoreCase(MODESOCKET)){
			try {
				int port = Integer.parseInt(args[1]);
				RunSocketMode client = new RunSocketMode(port);
				client.blockingListening();
			} catch (NumberFormatException e) {
				e.printStackTrace();
				printUsageAndExit("Error while parsing the portnumber");
			}
		} else if (args[0].equalsIgnoreCase(MODEFILE)){
			String errors = "";
			if (args.length < 4){
				errors += "Not enough parameters for the file mode.";
				printUsageAndExit(errors);
			}
			ReportFormat format = null;
			try {
				format = ReportFormat.valueOf(args[1].toUpperCase());
			} catch (IllegalArgumentException e){
				errors += "Unknown report format, currently supported formats are: ";
				errors += Arrays.toString(ReportFormat.values()) + "\n";
			}
			File policyFile = new File(args[2]);
			if (!policyFile.exists()){
				errors += "Policy file not found at " + args[2] + "\n";
			}
			
			File spreadsheetFile = new File(args[3]);
			if (!spreadsheetFile.exists()){
				errors += "Spreadsheet file not found at " + args[3] + "\n";
			}
			
			if (!errors.isEmpty()){
				printUsageAndExit(errors);
			}
			
			try {
				String report = RunFileMode.execute(format, policyFile, spreadsheetFile);
				System.out.println(report);
			} catch (Exception e) {
				e.printStackTrace();
				for (Throwable t : e.getSuppressed()){
					t.printStackTrace();
				}
			}
			
		}
		

	}
	
	/**
	 * Prints the reason to the standard output followed by the usage syntax and exits with the
	 * exit code -1
	 * @param reason why it was invalid
	 */
	private static void printUsageAndExit(String reason){
		String sb = "";
		if (reason != null){
			sb += reason + "\n";
		}	
		sb += "Usage: sif [MODE] [PARAMETERS]. "
		+ "Currently supported modes:\n"
		+ "sif socket [portnumber]\n"
		+ "sif file [html | xml] [path/to/policyFile] [path/to/spreadsheetFile]\n";
		if (isDebug()){
			sb += "To disable the debug mode remove or rename the file called 'debug' in the "
					+ "same folder as the jar";
		} else {
			sb += "To enable the debug mode place a file called 'debug' in the same folder as the jar";
		}
		System.out.print(sb);
		System.exit(-1);
	}
}
