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
public class EnglishNumberTokenizer {

	/**
	 * Maximum number for the group index (0 based, 3 represents billions).
	 */
	private static final int MAX_GROUP_INDEX = 3;
	/**
	 * Value used to separate the number into groups. For English groups are 1000 based.
	 */
	private static final long GROUP_SEPARATOR = 1_000L;
	/**
	 * {@link #GROUP_SEPARATOR} as a BigInteger.
	 */
	private static final BigInteger GROUP_SEPARATOR_BI = BigInteger.valueOf(GROUP_SEPARATOR);
	/**
	 * Maximum value a group quantifier can have (for English we limit it to 1 billion).
	 */
	private static final BigDecimal MAX_GROUP_QUANTIFIER = BigDecimal.TEN.pow(9);
	/**
	 * String used to separate the numbers before and after the decimal point.
	 */
	private static final String DECIMAL_POINT_SEPARATOR = "and";

	/**
	 * Tokenizes a non-negative decimal number.
	 * 
	 * @param number
	 *            - the number to tokenize.
	 * @return a {@link ValueToken} instance. Never <code>null</code>.
	 */
	public ValueToken tokenize(BigDecimal number) {

		// to tokenize the value we need to separate the number at the decimal point

		// 1. tokenize the integer left of the decimal point
		final BigInteger integerPart = number.toBigInteger();
		ValueToken integerPartToken = tokenize(integerPart);

		if (number.scale() > 0) {

			// 2. if there are digits to the right of the decimal point, tokenize them
			final BigInteger fractionalPart = getFractional(number, integerPart);
			final ValueToken fractionalPartToken = tokenize(fractionalPart);

			// 3. merge the two parts
			return new DecimalValueToken(integerPartToken, DECIMAL_POINT_SEPARATOR, fractionalPartToken);

		}

		// no values to the right of the decimal point. return the integer token.
		return integerPartToken;
	}

	/**
	 * Tokenizez an integer >= 0.
	 * 
	 * @param number
	 * @return a {@link ValueToken} instance. Never <code>null</code>.
	 */
	private static ValueToken tokenize(final BigInteger number) {

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

	/**
	 * For a {@link BigDecimal}, gets the value to the right of the decimal point.
	 * 
	 * @param number
	 *            - the number for which to get the fractional part.
	 * @param integerPart
	 *            - the integer part of {@code number}.
	 * @return the fractional part of the number as a {@link BigInteger}
	 */
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
	private static List<ValueToken> tokenizeNonZeroValue(final BigInteger number) {

		final List<ValueToken> groups = new ArrayList<ValueToken>();

		BigInteger toTokenize = number;

		int currentGroup = 0; // group index
		long currentGroupQuantifier = 1; // group quantifier {1, 1_000, 1_000_000, etc...}

		while (!toTokenize.equals(BigInteger.ZERO)) {

			if (currentGroup == MAX_GROUP_INDEX && toTokenize.compareTo(GROUP_SEPARATOR_BI) >= 0) {
				// the number overflows over 999,999,999,999
				// re-tokenize the overflow
				addOverflowingNumberTokens(groups, toTokenize);

				break;
			} else {
				// tokenize the current group and prepare the rest of the number for tokenization

				// values[0] -> next number to tokenize
				// values[1] -> the current group value (3 digits maximum)
				BigInteger[] values = toTokenize.divideAndRemainder(GROUP_SEPARATOR_BI);

				toTokenize = values[0];
				BigInteger groupValue = values[1];

				groups.add(parseGroup(groupValue.longValue(), currentGroupQuantifier));

				++currentGroup;
				currentGroupQuantifier *= GROUP_SEPARATOR;
			}
		}

		// the group tokens were added in reverse order:
		// {UNIT, THOUSAND, MILLION, BILLION}, but should be {BILLION, MILLION, THOUSAND, UNIT}
		if (groups.size() > 1) {
			Collections.reverse(groups);
		}

		return groups;
	}

	/**
	 * Adds the maximum group quantifier and then the tokenized number to the list.
	 * 
	 * @param groups
	 *            - the list of group tokens.
	 * @param toTokenize
	 *            - the number to tokenize.
	 */
	private static void addOverflowingNumberTokens(final List<ValueToken> groups, BigInteger toTokenize) {
		// since the groups are added in reverse order
		// the "billion" token needs to be added before the rest of the number
		groups.add(mappedValue(MAX_GROUP_QUANTIFIER.longValue()));
		groups.add(tokenize(toTokenize));
	}

	/**
	 * Parses group values. For English this means anywhere from 0 to 999.
	 * 
	 * @param groupValue
	 *            - the value to be parsed.
	 * @param quantifierValue
	 *            - the value of this group's quantifier (i.e: 1, 1000, 1000000, etc..)
	 * @return a {@link ValueToken} instance. Never <code>null</code>.
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
	 * @return a {@link ValueToken} instance. Never <code>null</code>.
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
	 * @return a {@link ValueToken} instance. Never <code>null</code>.
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
	 * @return a {@link ValueToken} instance. Never <code>null</code>.
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

	/**
	 * Factory for {@link ValueToken} that can be directly mapped to a word.
	 * 
	 * @param mappedValue
	 *            - the {@link Long} value that should have a mapping described.
	 * @return {@link ValueToken} instance. Never <code>null</code>.
	 */
	private static ValueToken mappedValue(long mappedValue) {
		return new MappedValueToken(mappedValue);
	}
}
