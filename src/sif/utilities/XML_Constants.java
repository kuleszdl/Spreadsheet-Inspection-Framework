package sif.utilities;

public class XML_Constants {
	public static final String NAME_POLICY_LIST = "policyList";

	public static final String NAME_DYNAMIC_POLICY = "dynamicPolicy",
			NAME_DYNAMIC_INPUT_VALUE = "inputCell",
			NAME_DYNAMIC_INPUT_WRAPPER = "inputCells",
			NAME_DYNAMIC_OUTPUT_VALUE = "outputCell",
			NAME_DYNAMIC_OUTPUT_WRAPPER = "outputCells",
			NAME_DYNAMIC_POLICY_FILENAME = "spreadsheetFileName",
			NAME_DYNAMIC_POLICY_RULE = "rules";

	public static final String NAME_FORMULA_COMPLEXITY_POLICY_RULE = "formulaComplexityPolicyRule",
			NAME_FORMULA_MAX_NESTING = "formulaComplexityMaxNesting",
			NAME_FORMULA_MAX_NR_OPERATIONS = "formulaComplexityMaxOperations";

	public static final String NAME_NO_CONSTANTS_POLICY_RULE = "noConstantsPolicyRule",
			NAME_NO_CONSTANTS_IGNORED_CONSTANTS_VALUE = "ignoredConstant",
			NAME_NO_CONSTANTS_IGNORED_FUNCTION_NAME = "functionName",
			NAME_NO_CONSTANTS_IGNORED_CONSTANTS_WRAPPER = "ignoredConstants",
			NAME_NO_CONSTANTS_IGNORED_FUNCTIONS_WRAPPER = "ignoredFunctions";

	public static final String NAME_IGNORED_CELLS_VALUE = "location",
			NAME_IGNORED_CELLS_WRAPPER = "ignoredCells";

	public static final String NAME_READING_DIRECTION_POLICY_RULE = "readingDirectionPolicyRule",
			NAME_READING_DIRECTION_LEFT_TO_RIGHT = "leftToRight",
			NAME_READING_DIRECTION_TOP_TO_BOTTOM = "topToBottom";

	public static final String NAME_SANITY_POLICY_RULE = "sanityRules",
			NAME_SANITY_CHECKING_WRAPPER = "sanityCheckingCells",
			NAME_SANITY_CONSTRAINT_WRAPPER = "sanityConstraintCells",
			NAME_SANITY_EXPLANATION_WRAPPER = "sanityExplanationCells",
			NAME_SANITY_LOCATION_NAME = "location",
			NAME_SANITY_VALUE_WRAPPER = "sanityValueCells",
			NAME_SANITY_WARNINGS = "sanityWarnings";

	public static final String NAME_OUTPUT_CELL = "cell",
			NAME_OUTPUT_CELL_LOCATION = "location",
			NAME_OUTPUT_CELL_CONTENT = "content";

	public static final String NAME_INSPECTION_REQUEST = "test",
			NAME_INSPECTION_REQUEST_TITLE = "title",
			NAME_INSPECTION_REQUEST_FILE = "file";

	public static final String NAME_OUTPUT_POLICY  = "policy",
			NAME_OUTPUT_POLICY_AUTHOR = "author",
			NAME_OUTPUT_POLICY_DESCRIPTION = "description",
			NAME_OUTPUT_POLICY_POLICY_NAME = "name";

	public static final String NAME_FINDINGS = "findings",
			NAME_FINDINGS_VIOLATIONLIST = "testedRule";

	public static final String NAME_VIOLATIONLIST_NAME = "name",
			NAME_VIOLATIONLIST_VIOLATIONS_WRAPPER = "violations",
			NAME_VIOLATIONLIST_POLICY = "testedPolicy";

	public static final String NAME_INVENTORY = "cells",
			NAME_INVENTORY_INPUT_WRAPPER = "input",
			NAME_INVENTORY_INTERMEDIATE_WRAPPER = "intermediate",
			NAME_INVENTORY_OUTPUT_WRAPPER = "output",
			NAME_INVENTORY_CELL_NAME = "cell";

	public static final String NAME_SINGLE_VIOLATION = "singleviolation",
			NAME_SINGLE_VIOLATION_CAUSING = "causingelement",
			NAME_SINGLE_VIOLATION_LOCATION = "location",
			NAME_SINGLE_VIOLATION_DESCRIPTION = "description",
			NAME_SINGLE_VIOLATION_SEVERITY = "severity";

	public static final String NAME_GROUP_VIOLATION = "violationgroup";

	public static final String NAME_MULTIPLE_SAME_REF_POLICY = "multipleSameRefPolicyRule",
			NAME_NON_CONSIDERED_VALUES_POLICY = "nonConsideredValuesPolicyRule",
			NAME_REF_TO_NULL_POLICY = "refToNullPolicyRule";

	public static final String NAME_STRING_DISTANCE_POLICY = "stringDistancePolicyRule",
			NAME_STRING_DISTANCE_DIFFERENCE = "stringDistanceDifference";

	public static final String NAME_IGNORED_WORKSHEETS_WRAPPER = "ignoredWorksheets",
			NAME_IGNORED_WORKSHEETS_VALUE = "ignoredWorksheetName";

	public static final String NAME_ONE_AMONG_OTHERS_POLICY = "oneAmongOthersPolicyRule",
			NAME_ONE_AMONG_OTHERS_ENVIRONMENT_STYLE = "oneAmongOthersStyle",
			NAME_ONE_AMONG_OTHERS_ENVIRONMENT_LENGTH = "oneAmongOthersLength";


}
