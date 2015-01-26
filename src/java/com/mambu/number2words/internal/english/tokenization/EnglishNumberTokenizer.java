package com.mambu.number2words.internal.english.tokenization;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.mambu.number2words.parsing.interfaces.ValueToken;
import com.mambu.number2words.parsing.tokenization.DecimalValueToken;
import com.mambu.number2words.parsing.tokenization.GroupListToken;
import com.mambu.number2words.parsing.tokenization.MappedValueToken;
import com.mambu.number2words.parsing.tokenization.NullValueToken;
import com.mambu.number2words.parsing.tokenization.PrefixedValueToken;
import com.mambu.number2words.parsing.tokenization.SuffixedValueToken;

/**
 * Tokenizer used to parse a number using English semantics.
 * <p>
 * This class is thread safe as it stores no state and it deals with immutable data.
 * </p>
 * 
 * @author aatasiei
 *
 */
public class EnglishNumberTokenizer {

	private static final int MAX_GROUP_INDEX = 3;
	private static final long GROUP_SEPARATOR = 1_000L;
	private static final BigInteger GROUP_SEPARATOR_BI = BigInteger.valueOf(GROUP_SEPARATOR);
	private static final BigDecimal MAX_GROUP_QUANTIFIER = BigDecimal.TEN.pow(9);

	/**
	 * Tokenizes a non-negative decimal number.
	 * 
	 * @param number
	 *            - the number to tokenize.
	 * @return a {@link ValueToken} instance. Never <code>null</code>.
	 */
	public ValueToken tokenize(BigDecimal number) {

		final BigInteger integerPart = number.toBigInteger();
		ValueToken integerPartToken = tokenize(integerPart);

		if (number.scale() > 0) {

			final BigInteger fractionalPart = getFractional(number, integerPart);
			return new DecimalValueToken(integerPartToken, "and", tokenize(fractionalPart));

		}
		return integerPartToken;
	}

	/**
	 * Tokenizez an integer >= 0.
	 * 
	 * @param number
	 * @return a {@link ValueToken} instance. Never <code>null</code>.
	 */
	private ValueToken tokenize(final BigInteger number) {

		List<ValueToken> groups;

		if (number.equals(BigInteger.ZERO)) {
			// tokenize zero
			groups = Collections.singletonList(mappedValue(0));
		} else {
			// tokenize non-zero
			groups = tokenizeNonZeroValue(number);

		}

		return new GroupListToken(groups);
	}

	private static BigInteger getFractional(BigDecimal number, final BigInteger integerPart) {

		final BigDecimal fractional = number.subtract(new BigDecimal(integerPart)).abs();

		if (fractional.scale() > 0) {

			// the fractional part must be parsed as a normal number
			// 0.20 will be sent as 20, 0.200 as 200, etc..
			return fractional.unscaledValue();
		}

		return BigInteger.ZERO;
	}

	/**
	 * Tokenizes an integer that is >0.
	 * 
	 * @param number
	 *            - the number to tokenize.
	 * @return a list of tokens. Never <code>null</code>.
	 */
	private List<ValueToken> tokenizeNonZeroValue(final BigInteger number) {

		List<ValueToken> groups = new ArrayList<ValueToken>();

		BigInteger toTokenize = number;

		int currentGroup = 0;
		long currentGroupQuantifier = 1;

		while (!toTokenize.equals(BigInteger.ZERO)) {

			if (currentGroup == MAX_GROUP_INDEX && toTokenize.compareTo(GROUP_SEPARATOR_BI) >= 0) {
				// the number overflows over 999,999,999,999
				// re-tokenize the overflow
				addOverflowingNumberTokens(groups, toTokenize);

				break;
			} else {

				BigInteger[] values = toTokenize.divideAndRemainder(GROUP_SEPARATOR_BI);

				toTokenize = values[0];
				BigInteger groupValue = values[1];

				ValueToken token = parseGroup(groupValue.longValue(), currentGroupQuantifier);
				groups.add(token);

				++currentGroup;
				currentGroupQuantifier *= GROUP_SEPARATOR;
			}
		}

		Collections.reverse(groups);

		return groups;
	}

	private void addOverflowingNumberTokens(List<ValueToken> groups, BigInteger toTokenize) {
		groups.add(mappedValue(MAX_GROUP_QUANTIFIER.longValue()));
		groups.add(tokenize(toTokenize));
	}

	/**
	 * 
	 * @param groupValue
	 * @param quantifierValue
	 * @return
	 */
	private static ValueToken parseGroup(long groupValue, long quantifierValue) {
		if (groupValue == 0) {
			// value is 0, so nothing should be printed
			return new NullValueToken();
		}
		if (quantifierValue > 1) {
			// add quantifier: thousand, million, billion
			return new SuffixedValueToken(parseGroupValue(groupValue), mappedValue(quantifierValue));
		}
		return parseGroupValue(groupValue);
	}

	/**
	 * Parses values from 1 to 999.
	 * 
	 * @param groupValue
	 *            - the value to be parsed.
	 * @return a {@link ValueToken} instance.
	 */
	private static ValueToken parseGroupValue(long groupValue) {
		if (groupValue < 21) {
			// 0 - 20
			return mappedValue(groupValue);

		} else if (groupValue < 100) {
			// 21 - 99
			return parseTwentyAndUp(groupValue);

		} else {
			// values 100 and over
			return parseHundreds(groupValue);
		}
	}

	/**
	 * Parse values 100 and up.
	 * 
	 * @param groupValue
	 *            - the value to be parsed.
	 * @return a {@link ValueToken} instance.
	 */
	private static ValueToken parseHundreds(long groupValue) {

		final long lastTwoDigits = groupValue % 100;
		final long hundredsCount = groupValue / 100;

		if (lastTwoDigits == 0) {
			// 100, 200, 300, etc...
			return new SuffixedValueToken(mappedValue(hundredsCount), mappedValue(100));
		} else {
			// 101 to 999 excluding 100, 200, 300, etc...
			return new PrefixedValueToken(parseHundreds(groupValue - lastTwoDigits), parseGroupValue(lastTwoDigits));
		}
	}

	/**
	 * Parse values from 20 up to 99.
	 * 
	 * @param groupValue
	 *            - the value to be parsed.
	 * @return a {@link ValueToken} instance.
	 */
	private static ValueToken parseTwentyAndUp(long groupValue) {
		// handle tens
		final long lastDigit = groupValue % 10;
		if (lastDigit == 0) {
			// 20, 30, 40 ...
			return mappedValue(groupValue);
		} else {

			// all values prefixed by 20, 30, 40 ...
			final long tens = groupValue / 10 * 10;
			return new PrefixedValueToken(mappedValue(tens), mappedValue(lastDigit));

		}
	}

	private static ValueToken mappedValue(long groupValue) {
		return new MappedValueToken(groupValue);
	}
}
