package sif.model.elements.basic.tokens;

import sif.model.elements.BasicAbstractElement;
import sif.model.elements.basic.tokencontainers.ITokenContainer;

/**
 * Representation of a scalar constant.
 * 
 * @author Sebastian Zitzelsberger
 * 
 */
public class ScalarConstant extends BasicAbstractElement implements
		ITokenElement {

	ITokenContainer container;

	private Object value = null;
	private ScalarConstantType type = ScalarConstantType.MISSING_ARGUMENT;
	private String errorValue;
	private String textValue;
	private Integer integerValue;
	private Double numericValue;
	private Boolean booleanValue;

	public Boolean getBooleanValue() {
		return booleanValue;
	}

	public ITokenContainer getContainer() {
		return this.container;
	}

	public String getErrorValue() {
		return errorValue;
	}

	public Integer getIntegerValue() {
		return integerValue;
	}

	@Override
	public String getLocation() {
		return container.getLocation();
	}

	public Double getNumericValue() {
		return numericValue;
	}

	@Override
	public String getStringRepresentation() {
		StringBuilder builder = new StringBuilder();
		builder.append(getType().toString().toLowerCase());
		builder.replace(0, 1, Character.toString(builder.charAt(0))
				.toUpperCase());
		builder.append(" : ");
		builder.append(getValueAsString());
		return builder.toString();
	}

	public String getTextValue() {
		return textValue;
	}

	public ScalarConstantType getType() {
		return type;
	}

	public Object getValue() {
		return value;
	}

	@Override
	public String getValueAsString() {
		String value = "";
		switch (type) {
		case BOOLEAN:
			value = booleanValue.toString();
			break;
		case ERROR:
			value = errorValue;
			break;
		case INTEGER:
			value = integerValue.toString();
			break;
		case MISSING_ARGUMENT:
			value = "Missing argument";
			break;
		case NUMERIC:
			value = numericValue.toString();
			break;
		case TEXT:
			value = textValue;
			break;
		default:
			value = "?";
			break;
		}
		return value;
	}

	@Override
	public Boolean isSameAs(ITokenElement token) {
		Boolean isSame = false;
		if (token instanceof ScalarConstant) {
			ScalarConstant otherConstant = (ScalarConstant) token;

			if (this.type == otherConstant.type) {
				if (this.getValue() != null) {
					isSame = this.getValue().equals(otherConstant.getValue());
				}
			}

		}
		return isSame;
	}

	public void setBooleanValue(Boolean booleanValue) {
		this.booleanValue = booleanValue;
		this.value = booleanValue;
		this.type = ScalarConstantType.BOOLEAN;
	}

	public void setContainer(ITokenContainer container) {
		this.container = container;
	}

	public void setErrorValue(String errorValue) {
		this.errorValue = errorValue;
		this.value = errorValue;
		this.type = ScalarConstantType.ERROR;
	}

	public void setIntegerValue(Integer integerValue) {
		this.integerValue = integerValue;
		this.value = integerValue;
		this.type = ScalarConstantType.INTEGER;
	}

	public void setNumericValue(Double numericValue) {
		this.numericValue = numericValue;
		this.value = numericValue;
		this.type = ScalarConstantType.NUMERIC;
	}

	public void setTextValue(String textValue) {
		this.textValue = textValue;
		this.value = textValue;
		this.type = ScalarConstantType.TEXT;
	}
}