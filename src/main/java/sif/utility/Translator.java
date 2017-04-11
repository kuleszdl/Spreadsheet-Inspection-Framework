package sif.utility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Main facility for internationalization of strings. Uses standard Java Resource bundles.
 */
public class Translator {

    private final static Logger logger = LoggerFactory.getLogger(Translator.class);
    private final static ResourceBundle bundle = ResourceBundle.getBundle("sif");
    private final static String VAR_REG_EX = "(\\{\\})";
    
    /**
     * Returns the translated string for the given key or the provided fallback, if the key cannot be found. 
     * 
     * @param key the translation key to search for
     * @return the translated string
     */
    public static String tl(String key) {
        String translated;
        try {
            translated = bundle.getString(key);
        } catch (Exception e) {
            logger.warn("Failed to get translation for key '{}'", key);
            logger.trace("Exception: ", e);
            return key;
        }
        try {
            /*
             * a little "hack" to make utf-8 work with resources as they will be loaded
             * as ISO-8859-1 Strings as default :(
             */
            return new String(translated.getBytes("ISO-8859-1"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            logger.warn("Unsupported character encoding in properties file", e);
            return translated;
        }
    }

    public static String tl(String key, String var) {
        String translated = tl(key);
        return translated.replaceFirst(VAR_REG_EX, var);
    }

    public static String tl(String key, List<String> vars) {
        String translated = tl(key);
        for (String replacement : vars) {
            translated = translated.replaceFirst(VAR_REG_EX, replacement);
        }
        return translated;
    }


}
