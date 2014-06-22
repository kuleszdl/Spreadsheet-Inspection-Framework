package sif.frontOffice;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.TreeMap;

import sif.model.policy.Policy;
import sif.model.policy.policyrule.AbstractPolicyRule;
import sif.model.policy.policyrule.CompositePolicyRule;
import sif.model.policy.policyrule.DynamicPolicyRule;
import sif.model.policy.policyrule.MonolithicPolicyRule;
import sif.model.policy.policyrule.SanityPolicyRule;
import sif.model.policy.policyrule.configuration.ConfigurableParameter;
import sif.model.policy.policyrule.configuration.ParameterConfiguration;
import sif.model.policy.policyrule.configuration.PolicyRuleConfiguration;
import sif.model.policy.policyrule.dynamicConditions.AbstractCondition;
import sif.model.policy.policyrule.dynamicConditions.BinaryCondition;
import sif.model.policy.policyrule.dynamicConditions.ElementCountCondition;
import sif.model.policy.policyrule.dynamicConditions.TernaryCondition;
import sif.model.policy.policyrule.implementations.FormulaComplexityPolicyRule;
import sif.model.policy.policyrule.implementations.NoConstantsInFormulasPolicyRule;
import sif.model.policy.policyrule.implementations.ReadingDirectionPolicyRule;
import sif.technicalDepartment.equipment.testing.facilities.implementations.DynamicTestFacility;
import sif.technicalDepartment.equipment.testing.facilities.implementations.FormulaComplexityTestFacility;
import sif.technicalDepartment.equipment.testing.facilities.implementations.NoConstantsInFormulasTestFacilitiy;
import sif.technicalDepartment.equipment.testing.facilities.implementations.ReadingDirectionTestFacility;
import sif.technicalDepartment.equipment.testing.facilities.implementations.SanityTestFacility;
import sif.technicalDepartment.equipment.testing.facilities.implementations.dynamicCheckers.BinaryConditionChecker;
import sif.technicalDepartment.equipment.testing.facilities.implementations.dynamicCheckers.ElementCountChecker;
import sif.technicalDepartment.equipment.testing.facilities.implementations.dynamicCheckers.IConditionChecker;
import sif.technicalDepartment.equipment.testing.facilities.implementations.dynamicCheckers.TernaryConditionChecker;
import sif.technicalDepartment.equipment.testing.facilities.types.MonolithicTestFacility;
import sif.technicalDepartment.management.TechnicalManager;

/***
 * The PolicyManager handles the management of the policies and policy rules
 * that are offered by SIF. It is responsible for the registration of new
 * policies and policy rules as well as their configuration. Currently available
 * policies and policy rules must be hardcoded as the registered list will not
 * be persisted.
 * 
 * @author Sebastian Zitzelsberger
 * 
 */
public class PolicyManager {
	private TreeMap<String, Class<? extends AbstractPolicyRule>> availablePolicyRules;

	private TreeMap<String, Policy> availablePolicies;

	protected PolicyManager() {
		this.availablePolicies = new TreeMap<String, Policy>();
		this.availablePolicyRules = new TreeMap<String, Class<? extends AbstractPolicyRule>>();

		// Register available policy rules.
		register(NoConstantsInFormulasPolicyRule.class,
				NoConstantsInFormulasTestFacilitiy.class);
		register(ReadingDirectionPolicyRule.class,
				ReadingDirectionTestFacility.class);
		register(FormulaComplexityPolicyRule.class,
				FormulaComplexityTestFacility.class);
		register(DynamicPolicyRule.class, DynamicTestFacility.class);
		register(SanityPolicyRule.class, SanityTestFacility.class);
		
		// Register available conditions
		registerCondition(BinaryCondition.class, BinaryConditionChecker.class);
		registerCondition(TernaryCondition.class, TernaryConditionChecker.class);
		registerCondition(ElementCountCondition.class, ElementCountChecker.class);
		//TODO registerCondition(PropertyCondition.class, PropertyConditionChecker.class);
		
		// Create and register available policies.
		Policy policy = new Policy();
		policy.setName("Basic Policy");
		policy.setAuthor("Sebastian Zitzelsberger");
		policy.setDescription("A basic policy that checks the spreadsheet for its"
				+ "reading direction, constants in formulas and the complexity of formulas.");
		policy.add(new NoConstantsInFormulasPolicyRule());
		policy.add(new ReadingDirectionPolicyRule());
		policy.add(new FormulaComplexityPolicyRule());
		//policy.add(new DynamicPolicyRule());
		register(policy);
		
		Policy dynPolicy = new Policy();
		dynPolicy.setName("Dynamic Policy");
		dynPolicy.setAuthor("Manuel Lemcke");
		dynPolicy.setDescription("A policy that also supports dynamic inspection of spreadsheets."); //TODO Beschreibung um aktuell implementierte Rules ergaenzen
		dynPolicy.add(new DynamicPolicyRule());
		register(dynPolicy);
		
		Policy sanePolicy = new Policy();
		sanePolicy.setName("Sanity Policy");
		sanePolicy.setAuthor("Wolfgang Kraus");
		sanePolicy.setDescription("A policy which supports basic sanity checks");
		sanePolicy.add(new SanityPolicyRule());
		register(sanePolicy);
	}

