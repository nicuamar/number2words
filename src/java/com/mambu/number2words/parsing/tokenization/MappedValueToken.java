package com.mambu.number2words.parsing.tokenization;

import com.mambu.number2words.parsing.interfaces.ValueMapping.MappingType;
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

	private final long value;
	private final MappingType mappingType;

	/**
	 * Constructor for {@link MappedValueToken}.
	 * 
	 * @param value
	 *            - the long value that this token should map. Must not be negative.
	 */
	public MappedValueToken(long value) {
		this(value, MappingType.SIMPLE);
	}

	/**
	 * Constructor for {@link MappedValueToken} which also supplies {@link MappingType}.
	 * 
	 * @param value
	 *            - the long value that this token should map. Must not be negative.
	 * @param type
	 *            - the mapping type. This can be used to identify group quantifiers during visits, when necessary.
	 */
	public MappedValueToken(long value, MappingType type) {
		if (value < 0) {
			throw new IllegalArgumentException("Negative values are not currently supported.");
		}
		this.value = value;
		this.mappingType = type;
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
	 * Gets the mapping type of this token.
	 * 
	 * @return the mappingType
	 */
	public MappingType getMappingType() {
		return mappingType;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <V> V accept(Visitor<V> visitor) {
		return visitor.visitMappedValue(this);
	}

	/**
	 * @return mapping type + ": " + value
	 */
	@Override
	public String toString() {
		return mappingType + ": " + value;
	}
}
