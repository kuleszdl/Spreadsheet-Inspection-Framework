package sif.model.policy.policyrule.implementations;

import sif.model.policy.policyrule.MonolithicPolicyRule;
import sif.model.policy.policyrule.configuration.ConfigurableParameter;

public class SanityPolicyRule extends MonolithicPolicyRule{

	@ConfigurableParameter(parameterClass = String[].class, displayedName = "Worksheets to check", description = "Defines if the worksheet which should be checked.")
	private String[] worksheetToCheck = new String[0];

	@ConfigurableParameter(parameterClass = String[][].class, displayedName = "Location of the restraining rows", description = "Defines the pairs of Value - Restriction as an Address 'worksheet!Column'.")
	private String[][] constraintLocations = new String[0][0];
	
	@ConfigurableParameter(parameterClass = Boolean.class, displayedName = "Warn when a column without constraints is encountered.", description = "Defines whether to warn when a column is neither being checked nor imposes restrictions.")
	private boolean warnUnconstrained = false;
	
	public SanityPolicyRule() {
		super();
		setDescription("Performs basic sanity checks");
	}
}
