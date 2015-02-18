package com.mambu.number2words.internal.spanish.tokenization;

import com.mambu.number2words.internal.common.tokenization.AbstractGroupedValuesTokenizer;
import com.mambu.number2words.internal.spanish.mapping.SpanishNumberMapping;
import com.mambu.number2words.parsing.interfaces.ValueMapping.MappingType;
import com.mambu.number2words.parsing.interfaces.ValueToken;
import com.mambu.number2words.parsing.tokenization.LiteralValueToken;
import com.mambu.number2words.parsing.tokenization.MappedValueToken;
import com.mambu.number2words.parsing.tokenization.PrefixedValueToken;
import com.mambu.number2words.parsing.tokenization.SuffixedValueToken;

/**
 * Tokenizer used to parse a number using Spanish semantics.
 * <p>
 * Before tokenization each number will be split into groups. These groups are between 3 and 6 digits long.
 * <p>
 * The numbers before and after the decimal point will be tokenized using the same process (so, 22.22 will result in
 * "veintidos coma veintidos")
 * <p>
 * This class is thread safe as it stores no state and it deals with immutable data.
 * 
 * @author aatasiei
 *
 */
public class SpanishNumberTokenizer extends AbstractGroupedValuesTokenizer<SpanishNumberMapping> {

	/**
	 * Conjunction used between certain numbers (for example 32 is "treinta " + "y" + " tres")
	 */
	private static final String NUMBER_CONJUNCTION = "y";
	/**
	 * Static literal for {@link #NUMBER_CONJUNCTION}.
	 */
	private static final LiteralValueToken NUMBER_CONJUNCTION_LITERAL = new LiteralValueToken(NUMBER_CONJUNCTION);

	/**
	 * Decimal separator for Spanish.
	 */
	private static final String DECIMAL_SEPARATOR = "coma";

	/**
	 * Default constructor.
	 */
	public SpanishNumberTokenizer() {
		super(SpanishNumberMapping.class, DECIMAL_SEPARATOR);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected ValueToken parseGroup(long groupValue, long quantifierValue) {

		// for 1000, only "mil" needs to be printed
		if (quantifierValue == 1000L && groupValue == 1L) {
			return mappedValue(quantifierValue);
		}

		if (groupValue == 0) {
			// value is 0, so nothing should be printed
			return nullValue();
		}
		if (quantifierValue > 1) {
			// add quantifier: thousand, million, billion
			return new SuffixedValueToken(parseGroupValue(groupValue), mappedQuantifierValue(quantifierValue));
		}
		return parseGroupValue(groupValue);
	}

	/**
	 * Creates a new {@link MappedValueToken} for a {@link MappingType#GROUP_QUANTIFIER}.
	 * 
	 * @param quantifierValue
	 *            - the quantifier value.
	 * @return a {@link ValueToken} instance. Never <code>null</code>.
	 */
	private static MappedValueToken mappedQuantifierValue(long quantifierValue) {
		return new MappedValueToken(quantifierValue, MappingType.GROUP_QUANTIFIER);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected ValueToken parseSubGroup(long groupValue, long subGroupQuantifier) {

		final long lastDigits = groupValue % subGroupQuantifier;

		if (lastDigits == 0) {
			// 10, 20, 30, ... 90, 100, 200, 300, etc...
			return mappedValue(groupValue);

		} else {

			// this branch covers:
			// 1) 21 to 99 excluding 30, 40, ...
			// 2) 101 to 999 excluding 200, 300, ...

			// the prefix represents 20, 30, ... 90, 100, 200, 300, etc..
			final ValueToken prefix = parseSubGroup(groupValue - lastDigits, subGroupQuantifier);

			ValueToken prefixedValue = null;

			// adding the last digits to the prefix to get the full number
			if (subGroupQuantifier >= largestConsecutiveMapping && subGroupQuantifier < 100L) {

				// this will contain the last digits in the sub group
				// for example: "y dos", where "y" is the NUMBER_CONJUNCTION constant

				prefixedValue = new PrefixedValueToken(NUMBER_CONJUNCTION_LITERAL, parseGroupValue(lastDigits));

			} else {
				// values that do not require "y"
				prefixedValue = parseGroupValue(lastDigits);
			}

			return new PrefixedValueToken(prefix, prefixedValue);
		}
	}
}
