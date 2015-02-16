package com.mambu.number2words.internal.spanish.mapping;

import static com.mambu.number2words.internal.common.mapping.SimpleWordValue.wordOf;
import static com.mambu.number2words.internal.common.mapping.SimpleWordValue.MappingWordData.map;
import static com.mambu.number2words.parsing.interfaces.WordValue.Form.DEFAULT;
import static com.mambu.number2words.parsing.interfaces.WordValue.Form.SHORTENED;
import static com.mambu.number2words.parsing.interfaces.WordValue.GrammaticalNumber.PLURAL;
import static com.mambu.number2words.parsing.interfaces.WordValue.GrammaticalNumber.SINGULAR;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.mambu.number2words.parsing.interfaces.ValueMapping;
import com.mambu.number2words.parsing.interfaces.WordValue;

/**
 * Listing of all the English numbers that can be mapped to a word.
 * 
 * @author aatasiei
 *
 */
public enum SpanishNumberMapping implements ValueMapping {

	ZERO(0, "cero"), ONE(1, wordOf(map(DEFAULT, "uno"), map(SHORTENED, "un"))), TWO(2, "dos"), THREE(3, "tres"), FOUR(
			4, "cuatro"), FIVE(5, "cinco"), SIX(6, "seis"), SEVEN(7, "siete"), EIGHT(8, "ocho"), NINE(9, "nueve"),

	TEN(10, "diez", MappingType.SUBGROUP_QUANTIFIER),

	ELEVEN(11, "once"), TWELVE(12, "doce"), THIRTEEN(13, "trece"), FOURTEEN(14, "catorce"), FIFTEEN(15, "quince"), SIXTEEN(
			16, "dieciseis"), SEVENTEEN(17, "diecisiete"), EIGHTEEN(18, "dieciocho"), NINETEEN(19, "diecinueve"),

	TWENTY(20, "veinte", MappingType.SUBGROUP_QUANTIFIER),

	THIRTY(30, "treinta", MappingType.SUBGROUP_QUANTIFIER),

	FOURTY(40, "cuarenta", MappingType.SUBGROUP_QUANTIFIER),

	FIFTY(50, "cincuenta", MappingType.SUBGROUP_QUANTIFIER),

	SIXTY(60, "sesenta", MappingType.SUBGROUP_QUANTIFIER),

	SEVENTY(70, "setenta", MappingType.SUBGROUP_QUANTIFIER),

	EIGHTY(80, "ochenta", MappingType.SUBGROUP_QUANTIFIER),

	NINETY(90, "noventa", MappingType.SUBGROUP_QUANTIFIER),

	HUNDRED(100, wordOf(map(DEFAULT, "ciento"), map(SHORTENED, "cien")), MappingType.SUBGROUP_QUANTIFIER),

	TWO_HUNDRED(200, "doscientos", MappingType.SUBGROUP_QUANTIFIER),

	THREE_HUNDRED(300, "trecientos", MappingType.SUBGROUP_QUANTIFIER),

	FOUR_HUNDRED(400, "cuatrocientos", MappingType.SUBGROUP_QUANTIFIER),

	FIVE_HUNDRED(500, "quinientos", MappingType.SUBGROUP_QUANTIFIER),

	SIX_HUNDRED(600, "seiscientos", MappingType.SUBGROUP_QUANTIFIER),

	SEVEN_HUNDRED(700, "setecientos", MappingType.SUBGROUP_QUANTIFIER),

	EIGHT_HUNDRED(800, "ochocientos", MappingType.SUBGROUP_QUANTIFIER),

	NINE_HUNDRED(900, "novecientos", MappingType.SUBGROUP_QUANTIFIER),

	THOUSAND(1_000, "mil", MappingType.GROUP_QUANTIFIER),

	MILLION(1_000_000, wordOf(map(SINGULAR, "millon"), map(PLURAL, "millones")), MappingType.GROUP_QUANTIFIER),

	TRILLION(1_000_000_000_000L, wordOf(map(SINGULAR, "billon"), map(PLURAL, "billones")), MappingType.GROUP_QUANTIFIER);

	/**
	 * Holds the direct mappings from value to enum instance.
	 */
	private static final Map<Long, SpanishNumberMapping> WORD_MAPPING = new HashMap<>();

	static {
		// store the Long -> Enum mapping for faster fetching.
		for (SpanishNumberMapping sm : SpanishNumberMapping.values()) {
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
	private WordValue word;
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
	SpanishNumberMapping(long value, String word) {
		this(value, wordOf(word), MappingType.SIMPLE);
	}

	SpanishNumberMapping(long value, WordValue word) {
		this(value, word, MappingType.SIMPLE);
	}

	SpanishNumberMapping(long value, String word, MappingType mappingType) {
		this(value, wordOf(word), mappingType);
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
	SpanishNumberMapping(long value, WordValue word, MappingType mappingType) {
		this.value = value;
		this.word = word;
		this.mappingType = mappingType;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public WordValue getWordValue() {
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
	public static SpanishNumberMapping fromNumber(Long value) {
		return Objects.requireNonNull(WORD_MAPPING.get(value));
	}

}
