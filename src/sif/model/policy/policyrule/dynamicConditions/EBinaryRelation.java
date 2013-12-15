package sif.model.policy.policyrule.dynamicConditions;

import java.util.Enumeration;

/**
 * 
 * @author Manuel Lemcke
 *
 */
public enum EBinaryRelation implements Enumeration<String>{
    equal,
    greaterThan,
    lessThan,
    lessOrEqual,
    greaterOrEqual;

    @Override
    public boolean hasMoreElements() {
        return this.hasMoreElements();            
    }

    @Override
    public String nextElement() {
        return this.nextElement();
    }      
}