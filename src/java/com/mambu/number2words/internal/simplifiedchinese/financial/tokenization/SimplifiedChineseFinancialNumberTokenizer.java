package com.mambu.number2words.internal.simplifiedchinese.financial.tokenization;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.mambu.number2words.internal.common.tokenization.AbstractGroupedValuesTokenizer;
import com.mambu.number2words.internal.common.tokenization.SequentialDigitsTokenizer;
import com.mambu.number2words.internal.simplifiedchinese.financial.mapping.SimplifiedChineseFinancialNumberMapping;
import com.mambu.number2words.parsing.interfaces.ValueMapping;
import com.mambu.number2words.parsing.interfaces.ValueToken;
import com.mambu.number2words.parsing.tokenization.GroupListToken;
import com.mambu.number2words.parsing.tokenization.NullValueToken;
import com.mambu.number2words.parsing.tokenization.PrefixedValueToken;

/**
 * Tokenizer used to parse a number using Simplified Chinese semantics.
 * <p>
 * Before tokenization each number will be split into groups. These groups are, at maximum, 4 digits long.
 * <p>
 * The numbers before and after the decimal point will be tokenized using a different process. All the digits after the
 * decimal point will be printed as is, in the order they appear (so, 22.22 will result in "贰拾贰点贰贰")
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
	 * Helper tokenizer that deals with tokenizing the fractional part of the number.
	 */
	private final SequentialDigitsTokenizer fractionalPartTokenizer;

	/**
	 * Default constructor.
	 */
	public SimplifiedChineseFinancialNumberTokenizer() {
		super(SimplifiedChineseFinancialNumberMapping.class, DECIMAL_POINT_SEPARATOR);

		// the fractional part just needs the digits in a sequence
		this.fractionalPartTokenizer = new SequentialDigitsTokenizer();
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * In Simplified Chinese all digits after the decimal point are just listed in the order they appear.
	 */
	@Override
	protected ValueToken tokenizeFractionalPart(final BigInteger number) {
		return fractionalPartTokenizer.tokenize(new BigDecimal(number));
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * This will remove all null tokens at the end of the list and makes sure there are no two consecutive null tokens.
	 * All consecutive null tokens save the first, will be removed.
	 */
	@Override
	protected List<ValueToken> postProcessGroups(List<ValueToken> groups) {

		// remove duplicate null tokens and the last tokens if they are null

		final LinkedList<ValueToken> tokens = new LinkedList<>();

		for (int i = 0; i < groups.size(); ++i) {

			final ValueToken token = groups.get(i);
			final boolean isNullToken = token instanceof NullValueToken;

			if (!isNullToken || (isNullToken && !(tokens.peekLast() instanceof NullValueToken))) {
				tokens.add(token);
			}
		}

		while (tokens.peekLast() instanceof NullValueToken) {
			tokens.removeLast();
		}

		return super.postProcessGroups(tokens);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected ValueToken parseGroupValue(long groupValue) {
		// for Simplified Chinese there are 3 subgroup quantifiers:
		// 10 (for 21 - 99) and 100 (for 100 - 999) and 1000 (for 1000 - 9999)

		List<ValueToken> tokens = new ArrayList<>();

		long toTokenize = groupValue;
		int zeroesFound = 0;

		// empty subgroups need to be marked and remembered as zeroes need to be written by the visitor
		// for example: for 10010 ~ 壹万[零]壹拾 there is a zero (零) before 10.
		// Only one zero (零) is written per group of zeroes in the number.
		//
		// NullValueTokens will be used to mark empty groups.

		for (ValueMapping subGroupQuantifier : getSubGroupQuantifiers()) {
			if (toTokenize >= subGroupQuantifier.getValue()) {

				final long subGroupValue = toTokenize / subGroupQuantifier.getValue();
				toTokenize = toTokenize % subGroupQuantifier.getValue();

				// suffix the digit with the subgroup quantifier
				// 2140 will be 2 - 1000, 1 - 100, 4 - 10
				final ValueToken subGroupToken = suffixValue(mappedValue(subGroupValue), subGroupQuantifier.getValue());

				tokens.add(prefixWithZeroes(zeroesFound, subGroupToken));

				zeroesFound = 0;

			} else {
				++zeroesFound;
			}
		}

		// in case that units need to be written
		if (toTokenize > 0L) {
			final ValueToken token = prefixWithZeroes(zeroesFound, mappedValue(toTokenize));
			tokens.add(token);
		}

		if (!tokens.isEmpty()) {
			return new GroupListToken(tokens);
		}

		throw new IllegalStateException(groupValue + " could not be parsed as group value ");
	}

	/**
	 * Prefixes the passed token with a null token if the passed value is greater then zero.
	 * 
	 * @param zeroesFound
	 *            - integer >= 0.
	 * @param token
	 *            - ValueToken. Not <code>null</code>.
	 * @return a {@link ValueToken} that may be prefixed by a null token.
	 */
	private static ValueToken prefixWithZeroes(int zeroesFound, ValueToken token) {
		if (zeroesFound > 0) {
			token = new PrefixedValueToken(nullValue(), token);
		}
		return token;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected ValueToken parseSubGroup(long groupValue, final long subGroupQuantifier) {

		// FUTURE: (aatasiei) a bit of a Refused Bequest code smell. will look into remerging or separating the behavior
		throw new IllegalStateException("Invalid state for tokenizing Simplified Chinese numbers");
	}
}
