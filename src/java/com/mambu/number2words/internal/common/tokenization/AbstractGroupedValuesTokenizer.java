package com.mambu.number2words.internal.common.tokenization;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import com.mambu.number2words.internal.common.mapping.QuantifyingMappingsHelper;
import com.mambu.number2words.parsing.interfaces.NumberTokenizer;
import com.mambu.number2words.parsing.interfaces.ValueMapping;
import com.mambu.number2words.parsing.interfaces.ValueMapping.MappingType;
import com.mambu.number2words.parsing.interfaces.ValueToken;
import com.mambu.number2words.parsing.tokenization.GroupListToken;
import com.mambu.number2words.parsing.tokenization.LiteralValueToken;
import com.mambu.number2words.parsing.tokenization.MappedValueToken;
import com.mambu.number2words.parsing.tokenization.NullValueToken;
import com.mambu.number2words.parsing.tokenization.SuffixedValueToken;

/**
 * Abstract class that defines a common way to tokenize values that can be deconstructed into groups and subgroups.
 * 
 * @author aatasiei
 *
 * @param <T>
 *            the {@link ValueMapping} {@link Enum} type from group, sub-group and mapping information will be
 *            retrieved.
 */
public abstract class AbstractGroupedValuesTokenizer<T extends Enum<T> & ValueMapping> implements NumberTokenizer {

	/**
	 * Maximum number for the group index (0 based). In English, for example, 3 would represent billions (if that is the
	 * largest quantifier).
	 */
	protected final int maximumGroupIndex;

	/**
	 * Value used to separate the number into groups. In English, for example, groups are 1000 based. In Chinese, they
	 * are 10000 based.
	 */
	protected final BigInteger groupingDivisor;

	/**
	 * Maximum value a group quantifier can have. In English, for example, it can be limited to 1 billion.
	 */
	protected final BigInteger largestGroupQuantifier;

	/**
	 * The largest consecutive mapping. In English, for example this would be "twenty" (20).
	 */
	protected final Long largestConsecutiveMapping;

	/**
	 * String used to separate the numbers before and after the decimal point.
	 */
	protected final String decimalPointSeparator;

	/**
	 * The sub-group quantifiers in descending order. For example, in English: (HUNDRED, TEN) (units subgroups by
	 * default so ONE is not added).
	 * 
	 * @see QuantifyingMappingsHelper#subGroupQuantifiers(Class)
	 */
	private final List<T> subGroupQuantifiers;

	/**
	 * {@link Enum} class instance used to map numbers to words. This is required to gather group divisor information.
	 */
	private Class<T> enumClass;

	/**
	 * Default constructor.
	 * 
	 * @param enumClass
	 *            - the {@link ValueMapping} {@link Enum} type from which to retrieve group, sub-group and mapping
	 *            information. All the mapped values of the {@link Enum} must be in strict ascending order. Not
	 *            <code>null</code>.
	 * @param decimalPointSeparator
	 *            - a {@link String} that will be used to separate different <i>words</i>. Not <code>null</code>.
	 */
	protected AbstractGroupedValuesTokenizer(final Class<T> enumClass, final String decimalPointSeparator) {

		this.decimalPointSeparator = Objects.requireNonNull(decimalPointSeparator);

		this.enumClass = enumClass;

		// initialize the values that are dependent on the value mappings

		this.maximumGroupIndex = QuantifyingMappingsHelper.groupQuantifiers(enumClass).size();

		this.groupingDivisor = BigInteger.valueOf(QuantifyingMappingsHelper.minGroupQuantifier(enumClass).getValue());

		this.largestGroupQuantifier = BigInteger.valueOf(QuantifyingMappingsHelper.maxGroupQuantifier(enumClass)
				.getValue());

		this.largestConsecutiveMapping = QuantifyingMappingsHelper.largestConsecutiveMapping(enumClass).getValue();

		this.subGroupQuantifiers = QuantifyingMappingsHelper.subGroupQuantifiers(enumClass);
	}

	/**
	 * Parses the value of a sub-group. A group value can be composed of multiple sub-groups.
	 * <p>
	 * Sub-groups are mappings marked with {@link MappingType#SUBGROUP_QUANTIFIER}. For English, this would mean
	 * combination of tens and hundreds.
	 * 
	 * @see QuantifyingMappingsHelper#subGroupQuantifiers(Class)
	 * @param subGroupValue
	 *            - the value to be parsed.
	 * @param subGroupQuantifier
	 *            - the value of this sub-group's quantifier (i.e: 1, 10, 100, etc...)
	 * @return a {@link ValueToken} instance. Never <code>null</code>.
	 */
	protected abstract ValueToken parseSubGroup(long subGroupValue, final long subGroupQuantifier);

