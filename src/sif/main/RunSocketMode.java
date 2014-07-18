package sif.main;

import java.io.BufferedWriter;
import java.io.File;
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

public class RunSocketMode{
	private int clientPort;

	public RunSocketMode(int port){
		clientPort = port;
	}

	public void blockingListening() {
		Socket clientSocket = null;

		try {

			clientSocket = new Socket(InetAddress.getLoopbackAddress(),
					clientPort);


			while (true) {

				/*
				 * Read the policy file
				 */
				String policyFile = Utils.readString(clientSocket);

				if (Application.isDebug()) {
					BufferedWriter out = new BufferedWriter(new FileWriter(new File("C:\\Spreadsheet Inspection Framework\\dynPol.xml")));
					out.write(policyFile);
					out.close();
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
				if (Application.isDebug()){
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
			if (Application.isDebug()){
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
						e1.printStackTrace();
					}
				}
				new Thread(con).start();
			} else {
				System.exit(Application.APPLICATIONERROR);
			}
		}
	}

}