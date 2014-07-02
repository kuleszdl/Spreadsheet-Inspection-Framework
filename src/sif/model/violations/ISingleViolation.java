package sif.model.violations;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import sif.model.violations.single.GenericSingleViolation;

/***
 * A violation that has not been assigned or can't be assigned to a group. If a
 * custom single violation is needed, it can be realized by implementing this
 * interface. Otherwise the {@link GenericSingleViolation} can be used.
 * 
 * @author Sebastian Zitzelsberger
 * 
 */
@XmlJavaTypeAdapter(IViolation.Adapter.class)
public interface ISingleViolation extends IViolation {

}