	/**
	 * String used to separate the numbers before and after the decimal point.
	 * 
	 * @return a {@link String} instance. Never <code>null</code>.
	 */
	protected String getDecimalSeparator() {
		return decimalPointSeparator;
	}

	/**
	 * The value of the smallest group that can be used to arithmetically divide a number into multiple sections.
	 * <p>
	 * Based on the current and the previous group's quantifiers we <i>decide</i> the group size.
	 * <p>
	 * While for most languages the groups are of equals sizes, there are exceptions. For example, Spanish has million -
	 * 10<sup>6</sup> and then billion - 10<sup>12</sup>. The 10<sup>6</sup> group range value must be handled in the
	 * million group.
	 * 
	 * @param currentGroupIndex
	 *            - the current group index. For example 0 is units, 1 can be thousands, 2 can be millions.
	 * 
	 * @see QuantifyingMappingsHelper#minGroupQuantifier(Class).
	 * @return the value of the grouping divisor.
	 */
	protected BigInteger getGroupingDivisor(final int currentGroupIndex) {

		if (currentGroupIndex == 0 || currentGroupIndex >= maximumGroupIndex) {
			// the first group is the minimum

			return BigInteger.valueOf(QuantifyingMappingsHelper.minGroupQuantifier(enumClass).getValue());

		} else {

			// the "difference" between the following group and the current group is the divisor
			// this is because some groups can be sometimes larger than others.
			// for example, in Spanish, after 1 million (10^6) the next group is 1 billion (10^12)

			// quantifiers are in reverse order
			final List<T> quantifiers = QuantifyingMappingsHelper.groupQuantifiers(enumClass);

			final int lastGroupIndex = quantifiers.size() - 1;

			final Long currentGroupQuantifier = quantifiers.get(lastGroupIndex - currentGroupIndex + 1).getValue();
			final Long nextGroupQuantifier = quantifiers.get(lastGroupIndex - currentGroupIndex).getValue();

			return BigInteger.valueOf(nextGroupQuantifier / currentGroupQuantifier);
		}
	}

	/**
	 * The index of the largest group quantifier (when iterating over all of them in ascending order). For example, in
	 * English, MILLION has index 2 (0 based counting - 0 for units, 1 for THOUSAND).
	 * 
	 * @return the maximum group index.
	 */
	protected int getMaximumGroupIndex() {
		return maximumGroupIndex;
	}

	/**
	 * The value of the largest group quantifier (when iterating over all of them in ascending order). For example, in
	 * English, a chosen maximum value could be 1 trillion.
	 * 
	 * @return the maximum group quantifier.
	 */
	protected BigInteger getMaximumGroupQuantifier() {
		return largestGroupQuantifier;
	}

	/**
	 * @see QuantifyingMappingsHelper#subGroupQuantifiers(Class)
	 * @return the sub-group quantifiers of the associated mapping in descending order. Never <code>null</code>.
	 */
	protected List<T> getSubGroupQuantifiers() {
		return Collections.unmodifiableList(subGroupQuantifiers);
	}

