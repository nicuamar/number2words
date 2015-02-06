package com.mambu.number2words.internal.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

import com.mambu.number2words.parsing.interfaces.ValueMapping;

public class QuantifyingMappingsHelper {

	private QuantifyingMappingsHelper() {
		// utility class
	}

	public static <T extends Enum<T> & ValueMapping> EnumSet<T> groupQuantifiers(final Class<T> clazz) {

		validateEnum(clazz);

		final EnumSet<T> result = EnumSet.noneOf(clazz);

		for (final T mapping : clazz.getEnumConstants()) {

			if (mapping.isGroupQuantifier()) {
				result.add(mapping);
			}
		}

		return result;
	}

	public static <T extends Enum<T> & ValueMapping> T largestConsecutiveMapping(final Class<T> clazz) {
		validateEnum(clazz);

		T last = null;

		for (final T mapping : clazz.getEnumConstants()) {

			if (last != null && last.getValue() != mapping.getValue() - 1L) {
				break;
			}

			last = mapping;
		}

		return last;
	}

	public static <T extends Enum<T> & ValueMapping> List<T> subGroupQuantifiers(final Class<T> clazz) {

		validateEnum(clazz);

		final List<T> result = new ArrayList<>();

		for (final T mapping : clazz.getEnumConstants()) {

			if (mapping.isSubGroupQuantifier()) {
				result.add(mapping);
			}
		}

		Collections.reverse(result);

		return result;
	}

	public static <T extends Enum<T> & ValueMapping> T minGroupQuantifier(final Class<T> clazz) {

		validateEnum(clazz);

		for (final T mapping : clazz.getEnumConstants()) {

			if (mapping.isGroupQuantifier()) {
				return mapping;
			}
		}

		throw new IllegalArgumentException(clazz.getSimpleName() + " does not contain any group quantifiers");
	}

	public static <T extends Enum<T> & ValueMapping> T maxGroupQuantifier(final Class<T> clazz) {

		validateEnum(clazz);

		final T[] mappings = clazz.getEnumConstants();

		if (!mappings[mappings.length - 1].isGroupQuantifier()) {
			throw new IllegalArgumentException(clazz.getSimpleName()
					+ "'s last enum constant is not a group quantifier");
		}

		return mappings[mappings.length - 1];
	}

	private static <T extends Enum<T> & ValueMapping> void validateEnum(final Class<T> clazz) {
		T last = null;

		for (final T mapping : clazz.getEnumConstants()) {

			if (last != null && last.getValue() >= mapping.getValue()) {
				throw new IllegalArgumentException("Enum class does not have the values in ascending order.");
			}

			last = mapping;
		}
	}

}
