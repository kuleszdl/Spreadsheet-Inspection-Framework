/**
 * 
 */
package sif.IO.xml;

import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.adapters.XmlAdapter;

import sif.model.policy.policyrule.dynamicConditions.ETernaryRelation;

/**
 * This does (un)marshalling of Enumerations manually. This is necessary because 
 * in some JVM Versions the JAXB implementation contains errors causing 
 * Enumerations not to be correctly (un)marshalled.
 * 
 * @author Manuel Lemcke
 *
 */
public class TernaryRelationAdapter extends XmlAdapter<String, ETernaryRelation> {

    @Override
    public String marshal(ETernaryRelation binaryRelation) throws Exception {
        return binaryRelation.name();
    }

    @Override
    public ETernaryRelation unmarshal(String string) throws Exception {
        try {
            return ETernaryRelation.valueOf(string);
        } catch(Exception e) {
            throw new JAXBException(e);
        }
    }

}