	/**
	 * Parses values smaller than the {@link #groupingDivisor} (which is also the smallest group quantifier).
	 * 
	 * @param groupValue
	 *            - the value to be parsed.
	 * @return a {@link ValueToken} instance. Never <code>null</code>.
	 */
	protected ValueToken parseGroupValue(long groupValue) {
		if (groupValue <= largestConsecutiveMapping) {
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
	 * Parses group values.
	 * <p>
	 * Groups are mappings marked with {@link MappingType#GROUP_QUANTIFIER}. For English this means anywhere from 0 to
	 * 999.
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

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ValueToken tokenize(final BigDecimal number) {

		if (BigDecimal.ZERO.compareTo(number) > 0) {
			// negative number
			throw new IllegalArgumentException("Negative numbers are not supported");
		}

		// to tokenize the value we need to separate the number at the decimal point

		// 1. tokenize the integer left of the decimal point
		final BigInteger integerPart = number.toBigInteger();
		ValueToken integerPartToken = tokenize(integerPart);

		ValueToken result = integerPartToken;

		if (number.scale() > 0) {

			// 2. if there are digits to the right of the decimal point, tokenize them
			final BigInteger fractionalPart = getFractional(number, integerPart);
			final ValueToken fractionalPartToken = tokenizeFractionalPart(fractionalPart);

			// 3. merge the two parts with the decimal separator in-between
			final ValueToken[] tokensForDecimalValue = new ValueToken[] { integerPartToken,
					new LiteralValueToken(getDecimalSeparator()), fractionalPartToken };

			result = new GroupListToken(Arrays.asList(tokensForDecimalValue));
		}

		// no values to the right of the decimal point. return the integer token.
		return result;
	}

	/**
	 * Tokenizes the fractional part of the number passed to {@link #tokenize(BigDecimal)}.
	 * <p>
	 * By default the same process of tokenization used for the integer part is used for the fractional part as well.
	 * <p>
	 * Override this if a special tokenizing process is required for the digits after the decimal point.
	 * 
	 * @param number
	 *            - the number to tokenize. Not <code>null</code>.
	 * @return a {@link ValueToken} instance. Never <code>null</code>.
	 */
	protected ValueToken tokenizeFractionalPart(final BigInteger number) {
		return tokenize(number);
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

	/**
	 * Tokenizes an integer that is >0.
	 * 
	 * @param number
	 *            - the number to tokenize.
	 * @return a list of tokens. Never <code>null</code>.
	 */
	private List<ValueToken> tokenizeNonZeroValue(final BigInteger number) {

		final List<ValueToken> groups = parseLargeValue(number);

		// the group tokens were added in reverse order:
		// {UNIT, THOUSAND, MILLION, BILLION}, but should be {BILLION, MILLION, THOUSAND, UNIT}
		if (groups.size() > 1) {
			Collections.reverse(groups);
		}

		return postProcessGroups(groups);
	}

	/**
	 * Parses a number (>0) and returns the list of tokens (in reverse order) that resulted.
	 * 
	 * @param number
	 *            - the {@link BigInteger} to parse. Not <code>null</code>.
	 * @return a list of {@link ValueToken}s in reverse order. Never <code>null</code>.
	 */
	private List<ValueToken> parseLargeValue(final BigInteger number) {

		final List<ValueToken> groups = new ArrayList<ValueToken>();

		BigInteger toTokenize = number;

		int currentGroup = 0; // group index
		long currentGroupQuantifier = 1; // group quantifier {1, 1_000, 1_000_000, etc...}

		BigInteger localGroupingDivisor = getGroupingDivisor(currentGroup);
		long longGroupDivisor = localGroupingDivisor.longValue();

		while (!toTokenize.equals(BigInteger.ZERO)) {

			if (currentGroup == getMaximumGroupIndex() && toTokenize.compareTo(localGroupingDivisor) >= 0) {
				// the number overflows over 999,999,999,999
				// re-tokenize the overflow
				addOverflowingNumberTokens(groups, toTokenize);

				break;
			} else {
				// tokenize the current group and prepare the rest of the number for tokenization

				// values[0] -> next number to tokenize
				// values[1] -> the current group value (3 digits maximum)
				BigInteger[] values = toTokenize.divideAndRemainder(localGroupingDivisor);

				toTokenize = values[0];
				BigInteger groupValue = values[1];

				if (groupValue.compareTo(groupingDivisor) >= 0) {
					// this is for groups that are larger than the default
					groups.add(suffixValue(tokenize(groupValue), longGroupDivisor));
				} else {
					groups.add(parseGroup(groupValue.longValue(), currentGroupQuantifier));
				}

				++currentGroup;
				currentGroupQuantifier *= longGroupDivisor;

				// update the divisor
				localGroupingDivisor = getGroupingDivisor(currentGroup);
				longGroupDivisor = localGroupingDivisor.longValue();
			}
		}
		return groups;
	}

	/**
	 * Post processes a list of tokens representing groups before being returned as a {@link GroupListToken}. This
	 * happens as a result of parsing a non-zero positive integer value.
	 * <p>
	 * By default this method does nothing.
	 * <p>
	 * Override this if you need to post process some group values.
	 * 
	 * @param groups
	 *            the group list. Not <code>null</code>.
	 * @return the list of group tokens, after being processed. Never <code>null</code>.
	 */
	protected List<ValueToken> postProcessGroups(final List<ValueToken> groups) {
		return Objects.requireNonNull(groups);
	}

	/**
	 * Adds the maximum group quantifier and then the tokenized number to the list.
	 * 
	 * @param groups
	 *            - the list of group tokens.
	 * @param toTokenize
	 *            - the number to tokenize.
	 */
	private void addOverflowingNumberTokens(final List<ValueToken> groups, BigInteger toTokenize) {
		// since the groups are added in reverse order
		// the "trillion" token needs to be added before the rest of the number
		groups.add(mappedValue(getMaximumGroupQuantifier().longValue()));
		groups.add(tokenize(toTokenize));
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
	 * Provides a shorthand to create {@link NullValueToken} instances.
	 * 
	 * @return {@link ValueToken} instance.
	 */
	protected static ValueToken nullValue() {
		return new NullValueToken();
	}

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
	 * . This creates tokens that can be used when tokenizing <i>groups</i> (i.e. "one thousand" as 1000, "万" as 10000,
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
