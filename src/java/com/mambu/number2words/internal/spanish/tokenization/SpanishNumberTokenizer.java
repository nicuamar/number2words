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
 * Tokenizer used to parse a number using English semantics.
 * <p>
 * Before tokenization each number will be split into groups. These groups are, at maximum, 3 digits long.
 * <p>
 * The numbers before and after the decimal point will be tokenized using the same process (so, 22.22 will result in
 * "twenty two and twenty two")
 * <p>
 * This class is thread safe as it stores no state and it deals with immutable data.
 * 
 * 
 * @author aatasiei
 *
 */
public class SpanishNumberTokenizer extends AbstractGroupedValuesTokenizer<SpanishNumberMapping> {

	/**
	 * Default constructor.
	 */
	public SpanishNumberTokenizer() {
		super(SpanishNumberMapping.class, "coma");
	}

	@Override
	protected ValueToken parseGroup(long groupValue, long quantifierValue) {

		if (quantifierValue == 1000L && groupValue == 1L) {
			return mappedValue(quantifierValue);
		}

		if (groupValue == 0) {
			// value is 0, so nothing should be printed
			return nullValue();
		}
		if (quantifierValue > 1) {
			// add quantifier: thousand, million, billion
			return new SuffixedValueToken(parseGroupValue(groupValue), new MappedValueToken(quantifierValue,
					MappingType.GROUP_QUANTIFIER));
		}
		return parseGroupValue(groupValue);
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

			// 20, 30, ... 90, 100, 200, 300, etc..
			final ValueToken prefix = parseSubGroup(groupValue - lastDigits, subGroupQuantifier);

			// adding the last digits to the prefix to get the full number
			if (subGroupQuantifier > 20L && subGroupQuantifier < 100L) {

				return new PrefixedValueToken(prefix, new PrefixedValueToken(new LiteralValueToken("y"),
						parseGroupValue(lastDigits)));

			} else {
				return new PrefixedValueToken(prefix, parseGroupValue(lastDigits));
			}
		}
	}
}