	/**
	 * Checks if the given policy contains only policy rules that are registered
	 * with the PolicyManager.
	 * 
	 * @param policy
	 *            The given policy.
	 * @return Whether the given policy contains only registered policy rules.
	 */
	private Boolean containsOnlyRegisteredPolicyRules(Policy policy) {
		Boolean result = true;

		//TODO Entweder auch DynamicPolicy.rules ueberpruefen oder .rules und 
		// .abstractPolicyRules mergen
		for (AbstractPolicyRule policyRule : policy.getPolicyRules().values()) {
			if (!availablePolicyRules.values().contains(policyRule.getClass())) {
				result = false;
				break;
			}
		}
		return result;

	}

	/**
	 * Creates the default configuration for the given policy rule.
	 * 
	 * @param policyRule
	 *            The given policy rule
	 * @throws Exception
	 */
	private void createDefaultConfigurationFor(AbstractPolicyRule policyRule) {
		PolicyRuleConfiguration ruleConfiguration = new PolicyRuleConfiguration();

		// Create a ParameterConfiguration for each field with an
		// ConfigurableParameter annotation.
		for (Field field : policyRule.getClass().getDeclaredFields()) {
			if (field.isAnnotationPresent(ConfigurableParameter.class)) {
				ConfigurableParameter parameter = field
						.getAnnotation(ConfigurableParameter.class);
				ParameterConfiguration<?> parameterConfiguration = null;
				String pattern = parameter.pattern();
				String name = parameter.displayedName();
				String description = parameter.description();

				Class<?> parameterClass = parameter.parameterClass();
				field.setAccessible(true);
				Object value;
				try {
					value = field.get(policyRule);
					parameterConfiguration = ParameterConfiguration
							.createParameterConfiguationFor(parameterClass,
									pattern);
					parameterConfiguration.setDefaultValue(value);
					parameterConfiguration.setParamaterName(name);
					parameterConfiguration.setDescription(description);
					parameterConfiguration.setFieldName(field.getName());
					ruleConfiguration.add(parameterConfiguration);
					field.setAccessible(false);
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}

		policyRule.setConfiguration(ruleConfiguration);
	}

	/**
	 * Gets the available policies that have been registered with the
	 * PolicyManager.
	 * 
	 * @return The available policies.
	 */
	protected TreeMap<String, Policy> getAvailablePolicies() {
		return this.availablePolicies;
	}

	/**
	 * Gets the available policy rules that haven been registered with the
	 * PolicyManager. Each policy rule has a default configuration.
	 * 
	 * @return The available policy rules.
	 */
	protected ArrayList<AbstractPolicyRule> getAvailablePolicyRules() {
		ArrayList<AbstractPolicyRule> rules = new ArrayList<AbstractPolicyRule>();
		for (Class<? extends AbstractPolicyRule> abstractPolicyRuleClass : availablePolicyRules
				.values()) {
			AbstractPolicyRule rule = null;
			try {
				// Create a new instance from the policy rule and create a
				// default configuration for it.
				rule = abstractPolicyRuleClass.newInstance();
				createDefaultConfigurationFor(rule);
				rules.add(rule);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		return rules;
	}

	/***
	 * Register the given policy rule class.
	 * 
	 * @param compositeRule
	 *            the given policy rule class.
	 */
	protected void register(Class<? extends CompositePolicyRule> compositeRule) {
		this.availablePolicyRules.put(compositeRule.getCanonicalName(),
				compositeRule);
	}

	/***
	 * Registers the given policy rule class and associates it with the given
	 * test facility class.
	 * 
	 * @param policyRuleClass
	 *            The given policy rule class.
	 * @param testFacilityClass
	 *            The given test facility class.
	 */
	protected void register(
			Class<? extends MonolithicPolicyRule> policyRuleClass,
			Class<? extends MonolithicTestFacility> testFacilityClass) {
		TechnicalManager.getInstance().register(policyRuleClass,
				testFacilityClass);
		this.availablePolicyRules.put(policyRuleClass.getCanonicalName(),
				policyRuleClass);
	}
	
	/***
	 * Registers the given policy rule class and associates it with the given
	 * test facility class.
	 * 
	 * @param policyRuleClass
	 *            The given policy rule class.
	 * @param testFacilityClass
	 *            The given test facility class.
	 */
	protected void registerCondition(
			Class<? extends AbstractCondition> conditionClass,
			Class<? extends IConditionChecker> checkerClass) {
		TechnicalManager.getInstance().registerCondition(conditionClass,
				checkerClass);
	}

	/***
	 * Registers the given policy with the PolicyManager.
	 * 
	 * @param policy
	 *            The given policy.
	 */
	protected void register(Policy policy) {
		if (containsOnlyRegisteredPolicyRules(policy)) {

			for (AbstractPolicyRule policyRule : policy.getPolicyRules()
					.values()) {
				if (policyRule.getConfiguration() == null) {
					try {
						this.createDefaultConfigurationFor(policyRule);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			this.availablePolicies.put(policy.getName(), policy);
		}
	}

}