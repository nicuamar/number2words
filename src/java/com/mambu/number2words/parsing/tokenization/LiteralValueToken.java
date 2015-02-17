package com.mambu.number2words.parsing.tokenization;

import java.util.Objects;

import com.mambu.number2words.parsing.interfaces.ValueToken;
import com.mambu.number2words.parsing.interfaces.Visitor;

/**
 * Token representing a string literal.
 * <p>
 * For example: "and", "y", "coma", etc...
 * 
 * @author aatasiei
 *
 */
public class LiteralValueToken implements ValueToken {

	private final String value;

	/**
	 * Default constructor.
	 * 
	 * @param value
	 *            - the literal that this token represents. Not <code>null</code>. (for <code>null</code>s use
	 *            {@link NullValueToken} instead).
	 */
	public LiteralValueToken(final String value) {
		this.value = Objects.requireNonNull(value);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <V> V accept(Visitor<V> visitor) {
		return visitor.visitLiteral(this);
	}

	/**
	 * Gets the string literal this token represents.
	 * 
	 * @return the string value. Never <code>null</code>.
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @return the literal value.
	 */
	@Override
	public String toString() {
		return value;
	};
}
