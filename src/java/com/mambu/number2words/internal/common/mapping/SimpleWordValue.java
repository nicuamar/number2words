package com.mambu.number2words.internal.common.mapping;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import com.mambu.number2words.parsing.interfaces.WordValue;

/**
 * A <i>word</i> value can be directly identified (and directly identifies) a number.
 * 
 * @author aatasiei
 * @see WordValue
 */
public class SimpleWordValue implements WordValue {

	/**
	 * Parameter object used for faster/simpler initialization of {@link SimpleWordValue} instances. It associates
	 * {@link String} <i>words</i> with their grammatical number and variations.
	 * 
	 * @author aatasiei
	 *
	 */
	public static class WordMappingData {

		private final Set<GrammaticalNumber> numbers;
		private final Set<WordForm> forms;
		private final String value;

		/**
		 * Associates a {@link String} value with a set of grammatical numbers and forms. This should be used when the
		 * same {@link String} can be used for multiple forms/numbers.
		 * 
		 * @param numbers
		 *            - set of grammatical numbers. Not <code>null</code>.
		 * @param forms
		 *            - set of word forms. Not <code>null</code>.
		 * @param value
		 *            - the string value. Not <code>null</code>.
		 */
		private WordMappingData(final Set<GrammaticalNumber> numbers, final Set<WordForm> forms, final String value) {
			this.numbers = Objects.requireNonNull(numbers);
			this.forms = Objects.requireNonNull(forms);
			this.value = Objects.requireNonNull(value);
		}

		/**
		 * Associates a {@link String} value with a grammatical number and a form.
		 * 
		 * @param number
		 *            - the grammatical number. Not <code>null</code>.
		 * @param form
		 *            - the word form. Not <code>null</code>.
		 * @param value
		 *            - the {@link String} value. Not <code>null</code>.
		 */
		WordMappingData(final GrammaticalNumber number, final WordForm form, final String value) {
			this(EnumSet.of(number), EnumSet.of(form), value);
		}

		/**
		 * Associates a {@link String} value with a grammatical number (and all forms).
		 * 
		 * @param number
		 *            - the grammatical number. Not <code>null</code>.
		 * @param value
		 *            - the {@link String} value. Not <code>null</code>.
		 */
		WordMappingData(final GrammaticalNumber number, final String value) {
			this(EnumSet.of(number), EnumSet.allOf(WordForm.class), value);
		}

		/**
		 * Associates a {@link String} value with a form (and all grammatical numbers).
		 * 
		 * @param form
		 *            - the word form. Not <code>null</code>.
		 * @param value
		 *            - the {@link String} value. Not <code>null</code>.
		 */
		WordMappingData(final WordForm form, final String value) {
			this(EnumSet.allOf(GrammaticalNumber.class), EnumSet.of(form), value);
		}
	}

	/**
	 * Contains the {@link String} words, categorized by {@link GrammaticalNumber} and {@link WordForm}.
	 */
	private final Map<GrammaticalNumber, Map<WordForm, String>> values;

	/**
	 * Default constructor. The resulting {@link WordValue} will have the same {@link String} associated with all the
	 * {@link GrammaticalNumber} and {@link WordForm} combinations.
	 * 
	 * @param value
	 *            - the {@link String} word. Not <code>null</code>.
	 */
	SimpleWordValue(final String value) {
		values = fillEnumMap(GrammaticalNumber.class, fillEnumMap(WordForm.class, Objects.requireNonNull(value)));
	}

	/**
	 * Associates various {@link String} values with different combinations of {@link GrammaticalNumber} and
	 * {@link WordForm} values.
	 * 
	 * @param wordData
	 *            array of initialization data. Not <code>null</code> or empty.
	 */
	SimpleWordValue(final WordMappingData... wordData) {

		if (wordData.length == 0) {
			throw new IllegalArgumentException("No word data provided");
		}

		values = new EnumMap<>(GrammaticalNumber.class);

		for (final WordMappingData data : wordData) {

			// for all grammatical numbers
			for (final GrammaticalNumber number : data.numbers) {

				final Map<WordForm, String> map = getOrNew(values, number);

				// for all forms
				for (final WordForm form : data.forms) {
					map.put(form, data.value);
				}
			}
		}

		// validate that the instance contains all valid combinations.
		sanityCheck();
	}

	/**
	 * Throw an {@link IllegalArgumentException} if there are missing ({@link GrammaticalNumber}, {@link WordForm})
	 * combinations.
	 */
	private void sanityCheck() {

		if (values.keySet().size() != GrammaticalNumber.values().length) {
			throw new IllegalArgumentException(values.toString() + " does not contain all the grammatical numbers");
		}

// Left with intention here
//		for (final GrammaticalNumber number : values.keySet()) {
//
//			if (values.get(number).size() != WordForm.values().length) {
//
//				throw new IllegalArgumentException(values.get(number).toString()
//						+ " does not contain all the word forms");
//			}
//		}

		values.entrySet().stream().map(Map.Entry::getValue)
				.filter(valueMap -> valueMap.size() != WordForm.values().length)
				.findFirst()
				.ifPresent(value -> {
					throw new IllegalArgumentException(value.toString() + " does not contain all the word forms");
				});
	}

	/**
	 * Returns a {@link Map} corresponding to the passed parameter. If the Map does not exist, it creates one.
	 * 
	 * @param values
	 *            - container for the maps. Not <code>null</code>.
	 * @param number
	 *            - the key for the map. Not <code>null</code>.
	 * @return
	 */
	private static Map<WordForm, String> getOrNew(final Map<GrammaticalNumber, Map<WordForm, String>> values,
			final GrammaticalNumber number) {

		if (!values.containsKey(Objects.requireNonNull(number))) {

			final EnumMap<WordForm, String> formToValue = new EnumMap<>(WordForm.class);
			values.put(number, formToValue);
		}

		return values.get(number);
	}

	/**
	 * Returns a {@link Map} containing all the {@link Enum} values as keys and the passed value parameter as the value.
	 * 
	 * @param enumClass
	 *            - the {@link Enum} class. Not <code>null</code>.
	 * @param value
	 *            - the value that will fill the resulting {@link Map}. Not <code>null</code>.
	 * @return
	 */
	private static <T extends Enum<T>, E> Map<T, E> fillEnumMap(final Class<T> enumClass, final E value) {

		Objects.requireNonNull(value);

		final EnumMap<T, E> mapValues = new EnumMap<>(enumClass);

		for (final T form : enumClass.getEnumConstants()) {
			mapValues.put(form, value);
		}

		return mapValues;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getWord(final GrammaticalNumber number, final WordForm form) {
		return values.get(number).get(form);
	}

}
