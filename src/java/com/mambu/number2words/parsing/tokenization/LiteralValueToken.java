package com.mambu.number2words.parsing.tokenization;

import java.util.Objects;

import com.mambu.number2words.parsing.interfaces.ValueToken;
import com.mambu.number2words.parsing.interfaces.Visitor;

/**
 * Token representing an empty group.
 * <p>
 * For example: 1000 should only display "one hundred" not "one hundred zero".
 * 
 * @author aatasiei
 *
 */
public class LiteralValueToken implements ValueToken {

	private final String value;

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
	 * @return the value
	 */
	public String getValue() {
		return value;
	}
}
