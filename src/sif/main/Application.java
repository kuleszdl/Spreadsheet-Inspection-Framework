package sif.main;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.net.InetAddress;
import java.net.Socket;

import sif.IO.ReportFormat;
import sif.IO.xml.SifMarshaller;
import sif.frontOffice.FrontDesk;
import sif.model.policy.DynamicPolicy;
import sif.model.policy.policyrule.implementations.FormulaComplexityPolicyRule;
import sif.model.policy.policyrule.implementations.NoConstantsInFormulasPolicyRule;
import sif.model.policy.policyrule.implementations.ReadingDirectionPolicyRule;

/***
 * This is the application class of the Spreadsheet Inspection Framework.
 * 
 * 
 * @author Ehssan Doust
 * 
 */
public class Application {
	private static final boolean DEBUG = false;
	/**
	 * @param args
	 * 
	 */
	public static void main(String[] args) {

		// Check for correct command line parameters
		if (args != null && args.length == 1) {

			try {

				int port = Integer.parseInt(args[0]);

				// Try and open a socket connection to the server
				Socket clientSocket = null;

				try {

					clientSocket = new Socket(InetAddress.getLoopbackAddress(),
							port);

					while (true) {

						/*
						 * Read the policy file
						 */
						String policyFile = Utils.readString(clientSocket);

						/*
						 * Read the spreadsheet file
						 */
						byte[] spreadsheetContent = Utils
								.readBytes(clientSocket);
						File spreadsheetFile = Utils
								.writeToTempFile(spreadsheetContent);

						/*
						 * Generate the report
						 */
						String requestName = "Programmatic Request";

						FrontDesk desk = FrontDesk.getInstance();

						DynamicPolicy policy = SifMarshaller
								.unmarshal(new StringReader(policyFile));

						policy.add(new NoConstantsInFormulasPolicyRule());
						policy.add(new ReadingDirectionPolicyRule());
						policy.add(new FormulaComplexityPolicyRule());

						desk.createAndRunDynamicInspectionRequest(requestName,
								spreadsheetFile, policy);

						String xmlReport = desk
								.createInspectionReport(ReportFormat.XML);

						/*
						 * Send the report
						 */
						Utils.writeString(clientSocket, xmlReport);

					}

				} catch (IOException e){
					// silently ignore the IOException when it is closed
				} catch (Throwable e) {
					if (DEBUG){
						// show a window with the exceptions from the application
						e.printStackTrace();
						for (Throwable e2 : e.getSuppressed())
							e2.printStackTrace();
						DebugConsole con = new DebugConsole();
						con.addStackTrace(e);
						if (e.getCause() != null)
							con.addStackTrace(e.getCause());
						for (Throwable e2 : e.getSuppressed()){
							con.addStackTrace(e2);
							if (e2.getCause() != null)
								con.addStackTrace(e2.getCause());
						}
						new Thread(con).start();
					}
				}

			} catch (NumberFormatException e) {

				// Parameter was not an integer, just exit silently
				// as we do not expect a human to call this jar
				// System.out.println("Wrong parameter");

			}

		} else {

			// System.out.println("No parameters or wrong number of parameter");

		}

	}
}
