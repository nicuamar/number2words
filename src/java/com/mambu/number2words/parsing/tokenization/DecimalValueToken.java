package com.mambu.number2words.parsing.tokenization;

import java.util.Objects;

import com.mambu.number2words.parsing.interfaces.ValueToken;
import com.mambu.number2words.parsing.interfaces.Visitor;

/**
 * Token that represents a decimal value.
 * 
 * @author aatasiei
 *
 */
@Deprecated
public class DecimalValueToken extends AbstractPairToken {

	/**
	 * Separator string that should represent a decimal separator.
	 */
	// FUTURE: (aatasiei) if String literals appear in other tokens, a LiteralToken might be required.
	private String separator;

	/**
	 * Default constructor.
	 * 
	 * @param integerPart
	 *            - the token representing the number to the left of the decimal point.
	 * @param decimalSeparator
	 *            - the token representing the decimal separator.
	 * @param fractionalPart
	 *            - the token representing the number to the right of the decimal point.
	 */
	public DecimalValueToken(ValueToken integerPart, String decimalSeparator, ValueToken fractionalPart) {
		super(integerPart, fractionalPart);
		this.separator = Objects.requireNonNull(decimalSeparator);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <V> V accept(Visitor<V> visitor) {
		return visitor.visitDecimalValue(this);
	}

	/**
	 * Gets the decimal separator string.
	 * 
	 * @return a String instance. Not <code>null</code>.
	 */
	public String getDecimalSeparator() {
		return separator;
	}

	/**
	 * Gets the token representing the number to the left of the decimal point.
	 * 
	 * @return a {@link ValueToken} instance. Not <code>null</code>.
	 */
	public ValueToken getIntegerPart() {
		return getLeftToken();
	}

	/**
	 * Gets the token representing the number to the right of the decimal point.
	 * 
	 * @return a {@link ValueToken} instance. Not <code>null</code>.
	 */
	public ValueToken getFractionalPart() {
		return getRightToken();
	}

}
