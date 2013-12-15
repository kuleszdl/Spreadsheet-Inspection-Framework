/**
 * 
 */
package sif.IO.xml;

import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.adapters.XmlAdapter;

import sif.model.policy.policyrule.dynamicConditions.EBinaryRelation;

/**
 * This does (un)marshalling of Enumerations manually. This is necessary because 
 * in some JVM Versions the JAXB implementation contains errors causing 
 * Enumerations not to be correctly (un)marshalled.
 * 
 * @author Manuel Lemcke
 *
 */
public class BinaryRelationAdapter extends XmlAdapter<String, EBinaryRelation> {

    @Override
    public String marshal(EBinaryRelation binaryRelation) throws Exception {
        return binaryRelation.name();
    }

    @Override
    public EBinaryRelation unmarshal(String string) throws Exception {
        try {
            return EBinaryRelation.valueOf(string);
        } catch(Exception e) {
            throw new JAXBException(e);
        }
    }

}
