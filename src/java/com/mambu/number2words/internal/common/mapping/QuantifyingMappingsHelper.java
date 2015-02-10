package com.mambu.number2words.internal.common.mapping;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.mambu.number2words.parsing.interfaces.ValueMapping;

/**
 * Utilities class that helps with analyzing {@link ValueMapping} {@link Enum}s detailing the string literals specific
 * to a certain language.
 * <p>
 * Presumes that the {@link Enum} has all the mappings in strict ascending order, with the smallest being zero and the
 * largest being the largest group quantifier (for example, 1 billion for English (see {@link #groupQuantifiers(Class)}
 * ).
 * 
 * @author aatasiei
 * 
 */
public class QuantifyingMappingsHelper {

	/**
	 * Private constructor. No instances allowed.
	 */
	private QuantifyingMappingsHelper() {
		// utilities class
	}

	/**
	 * Retrieves the group quantifiers from an {@link ValueMapping} {@link Enum} in the descending order of their
	 * values.
	 * <p>
	 * <b>Note:</b> The units group does not have a quantifier (it is presumed 1). Only quantifiers different than 1
	 * will be returned!
	 * <p>
	 * Groups have values smaller the smallest group quantifier (usually 1000 or 10000), and can be used - in
	 * combination with group quantifiers - to represent all the positive numbers.
	 * <p>
	 * <b>For example:</b><br/>
	 * For English, each group has a maximum of 3 digits and covers values from 0 to 999:
	 * 
	 * <pre>
	 *   5,123,234.9023 would be grouped as
	 * <u>|   GROUP VALUES   | GROUP QUANTIFIERS |   DETAILS   </u>
	 * | 5 x              |  (1,000,000)      | millions group
	 * |   123 x          |  (1,000)          | thousands group
	 * |       234 x      |  (1)              | units group (this group is implicit and does not use a quantifier)
	 * |           9 x    |  (1,000)          | thousands group
	 * |             23 x |  (1)              | units group (this group is implicit and does not use a quantifier)
	 * </pre>
	 * 
	 * @param enumClass
	 *            - the {@link ValueMapping} {@link Enum} type from which to retrieve the group quantifiers. All the
	 *            mapped values of the {@link Enum} must be in strict ascending order.
	 * @return an immutable {@link List} containing all the instances that are {@link ValueMapping#isGroupQuantifier()
	 *         group quantifiers} (the quantifiers will be in descending order). Never <code>null</code>.
	 * @throws IllegalArgumentException
	 *             if the values of the {@link Enum} are not in strict ascending order.
	 */
	public static <T extends Enum<T> & ValueMapping> List<T> groupQuantifiers(final Class<T> enumClass) {

		// validated all mappings are in strict ascending order (otherwise the enum is malformed)
		validateEnum(enumClass);

		final List<T> result = new ArrayList<>();

		for (final T mapping : enumClass.getEnumConstants()) {

			if (mapping.isGroupQuantifier()) {
				result.add(mapping);
			}
		}

		// values are ascending in the enum
		Collections.reverse(result);

		return result.isEmpty() ? Collections.<T> emptyList() : Collections.unmodifiableList(result);
	}

	/**
	 * Returns the largest consecutive value mapped in the {@link ValueMapping} {@link Enum}. This represents the upper
	 * bound of the numeric values that can be directly converted into words (the lower bound is 0).
	 * <p>
	 * Basically, everything between 0 and {@link #largestConsecutiveMapping(Class)} can be directly evaluated to string
	 * literals.
	 * <p>
	 * For English, this value should be 20 (every number from <i>zero</i> to <i>twenty</i> has its own string literal).
	 * 
	 * @param enumClass
	 *            - the {@link ValueMapping} {@link Enum} type from which to retrieve the group quantifiers. All the
	 *            mapped values of the {@link Enum} must be in strict ascending order.
	 * @return the largest consecutive mapping (this should usually be the mapping for 10.)
	 * @throws IllegalArgumentException
	 *             if the values of the {@link Enum} are not in strict ascending order.
	 */
	public static <T extends Enum<T> & ValueMapping> T largestConsecutiveMapping(final Class<T> enumClass) {

		// validated all mappings are in strict ascending order (otherwise the enum is malformed)
		validateEnum(enumClass);

		T last = null;

		for (final T mapping : enumClass.getEnumConstants()) {

			if (last != null && last.getValue() != mapping.getValue() - 1L) {
				// look for the first non-consecutive value
				// where non-consecutive <=> current value - previous value > 1
				break;
			}

			last = mapping;
		}

		return last;
	}

