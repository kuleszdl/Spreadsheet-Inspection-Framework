package sif.main;

import java.io.File;
import java.util.Arrays;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import sif.IO.ReportFormat;
import sif.IO.spreadsheet.InvalidSpreadsheetFileException;

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
	private static Logger logger = Logger.getLogger(Application.class);

	private static final String DEBUGFILENAME = "debug";
	private static final String MODESOCKET = "socket";
	private static final String MODEFILE = "file";
	public static final int NOTENOUGHPARAMETERS = -1,
			INVALIDPARAMETER = -2,
			APPLICATIONERROR = -3;
	/**
	 * Whether debug messages should be shown
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
			logger.info("", e);
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
		BasicConfigurator.configure();
		Logger.getRootLogger().setLevel(Level.ERROR);
		if (args.length < 2){
			printUsageAndExit("Not enough parameters.", NOTENOUGHPARAMETERS);
		}


		if (args[0].equalsIgnoreCase(MODESOCKET)){
			try {
				int port = Integer.parseInt(args[1]);
				RunSocketMode client = new RunSocketMode(port);
				client.blockingListening();
			} catch (NumberFormatException e) {
				logger.info("", e);
				printUsageAndExit("Error while parsing the portnumber", INVALIDPARAMETER);
			}
		} else if (args[0].equalsIgnoreCase(MODEFILE)){
			String errors = "";
			if (args.length < 4){
				errors += "Not enough parameters for the file mode.";
				printUsageAndExit(errors, NOTENOUGHPARAMETERS);
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
				printUsageAndExit(errors, INVALIDPARAMETER);
			}

			try {
				String report = RunFileMode.execute(format, policyFile, spreadsheetFile);
				logger.info(report);
			} catch (InvalidSpreadsheetFileException e) {
				logger.info("", e);
				for (Throwable t : e.getAdditional()){
					logger.info("", t);
				}
				System.exit(APPLICATIONERROR);
			} catch (Exception e) {
				logger.info("", e);
				System.exit(APPLICATIONERROR);
			}

		} else {
			printUsageAndExit("Wrong parameters", INVALIDPARAMETER);
		}


	}

	/**
	 * Prints the reason to the standard output followed by the usage syntax and exits with the
	 * exit code -1
	 * @param reason why it was invalid
	 */
	private static void printUsageAndExit(String reason, int code){
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
		System.exit(code);
	}
}
