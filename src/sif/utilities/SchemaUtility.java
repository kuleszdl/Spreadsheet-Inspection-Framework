package sif.utilities;

import java.io.InputStream;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.SAXException;

import sif.IO.xml.SifMarshaller;

public class SchemaUtility {
	public static final String SCHEMA_LANGUAGE = "http://www.w3.org/2001/XMLSchema";
	private static final String SCHEMA_PREFIX = "sif/model/schema/SpRuDeL",
			SCHEMA_VERSION = "1_3_",
			SCHEMA_REQUEST = "Request.xsd",
			SCHEMA_REPORT = "Report.xsd";

	public static void setReportValidation(Marshaller m) throws SAXException{
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
		}
	}
	
	public static void setRequestValidation(Unmarshaller unmarshaller) throws SAXException{
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
		}
	}
}
