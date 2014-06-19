package sif.main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.net.InetAddress;
import java.net.Socket;

import sif.IO.ReportFormat;
import sif.IO.xml.SifMarshaller;
import sif.frontOffice.FrontDesk;
import sif.model.policy.Policy;
import sif.model.policy.PolicyList;

/***
 * This is the application class of the Spreadsheet Inspection Framework.
 * 
 * 
 * @author Ehssan Doust
 * 
 */
public class Application {
	private static boolean DEBUG = false;
	/**
	 * @param args
	 * 
	 */
	public static void main(String[] args) {

		// Check for correct command line parameters
		if (args != null && args.length >= 1) {

			try {
				if (args.length >= 2){
					if (args[1].equalsIgnoreCase("-debug")){
						DEBUG = true;
					}
				} else if (args.length == 1){
					File debugFile = new File("C:\\Spreadsheet Inspection Framework\\debug");
					if (debugFile.exists()){
						DEBUG = true;
					}
					}
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

						if (DEBUG) {
							BufferedWriter out = new BufferedWriter(new FileWriter(new File("C:\\Spreadsheet Inspection Framework\\dynPol.xml")));
							out.write(policyFile);
							out.close();
//							return;
						}
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

						PolicyList policyList = SifMarshaller
								.unmarshal(new StringReader(policyFile));
						
						Policy policy = policyList.getDynamicPolicy();
						if (policy == null){
							policy = new Policy();
						}
						if (policyList.getFormulaComplexityRule() != null){
							policy.add(policyList.getFormulaComplexityRule());
						}
						if (policyList.getNoConstantsRule() != null){
							policy.add(policyList.getNoConstantsRule());
						}
						if (policyList.getReadingPolicyRule() != null){
							policy.add(policyList.getReadingPolicyRule());
						}
						if (policyList.getSanityPolicyRule() != null){
							policy.add(policyList.getSanityPolicyRule());
						}

						desk.createAndRunDynamicInspectionRequest(requestName,
								spreadsheetFile, policy);

						String xmlReport = desk
								.createInspectionReport(ReportFormat.XML);
						if (DEBUG){
							BufferedWriter out = new BufferedWriter(new FileWriter(new File("C:\\Spreadsheet Inspection Framework\\findings.xml")));
							out.write(xmlReport);
							out.close();
						}
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
						// Close the socket so SIFEI can continue to work, or rather fail
						// the parsing due to no root element
						// TODO: report why it failed
						if (!clientSocket.isClosed()){
							try {
								clientSocket.close();
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
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
			// debugging procedure, reads the policy.xml and runs on checking.xls
			// in the project root (eclipse) or working directory (jar)
			try {


				StringBuilder build = new StringBuilder();
				BufferedReader in = new BufferedReader(new FileReader(new File("policy.xml")));
				String akt = null;
				while ((akt = in.readLine()) != null){
					build.append(akt);
				}
				in.close();

				PolicyList policyList = SifMarshaller
						.unmarshal(new StringReader(build.toString()));

				Policy policy = policyList.getDynamicPolicy(); 
				if (policy == null){
					policy = new Policy();
				}
				if (policyList.getFormulaComplexityRule() != null){
					policy.add(policyList.getFormulaComplexityRule());
				}
				if (policyList.getNoConstantsRule() != null){
					policy.add(policyList.getNoConstantsRule());
				}
				if (policyList.getReadingPolicyRule() != null){
					policy.add(policyList.getReadingPolicyRule());
				}
				if (policyList.getSanityPolicyRule() != null){
					policy.add(policyList.getSanityPolicyRule());
				}
				
				FrontDesk desk = FrontDesk.getInstance();
				
				desk.createAndRunDynamicInspectionRequest("Debug",
						new File("checking.xls"), policy);

				String xmlReport = desk
						.createInspectionReport(ReportFormat.XML);

				
				System.out.println(xmlReport);

			} catch (Exception e){
				e.printStackTrace();
				for (Throwable t : e.getSuppressed()){
					t.printStackTrace();
				}
			}

		}

	}
}
