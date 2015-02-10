package com.mambu.number2words.internal.simplifiedchinese.financial.tokenization;

import com.mambu.number2words.internal.common.tokenization.AbstractGroupedValuesTokenizer;
import com.mambu.number2words.internal.simplifiedchinese.financial.mapping.SimplifiedChineseFinancialNumberMapping;
import com.mambu.number2words.parsing.interfaces.ValueToken;
import com.mambu.number2words.parsing.tokenization.PrefixedValueToken;

/**
 * Tokenizer used to parse a number using Simplified Chinese semantics.
 * <p>
 * Before tokenization each number will be split into groups. These groups are, at maximum, 4 digits long.
 * <p>
 * The numbers before and after the decimal point will be tokenized using the same process (so, 22.22 will result in
 * "贰拾贰点贰拾贰")
 * <p>
 * This class is thread safe as it stores no state and it deals with immutable data.
 * 
 * 
 * @author aatasiei
 *
 */
public class SimplifiedChineseFinancialNumberTokenizer extends
		AbstractGroupedValuesTokenizer<SimplifiedChineseFinancialNumberMapping> {

	/**
	 * String used to separate the numbers before and after the decimal point.
	 */
	private static final String DECIMAL_POINT_SEPARATOR = "点";

	/**
	 * Default constructor.
	 */
	public SimplifiedChineseFinancialNumberTokenizer() {
		super(SimplifiedChineseFinancialNumberMapping.class, DECIMAL_POINT_SEPARATOR);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected ValueToken parseSubGroup(long groupValue, final long subGroupQuantifier) {

		// the remainder of the division of the groupValue with the sub group quantifier
		// for example:
		// 1. group value = 123 and quantifier = 100 => remainder is 23
		// 2. group value is 3492 and quantifier = 1000 => remainder is 492
		//
		// these values might need to be should be tokenized separately from this subgroup as they may be part of
		// another subgroup:
		// for example:
		// 1. group value = 4954 will have 3 sub groups (4 x 1000) + (9 x 100) + (5 x 10) + 4
		//
		// 2. group value = 230 will have 2 sub groups (2 x 100) + (3 x 10)
		//
		final long lastDigits = groupValue % subGroupQuantifier;

		// the value multiplied with the sub group quantifier
		// for example:
		// 1. 423 would be tokenized as 4 x 100 ( + ( 2 x 10 ( + 3))). the multiplier is 4.
		//
		// 2. 13 would be tokenized as 1 x 10 ( + 3) and the multiplier is 1.
		//
		final long subGroupMultiplier = groupValue / subGroupQuantifier;

		if (lastDigits == 0L) {

			// for 10 the "1" is not shown (1 x 10), for the others (100, 1000), it is shown (1 x 100, 1 x 1000)
			if (subGroupMultiplier > 1L || subGroupQuantifier > 10L) {

				// 20, 30.. 90 or 100, 200.. 900 or 1000, 2000.. 9000
				return suffixValue(mappedValue(subGroupMultiplier), subGroupQuantifier);
			} else {

				// 10
				return mappedValue(groupValue);
			}
		} else {
			// 101 to 9999 excluding numbers that end in all zeroes (200, 300.. 900, 1000, 2000.. 9000)
			return new PrefixedValueToken(parseSubGroup(groupValue - lastDigits, subGroupQuantifier),
					parseGroupValue(lastDigits));
		}
	}

}
