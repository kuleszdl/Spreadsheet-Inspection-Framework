package sif.utilities;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import sif.main.Application;

/**
 * Main facility for internationalization of strings. Uses standard Java Resource bundles.
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
	 * Returns the translated string for the given key or the provided fallback, if the key cannot be found. 
	 * 
	 * @param key the translation key to search for
	 * @param fallback the fallback to rely on, if the key cannot be found
	 * @return
	 */
	public String tl(String key, String fallback) {
		String translated = bundle.getString(key);
		if (translated!=null) {
			return translated;
		} else {
			return fallback;
		}
	}

}