	/**
	 * Retrieves the sub-group quantifiers from an {@link ValueMapping} {@link Enum} in the descending order of their
	 * values.
	 * <p>
	 * Subgroups should have values smaller the smallest subgroup quantifier (usually 10 or 100), and can be used - in
	 * combination with sub-group quantifiers - to represent all the positive values that might appear as a group value
	 * (see {@link #groupQuantifiers(Class)}.
	 * <p>
	 * <b>For example:</b><br/>
	 * 6539 in Simplified Chinese (where each group has a maximum of 4 digits and covers values from 0 to 9999) would be
	 * deconstructed as:
	 * 
	 * <pre>
	 *   6539
	 * <u>| SUBGROUP VALUES | SUBGROUP QUANTIFIERS |   DETAILS   </u>
	 * | 6 x             |  (1000)              | thousands sub-group
	 * |  5 x            |  (100)               | hundreds sub-group
	 * |   3 x           |  (10)                | tens sub-group
	 * |    9 x          |  (1)                 | units sub-group (this group is implicit and does not use a quantifier)
	 * </pre>
	 * 
	 * @param enumClass
	 *            - the {@link ValueMapping} {@link Enum} type from which to retrieve the sub-group quantifiers. All the
	 *            mapped values of the {@link Enum} must be in strict ascending order.
	 * @return an immutable {@link List} containing all the instances that are
	 *         {@link ValueMapping#isSubGroupQuantifier() sub-group quantifiers} (the quantifiers will be in descending
	 *         order). Never <code>null</code>.
	 * @throws IllegalArgumentException
	 *             if the values of the {@link Enum} are not in strict ascending order.
	 */
	public static <T extends Enum<T> & ValueMapping> List<T> subGroupQuantifiers(final Class<T> enumClass) {

		// validated all mappings are in strict ascending order (otherwise the enum is malformed)
		validateEnum(enumClass);

		final List<T> result = new ArrayList<>();

		for (final T mapping : enumClass.getEnumConstants()) {

			if (mapping.isSubGroupQuantifier()) {
				result.add(mapping);
			}
		}

		// values are ascending in the enum
		Collections.reverse(result);

		return result.isEmpty() ? Collections.<T> emptyList() : Collections.unmodifiableList(result);
	}

	/**
	 * Retrieves the smallest {@link ValueMapping#isGroupQuantifier() group quantifier} mapping. This should generally
	 * be the mapping for 1000 or 10000.
	 * <p>
	 * <b>Note:</b> The units group does not have a quantifier (it is presumed 1). Only quantifiers different than 1
	 * will be returned!
	 * <p>
	 * 
	 * @param enumClass
	 *            - the {@link ValueMapping} {@link Enum} type from which to retrieve the group mapping. All the mapped
	 *            values of the {@link Enum} must be in strict ascending order.
	 * @return the first group mapping encountered in the {@link Enum}. Never <code>null</code>.
	 * @throws IllegalArgumentException
	 *             if
	 *             <ul>
	 *             <li>the values of the {@link Enum} are not in strict ascending order or, <li>no group quantifier is
	 *             found (the {@link Enum} has no group mappings).
	 */
	public static <T extends Enum<T> & ValueMapping> T minGroupQuantifier(final Class<T> enumClass) {

		// validated all mappings are in strict ascending order (otherwise the enum is malformed)
		validateEnum(enumClass);

		for (final T mapping : enumClass.getEnumConstants()) {

			if (mapping.isGroupQuantifier()) {
				return mapping;
			}
		}

		throw new IllegalArgumentException(enumClass.getSimpleName() + " does not contain any group quantifiers");
	}

	/**
	 * Retrieves the largest {@link ValueMapping#isGroupQuantifier() group quantifier} mapping. This must be the last
	 * mapping in the {@link Enum}.
	 * <p>
	 * <b>Note:</b> The units group does not have a quantifier (it is presumed 1). Only quantifiers different than 1
	 * will be returned!
	 * <p>
	 * 
	 * @param enumClass
	 *            - the {@link ValueMapping} {@link Enum} type from which to retrieve the group mapping. All the mapped
	 *            values of the {@link Enum} must be in strict ascending order.
	 * @return the last group mapping encountered in the {@link Enum}. Never <code>null</code>.
	 * @throws IllegalArgumentException
	 *             if
	 *             <ul>
	 *             <li>the values of the {@link Enum} are not in strict ascending order or, <li>the last {@link Enum}
	 *             instance is not a group mapping.
	 */
	public static <T extends Enum<T> & ValueMapping> T maxGroupQuantifier(final Class<T> enumClass) {

		// validated all mappings are in strict ascending order (otherwise the enum is malformed)
		validateEnum(enumClass);

		final T[] mappings = enumClass.getEnumConstants();

		if (!mappings[mappings.length - 1].isGroupQuantifier()) {
			throw new IllegalArgumentException(enumClass.getSimpleName()
					+ "'s last enum constant is not a group quantifier");
		}

		return mappings[mappings.length - 1];
	}

	/**
	 * Validates that the {@link Enum} instances represent values in strict ascending order.
	 * 
	 * @param clazz
	 *            - the {@link ValueMapping} {@link Enum} type to check. All the mapped values of the {@link Enum} must
	 *            be in strict ascending order.
	 * @throws IllegalArgumentException
	 *             if the values of the {@link Enum} are not in strict ascending order.
	 */
	private static <T extends Enum<T> & ValueMapping> void validateEnum(final Class<T> clazz) {

		T last = null;

		for (final T mapping : clazz.getEnumConstants()) {

			if (last != null && last.getValue() > mapping.getValue()) {
				throw new IllegalArgumentException("Enum class does not have the values in strict ascending order.");
			}

			last = mapping;
		}
	}
}
