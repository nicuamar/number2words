package com.mambu.number2words.parsing.tokenization;

import com.mambu.number2words.parsing.interfaces.ValueToken;
import com.mambu.number2words.parsing.interfaces.Visitor;

/**
 * Token that represents a value that can be atomically represented into words (without the need of further
 * tokenization).
 * <p>
 * For example, in English, values 0 (zero) through 20 (twenty) can all be atomically represented by their respective
 * words.
 * 
 * @author aatasiei
 *
 */
public class MappedValueToken implements ValueToken {

	private long value;

	/**
	 * Constructor for {@link MappedValueToken}.
	 * 
	 * @param value
	 *            - the long value that this token should map. Must not be negative.
	 */
	public MappedValueToken(long value) {
		if (value < 0) {
			throw new IllegalArgumentException();
		}
		this.value = value;
	}

	/**
	 * Gets the value mapped by this token.
	 * 
	 * @return a long value.
	 */
	public long getMappedValue() {
		return this.value;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <V> V accept(Visitor<V> visitor) {
		return visitor.visitMappedValue(this);
	}
}
