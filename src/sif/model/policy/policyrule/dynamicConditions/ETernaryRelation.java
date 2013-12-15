package sif.model.policy.policyrule.dynamicConditions;

import java.util.Enumeration;


/**
 * 
 * @author Manuel Lemcke
 *
 */
public enum ETernaryRelation implements Enumeration<String>{
    closed,
    open,
    openLeft,
    openRight;
    
    @Override
    public boolean hasMoreElements() {
        return this.hasMoreElements();
    }

    @Override
    public String nextElement() {
        return this.nextElement();
    } 
}
