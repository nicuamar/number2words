package com.mambu.number2words.internal.english.mapping;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.mambu.number2words.parsing.interfaces.ValueMapping;

/**
 * Listing of all the English numbers that can be mapped to a word.
 * 
 * @author aatasiei
 *
 */
public enum EnglishNumberMapping implements ValueMapping {

	ZERO(0, "zero"), ONE(1, "one"), TWO(2, "two"), THREE(3, "three"), FOUR(4, "four"), FIVE(5, "five"), SIX(6, "six"), SEVEN(
			7, "seven"), EIGHT(8, "eight"), NINE(9, "nine"),

	TEN(10, "ten", MappingType.SUBGROUP_QUANTIFIER),

	ELEVEN(11, "eleven"), TWELVE(12, "twelve"), THIRTEEN(13, "thirteen"), FOURTEEN(14, "fourteen"), FIFTEEN(
			15, "fifteen"), SIXTEEN(16, "sixteen"), SEVENTEEN(17, "seventeen"), EIGHTEEN(18, "eighteen"), NINETEEN(19,
			"nineteen"),

	TWENTY(20, "twenty"), THIRTY(30, "thirty"), FOURTY(40, "forty"), FIFTY(50, "fifty"), SIXTY(60, "sixty"), SEVENTY(
			70, "seventy"), EIGHTY(80, "eighty"), NINETY(90, "ninety"),

	HUNDRED(100, "hundred", MappingType.SUBGROUP_QUANTIFIER),

	THOUSAND(1_000, "thousand", MappingType.GROUP_QUANTIFIER),

	MILLION(1_000_000, "million", MappingType.GROUP_QUANTIFIER),

	BILLION(1_000_000_000, "billion", MappingType.GROUP_QUANTIFIER),

	TRILLION(1_000_000_000_000L, "trillion", MappingType.GROUP_QUANTIFIER);

	/**
	 * Holds the direct mappings from value to enum instance.
	 */
	private static final Map<Long, EnglishNumberMapping> WORD_MAPPING = new HashMap<>();

	static {
		// store the Long -> Enum mapping for faster fetching.
		for (EnglishNumberMapping sm : EnglishNumberMapping.values()) {
			WORD_MAPPING.put(sm.getValue(), sm);
		}
	}

	/**
	 * Value the instance represents.
	 */
	private long value;
	/**
	 * String to be used when transcribing the value.
	 */
	private String word;
	/**
	 * True, if the instance represents a group. For example: "thousand", "million", etc...
	 */
	private MappingType mappingType;

	/**
	 * Constructor for value mappings (non quantifiers).
	 * 
	 * @param value
	 *            - long value represented.
	 * @param word
	 *            - the String representation.
	 */
	EnglishNumberMapping(long value, String word) {
		this(value, word, MappingType.SIMPLE);
	}

	/**
	 * Constructor for value mappings.
	 * 
	 * @param value
	 *            - long value represented.
	 * @param word
	 *            - the String representation.
	 * @param isQuantifier
	 *            - true, if the value is a quantifier
	 */
	EnglishNumberMapping(long value, String word, MappingType mappingType) {
		this.value = value;
		this.word = word;
		this.mappingType = mappingType;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getWord() {
		return word;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Long getValue() {
		return value;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isGroupQuantifier() {
		return mappingType == MappingType.GROUP_QUANTIFIER;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isSubGroupQuantifier() {
		return mappingType == MappingType.SUBGROUP_QUANTIFIER;
	}

	/**
	 * Returns the mapping represented by the specified number.
	 * 
	 * @param value
	 *            - a {@link Long} instance. Not <code>null</code>.
	 * @return a {@link ValueMapping} instance.
	 * @throws NullPointerException
	 *             if the value could not be mapped.
	 */
	public static EnglishNumberMapping fromNumber(Long value) {
		return Objects.requireNonNull(WORD_MAPPING.get(value));
	}
}
