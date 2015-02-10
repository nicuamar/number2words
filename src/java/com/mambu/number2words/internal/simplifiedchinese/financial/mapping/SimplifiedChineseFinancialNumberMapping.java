package com.mambu.number2words.internal.simplifiedchinese.financial.mapping;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.mambu.number2words.parsing.interfaces.ValueMapping;

/**
 * Listing of all (up to 10<sup>12</sup>) the Simplified Chinese financial numbers that can be mapped to a word.
 * 
 * @author aatasiei
 *
 */
public enum SimplifiedChineseFinancialNumberMapping implements ValueMapping {

	ZERO(0, "零"), ONE(1, "壹"), TWO(2, "贰"), THREE(3, "叁"), FOUR(4, "肆"), FIVE(5, "伍"), SIX(6, "陆"), SEVEN(7, "柒"), EIGHT(
			8, "捌"), NINE(9, "玖"),

	TEN(10, "拾", MappingType.SUBGROUP_QUANTIFIER),

	HUNDRED(100, "佰", MappingType.SUBGROUP_QUANTIFIER),

	THOUSAND(1000, "仟", MappingType.SUBGROUP_QUANTIFIER),

	TEN_THOUSAND(1_0000, "萬", MappingType.GROUP_QUANTIFIER),

	HUNDRED_MILLION(1_0000_0000, "億", MappingType.GROUP_QUANTIFIER),

	THOUSAND_BILLION(1_0000_0000_0000L, "兆", MappingType.GROUP_QUANTIFIER);

	/**
	 * Holds the direct mappings from value to enum instance.
	 */
	private static final Map<Long, SimplifiedChineseFinancialNumberMapping> WORD_MAPPING = new HashMap<>();

	static {
		// store the Long -> Enum mapping for faster fetching.
		for (SimplifiedChineseFinancialNumberMapping sm : SimplifiedChineseFinancialNumberMapping.values()) {
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
	 * True, if the instance represents a group. For example: "萬" (10,000), "億" (100,000,000), etc...
	 * <p>
	 * Chinese uses groups of 4 digits.
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
	SimplifiedChineseFinancialNumberMapping(long value, String word) {
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
	SimplifiedChineseFinancialNumberMapping(long value, String word, MappingType mappingType) {
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
	 * Returns the mapping represented by the specified number.
	 * 
	 * @param value
	 *            - a {@link Long} instance. Not <code>null</code>.
	 * @return a {@link ValueMapping} instance.
	 * @throws NullPointerException
	 *             if the value could not be mapped.
	 */
	public static SimplifiedChineseFinancialNumberMapping fromNumber(Long value) {
		return Objects.requireNonNull(WORD_MAPPING.get(value));
	}

	@Override
	public boolean isSubGroupQuantifier() {
		return mappingType == MappingType.SUBGROUP_QUANTIFIER;
	}
}
