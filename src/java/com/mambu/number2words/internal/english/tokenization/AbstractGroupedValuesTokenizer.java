package com.mambu.number2words.internal.english.tokenization;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.mambu.number2words.internal.common.QuantifyingMappingsHelper;
import com.mambu.number2words.parsing.interfaces.NumberTokenizer;
import com.mambu.number2words.parsing.interfaces.ValueMapping;
import com.mambu.number2words.parsing.interfaces.ValueToken;
import com.mambu.number2words.parsing.tokenization.DecimalValueToken;
import com.mambu.number2words.parsing.tokenization.GroupListToken;
import com.mambu.number2words.parsing.tokenization.MappedValueToken;
import com.mambu.number2words.parsing.tokenization.NullValueToken;
import com.mambu.number2words.parsing.tokenization.SuffixedValueToken;

public abstract class AbstractGroupedValuesTokenizer<T extends Enum<T> & ValueMapping> implements NumberTokenizer {

	// TODO: some of these could be deduced from the mapping
	protected int maximumGroupIndex;
	protected BigInteger groupDivider;
	protected BigInteger maximumGroupQuantifier;
	protected String decimalPointSeparator;

	protected Class<T> enumClass;

	protected AbstractGroupedValuesTokenizer(int maximumGroupIndex, BigInteger maximumGroupQuantifier,
			BigInteger groupDivider, String decimalPointSeparator) {

		super();
		this.maximumGroupIndex = maximumGroupIndex;
		this.groupDivider = groupDivider;
		this.maximumGroupQuantifier = maximumGroupQuantifier;
		this.decimalPointSeparator = decimalPointSeparator;
	}

