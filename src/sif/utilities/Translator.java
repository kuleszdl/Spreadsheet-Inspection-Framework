package sif.utilities;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import sif.main.Application;

/**
 * A translator which makes use of standard Java Resource bundles
 * 
 * @author kuleszdl
 *
 */
public class Translator {
	
	private static Logger logger = Logger.getLogger(Translator.class);
	
	public static final Translator instance = new Translator();
	
	private ResourceBundle bundle = null;
	
	/**
	 * private constructor - class has to be accessed via the instance variable
	 */
	private Translator() {
		// Initialize the resource bundle
		
		String baseName = Application.RESOURCE_PREFIX;
		
	    try
	    {
	      bundle = ResourceBundle.getBundle( baseName );
	    }
	    catch ( MissingResourceException e ) {
	    	logger.error("Error setting up the Translator: ", e );
	    }
	}
	
	/**
	 * Returns the translated string for the given key
	 * 
	 * @param key
	 * @return
	 */
	public String tl(String key) {
		return bundle.getString(key);
	}

}
