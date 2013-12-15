package sif.IO.xml;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import org.xml.sax.SAXParseException;

import sif.model.policy.DynamicPolicy;
import sif.model.policy.policyrule.AbstractPolicyRule;

/**
 * Utility class for (un)marshaling dynamic policy specifications
 * 
 * @author Manuel Lemcke
 * 
 */
public class SifMarshaller {
	/**
	 * Creates a report of all DynamicPolicyRules contained in a XML
	 * specification.
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static String createTextReport(File file) throws Exception {
		DynamicPolicy dynPolicy = unmarshal(file);

		Iterator<?> ruleIterator = dynPolicy.getPolicyRules().values()
				.iterator();
		Integer currentPos = 0;
		String outputString = "";
		AbstractPolicyRule currentRule = null;
		while (ruleIterator.hasNext()) {
			currentPos++;
			currentRule = (AbstractPolicyRule) ruleIterator.next();

			outputString += "Name: " + currentRule.getName();
		}

		return outputString;
	}

	/**
	 * Initiates a JAXB Unmarshaler with the correct context for SIF Policies
	 * and calls it's unmarshal method.
	 * 
	 * @param file
	 *            The XML specification to be unmarshalled
	 * @return The result of unmarshalling
	 * @throws SAXParseException
	 *             If there was a problem with the XML file. Often caused by
	 *             wrong encoding information in the XML header. ,
	 * @throws JAXBException
	 *             If there was a problem while unmarshalling.
	 * @throws IOException 
	 */
	@SuppressWarnings("unchecked")
	public static DynamicPolicy unmarshal(File file) throws SAXParseException,
			JAXBException, IOException {
		DynamicPolicy dynPolicy = null;

		JAXBContext jc = JAXBContext.newInstance("sif.model.policy");
// DEBUG		
//		FileInputStream stream = new FileInputStream(file);
//		StringBuffer strContent = new StringBuffer("");
//		int ch;
//		while((ch = stream.read()) != -1) {
//	        strContent.append((char)ch);			
//		}
//		stream.close();
//		
//	    System.out.println("File contents :");
//	    System.out.println(strContent);
	    
		Unmarshaller unmarshaller = jc.createUnmarshaller();
		Object o = unmarshaller.unmarshal(file);

		// Wenn ein JAXBElement erzeugt wurde und dies eine DynamicRule ist
		if (o instanceof JAXBElement
				&& ((JAXBElement<?>) o).getDeclaredType().equals(
						DynamicPolicy.class)) {
			dynPolicy = ((JAXBElement<DynamicPolicy>) o).getValue();

			// JOptionPane.showMessageDialog(null, dynPolicy.getName());
		} else if (o instanceof DynamicPolicy) {
			// JOptionPane.showMessageDialog(null, "Native DynamicPolicy: "
			// + o.getClass().getCanonicalName() + ".\r\n"
			// + "Name: " + ((DynamicPolicy)o).getName());
			dynPolicy = (DynamicPolicy) o;
		} else {
			// JOptionPane.showMessageDialog(null,
			// "Kein JAXBElement<DynamicPolicy>: " +
			// o.getClass().getCanonicalName());
			throw new JAXBException(
					"The unmarshalled Element was neither a DynamicPolicy"
							+ " nor a JAXBElement.");
		}
		return dynPolicy;
	}

	/**
	 * Marshals the {@link DynamicPolicy} object to a XML file. If a
	 * schema location is provided it will be included in the XML file.
	 * 
	 * @param policy
	 * @param file
	 * @param shemaLocation
	 * 			If not null or "" will be included as xsi:schemaLocation.
	 * @throws JAXBException
	 */
	public static void marshal(DynamicPolicy policy, File file,
			String shemaLocation) throws JAXBException {
		JAXBContext jc = JAXBContext.newInstance("sif.model.policy");

		Marshaller marshaller = jc.createMarshaller();
		if (shemaLocation != null && shemaLocation != "") {
			marshaller.setProperty(Marshaller.JAXB_SCHEMA_LOCATION,
					shemaLocation);
		}
		marshaller.marshal(policy, file);
	}
}