	protected AbstractGroupedValuesTokenizer(Class<T> enumClass, String decimalPointSeparator) {
		super();

		this.maximumGroupIndex = QuantifyingMappingsHelper.groupQuantifiers(enumClass).size();

		this.groupDivider = BigInteger.valueOf(QuantifyingMappingsHelper.minGroupQuantifier(enumClass).getValue());

		this.maximumGroupQuantifier = BigInteger.valueOf(QuantifyingMappingsHelper.maxGroupQuantifier(enumClass)
				.getValue());

		this.decimalPointSeparator = decimalPointSeparator;

		this.enumClass = enumClass;
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

	protected List<T> getSubGroupQuantifiers() {
		return QuantifyingMappingsHelper.subGroupQuantifiers(enumClass);
	}

	protected T getLargestConsecutiveMapping() {
		return QuantifyingMappingsHelper.largestConsecutiveMapping(enumClass);
	}

	/**
	 * Parses values from 1 to 999.
	 * 
	 * @param groupValue
	 *            - the value to be parsed.
	 * @return a {@link ValueToken} instance. Never <code>null</code>.
	 */
	protected ValueToken parseGroupValue(long groupValue) {
		if (groupValue <= getLargestConsecutiveMapping().getValue()) {
			// for English: 0 - 20
			return mappedValue(groupValue);

		} else {
			// for English there are 2 subgroup quantifiers:
			// 10 (for 21 - 99) and 100 (for 100 - 999)
			for (ValueMapping subGroupQuantifier : getSubGroupQuantifiers()) {
				if (groupValue >= subGroupQuantifier.getValue()) {
					return parseSubGroup(groupValue, subGroupQuantifier.getValue());
				}
			}
		}
		throw new IllegalStateException(groupValue + " could not be parsed as group value ");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ValueToken tokenize(BigDecimal number) {

		// to tokenize the value we need to separate the number at the decimal point

		// 1. tokenize the integer left of the decimal point
		final BigInteger integerPart = number.toBigInteger();
		ValueToken integerPartToken = tokenize(integerPart, getGroupDivider());

		if (number.scale() > 0) {

			// 2. if there are digits to the right of the decimal point, tokenize them
			final BigInteger fractionalPart = getFractional(number, integerPart);
			final ValueToken fractionalPartToken = tokenize(fractionalPart, getGroupDivider());

			// 3. merge the two parts
			return new DecimalValueToken(integerPartToken, getDecimalSeparator(), fractionalPartToken);

		}

		// no values to the right of the decimal point. return the integer token.
		return integerPartToken;
	}

	protected String getDecimalSeparator() {
		return decimalPointSeparator;
	}

	/**
	 * Tokenizez an integer >= 0.
	 * 
	 * @param number
	 * @return a {@link ValueToken} instance. Never <code>null</code>.
	 */
	private ValueToken tokenize(final BigInteger number, final BigInteger groupSeparator) {

		List<ValueToken> groups;

		if (number.equals(BigInteger.ZERO)) {
			// tokenize zero
			groups = Collections.singletonList(mappedValue(0));
		} else {
			// tokenize non-zero
			groups = tokenizeNonZeroValue(number, groupSeparator);

		}

		return new GroupListToken(groups);
	}

	protected static ValueToken nullValue() {
		return new NullValueToken();
	}

	/**
	 * Tokenizes an integer that is >0.
	 * 
	 * @param number
	 *            - the number to tokenize.
	 * @return a list of tokens. Never <code>null</code>.
	 */
	private List<ValueToken> tokenizeNonZeroValue(final BigInteger number, final BigInteger groupSeparator) {

		final List<ValueToken> groups = new ArrayList<ValueToken>();

		BigInteger toTokenize = number;

		final long longGroupSeparator = groupSeparator.longValue();

		int currentGroup = 0; // group index
		long currentGroupQuantifier = 1; // group quantifier {1, 1_000, 1_000_000, etc...}

		while (!toTokenize.equals(BigInteger.ZERO)) {

			if (currentGroup == getMaximumGroupIndex() && toTokenize.compareTo(groupSeparator) >= 0) {
				// the number overflows over 999,999,999,999
				// re-tokenize the overflow
				addOverflowingNumberTokens(groups, toTokenize, groupSeparator);

				break;
			} else {
				// tokenize the current group and prepare the rest of the number for tokenization

				// values[0] -> next number to tokenize
				// values[1] -> the current group value (3 digits maximum)
				BigInteger[] values = toTokenize.divideAndRemainder(groupSeparator);

				toTokenize = values[0];
				BigInteger groupValue = values[1];

				groups.add(parseGroup(groupValue.longValue(), currentGroupQuantifier));

				++currentGroup;
				currentGroupQuantifier *= longGroupSeparator;
			}
		}

		// the group tokens were added in reverse order:
		// {UNIT, THOUSAND, MILLION, BILLION}, but should be {BILLION, MILLION, THOUSAND, UNIT}
		if (groups.size() > 1) {
			Collections.reverse(groups);
		}

		return groups;
	}

	protected BigInteger getGroupDivider() {
		return groupDivider;
	}

	protected int getMaximumGroupIndex() {
		return maximumGroupIndex;
	}

	/**
	 * Adds the maximum group quantifier and then the tokenized number to the list.
	 * 
	 * @param groups
	 *            - the list of group tokens.
	 * @param toTokenize
	 *            - the number to tokenize.
	 */
	private void addOverflowingNumberTokens(final List<ValueToken> groups, BigInteger toTokenize,
			BigInteger groupSeparator) {
		// since the groups are added in reverse order
		// the "billion" token needs to be added before the rest of the number
		groups.add(mappedValue(getMaximumGroupQuantifier().longValue()));
		groups.add(tokenize(toTokenize, groupSeparator));
	}

	protected BigInteger getMaximumGroupQuantifier() {
		return maximumGroupQuantifier;
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
	protected ValueToken parseGroup(long groupValue, long quantifierValue) {
		if (groupValue == 0) {
			// value is 0, so nothing should be printed
			return nullValue();
		}
		if (quantifierValue > 1) {
			// add quantifier: thousand, million, billion
			return suffixValue(parseGroupValue(groupValue), quantifierValue);
		}
		return parseGroupValue(groupValue);
	}

	protected abstract ValueToken parseSubGroup(long groupValue, final long subGroupQuantifier);

	/**
	 * Factory for {@link ValueToken} that can be directly mapped to a word.
	 * 
	 * @param mappedValue
	 *            - the {@link Long} value that should have a mapping described.
	 * @return {@link ValueToken} instance. Never <code>null</code>.
	 */
	protected static ValueToken mappedValue(long mappedValue) {
		return new MappedValueToken(mappedValue);
	}

	/**
	 * Shortcut to creating a new {@link ValueToken} that has {@code quantifierValue} as a suffix for {@code groupValue}
	 * . This creates tokens that can be used when tokenizing <i>groups</i> (i.e. "one thousand" as 1000, "壹萬" as 10000,
	 * etc..)
	 * 
	 * @param groupValue
	 *            - the ValueToken representing the value of the group. Not <code>null</code>.
	 * @param quantifierValue
	 *            - the value of the group quantifier (it should map to a literal - for example 1000000 should map to
	 *            "million" for English). Not <code>null</code>.
	 * @return {@link ValueToken} instance. Never <code>null</code>.
	 */
	protected static ValueToken suffixValue(final ValueToken groupValueToken, long quantifierValue) {
		return new SuffixedValueToken(groupValueToken, mappedValue(quantifierValue));
	}

}
