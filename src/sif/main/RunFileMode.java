package sif.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.StringReader;

import sif.IO.ReportFormat;
import sif.IO.xml.SifMarshaller;
import sif.frontOffice.FrontDesk;
import sif.model.policy.DynamicPolicy;
import sif.model.policy.Policy;
import sif.model.policy.PolicyList;

public class RunFileMode {

	
	public static String execute(ReportFormat outputFormat, File policyFile, File spreadsheetFile) throws Exception{
		StringBuilder build = new StringBuilder();
		BufferedReader in = new BufferedReader(new FileReader(policyFile));
		String akt = null;
		while ((akt = in.readLine()) != null){
			build.append(akt);
		}
		in.close();

		PolicyList policyList = SifMarshaller
				.unmarshal(new StringReader(build.toString()));

		Policy policy = policyList.getDynamicPolicy(); 
		if (policy == null){
			policy = new DynamicPolicy();
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
		
		desk.createAndRunDynamicInspectionRequest("File mode request",
				spreadsheetFile, policy);

		return desk.createInspectionReport(outputFormat);
	}

}
