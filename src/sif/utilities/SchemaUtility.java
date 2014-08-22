package sif.utilities;

import java.io.InputStream;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.SAXException;

import sif.IO.xml.SifMarshaller;

/**
 * Holds the information for the current schema files used for validation, see the respective
 * {@link #setReportValidation(Marshaller)} and {@link #setRequestValidation(Unmarshaller)}
 * @author Wolfgang Kraus
 *
 */
/*
 * Both the SAXException and the NPE should be checked before rolling out, so they don't need
 * to be propagated.
 */
public class SchemaUtility {
	public static final String SCHEMA_LANGUAGE = "http://www.w3.org/2001/XMLSchema";
	private static final String SCHEMA_PREFIX = "sif/model/schema/SpRuDeL",
			SCHEMA_VERSION = "1_4_",
			SCHEMA_REQUEST = "Request.xsd",
			SCHEMA_REPORT = "Report.xsd";

	/**
	 * Sets the validation for the given marshaller
	 * @param m to get the validation schema
	 */
	public static void setReportValidation(Marshaller m){
		try {
			SchemaFactory fac = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
			InputStream is = SifMarshaller.class.getClassLoader().getResourceAsStream(
					SCHEMA_PREFIX + SCHEMA_VERSION + SCHEMA_REPORT
					);
			StreamSource ss = new StreamSource(is);
			Schema s = fac.newSchema(ss);
			m.setSchema(s);
		} catch (NullPointerException e){
			// file not found
			System.err.println("Schema file for the report not found");
		} catch (SAXException e) {
			// Error during parsing the schema file
			System.err.println("Schema file for the report could not be parsed:");
			e.printStackTrace();
		}
	}
	
	/**
	 * Sets the validation for the given unmarshaller
	 * @param unmarshaller to get the validation schema
	 */
	public static void setRequestValidation(Unmarshaller unmarshaller){
		try {
			SchemaFactory fac = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
			InputStream is = SifMarshaller.class.getClassLoader().getResourceAsStream(
					SCHEMA_PREFIX + SCHEMA_VERSION + SCHEMA_REQUEST
					);
			StreamSource ss = new StreamSource(is);
			Schema s = fac.newSchema(ss);
			unmarshaller.setSchema(s);
		} catch (NullPointerException e){
			// file not found
			System.err.println("Schema file for the request not found");
		} catch (SAXException e) {
			// Error during parsing the schema file
			System.err.println("Schema file for the request could not be parsed:");
			e.printStackTrace();
		}
	}
}
