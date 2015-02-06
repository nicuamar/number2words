package com.mambu.number2words.internal.english.tokenization;

import com.mambu.number2words.internal.english.EnglishNumberMapping;
import com.mambu.number2words.parsing.interfaces.ValueToken;
import com.mambu.number2words.parsing.tokenization.PrefixedValueToken;

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
public class EnglishNumberTokenizer extends AbstractGroupedValuesTokenizer<EnglishNumberMapping> {

	/**
	 * String used to separate the numbers before and after the decimal point.
	 */
	private static final String DECIMAL_POINT_SEPARATOR = "and";

	/**
	 * Default constructor.
	 */
	public EnglishNumberTokenizer() {
		super(EnglishNumberMapping.class, DECIMAL_POINT_SEPARATOR);
	}

	@Override
	protected ValueToken parseSubGroup(long groupValue, long subGroupQuantifier) {
		final long lastDigits = groupValue % subGroupQuantifier;
		final long subGroupMultiplier = groupValue / subGroupQuantifier;

		if (lastDigits == 0) {
			// 100, 200, 300, etc...
			return subGroupQuantifier >= 100L ? suffixValue(mappedValue(subGroupMultiplier), subGroupQuantifier)
					: mappedValue(groupValue);
		} else {
			// 101 to 999 excluding 100, 200, 300, etc...
			return new PrefixedValueToken(parseSubGroup(groupValue - lastDigits, subGroupQuantifier),
					parseGroupValue(lastDigits));
		}
	}

}
