package sif.IO.xml;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.Iterator;
import java.util.Scanner;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import org.xml.sax.SAXException;
import sif.model.policy.DynamicPolicy;
import sif.model.policy.PolicyList;
import sif.model.policy.policyrule.AbstractPolicyRule;
import sif.utilities.SchemaUtility;

/**
 * Utility class for (un)marshaling dynamic policy specifications
 * 
 * @author Manuel Lemcke, Ehssan Doust
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
		PolicyList dynPolicy = unmarshal(file);

		Iterator<?> ruleIterator = dynPolicy.getDynamicPolicy().getPolicyRules().values()
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
	 * @param xmlReader
	 *            The XML specification to be unmarshalled
	 * @return The result of unmarshalling
	 * @throws JAXBException
	 *             If there was a problem while unmarshalling.
	 * @throws IOException
	 * @throws SAXException If the Xml stream was not valid
	 */
	@SuppressWarnings("unchecked")
	public static PolicyList unmarshal(StringReader xmlReader)
			throws JAXBException, IOException, SAXException {
		PolicyList dynPolicy = null;

		JAXBContext jc = JAXBContext.newInstance("sif.model.policy");
		Unmarshaller unmarshaller = jc.createUnmarshaller();
		
		
		SchemaUtility.setRequestValidation(unmarshaller);
		
		Object o = unmarshaller.unmarshal(xmlReader);

		// Wenn ein JAXBElement erzeugt wurde und dies eine DynamicRule ist
		if (o instanceof JAXBElement
				&& ((JAXBElement<?>) o).getDeclaredType().equals(
						PolicyList.class)) {

			dynPolicy = ((JAXBElement<PolicyList>) o).getValue();

		} else if (o instanceof PolicyList) {

			dynPolicy = (PolicyList) o;

		} else {

			throw new JAXBException(
					"The unmarshalled Element was neither a DynamicPolicy"
							+ " nor a JAXBElement.");

		}
		return dynPolicy;
	}

	/**
	 * Initiates a JAXB Unmarshaler with the correct context for SIF Policies
	 * and calls it's unmarshal method.
	 * 
	 * @param file
	 *            The XML specification to be unmarshalled
	 * @return The result of unmarshalling
	 * @throws JAXBException
	 *             If there was a problem while unmarshalling.
	 * @throws IOException
	 * @throws SAXException 
	 */
	/*
	 * The new StringReader... will be closed through the unmarshaller in the next function call
	 */
	@SuppressWarnings("resource")
	public static PolicyList unmarshal(File file) throws JAXBException, IOException, SAXException {

		return unmarshal(new StringReader(new Scanner(file).useDelimiter("\\Z")
				.next()));

	}

	/**
	 * Marshals the {@link DynamicPolicy} object to a XML file. If a schema
	 * location is provided it will be included in the XML file.
	 * 
	 * @param policy
	 * @param file
	 * @param shemaLocation
	 *            If not null or "" will be included as xsi:schemaLocation.
	 * @throws JAXBException
	 * @deprecated the other methods {@link #unmarshal(File)} and {@link #unmarshal(StringReader)} 
	 * 		are validating, through {@link sif.utilities.SchemaUtility}
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
