package com.mambu.number2words.internal.common.mapping;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import com.mambu.number2words.parsing.interfaces.WordValue;

/**
 * @author aatasiei
 *
 */
public class SimpleWordValue implements WordValue {

	public static class MappingWordData {

		private final Set<GrammaticalNumber> numbers;
		private final Set<WordForm> forms;
		private final String value;

		private MappingWordData(final Set<GrammaticalNumber> numbers, final Set<WordForm> forms, final String value) {
			this.numbers = numbers;
			this.forms = forms;
			this.value = value;
		}

		MappingWordData(final GrammaticalNumber number, final WordForm form, final String value) {
			this(EnumSet.of(number), EnumSet.of(form), value);
		}

		MappingWordData(final GrammaticalNumber number, final String value) {
			this(EnumSet.of(number), EnumSet.allOf(WordForm.class), value);
		}

		MappingWordData(final WordForm form, final String value) {
			this(EnumSet.allOf(GrammaticalNumber.class), EnumSet.of(form), value);
		}

		public static MappingWordData map(final GrammaticalNumber number, final WordForm form, final String value) {
			return new MappingWordData(number, form, value);
		}

		public static MappingWordData map(final GrammaticalNumber number, final String value) {
			return new MappingWordData(number, value);
		}

		public static MappingWordData map(final WordForm form, final String value) {
			return new MappingWordData(form, value);
		}
	}

	private final Map<GrammaticalNumber, Map<WordForm, String>> values;

	// single value
	SimpleWordValue(final String value) {
		values = fillEnumMap(GrammaticalNumber.class, fillEnumMap(WordForm.class, Objects.requireNonNull(value)));
	}

	SimpleWordValue(final MappingWordData... wordData) {

		if (wordData.length == 0) {
			throw new IllegalArgumentException("No word data provided");
		}

		values = new EnumMap<>(GrammaticalNumber.class);

		for (final MappingWordData data : wordData) {

			for (final GrammaticalNumber number : data.numbers) {

				final Map<WordForm, String> map = getOrNew(number);

				for (final WordForm form : data.forms) {
					map.put(form, data.value);
				}
			}
		}
	}

	public static SimpleWordValue wordOf(final String value) {
		return new SimpleWordValue(value);
	}

	public static SimpleWordValue wordOf(final MappingWordData... wordData) {
		return new SimpleWordValue(wordData);
	}

	private Map<WordForm, String> getOrNew(final GrammaticalNumber number) {

		if (!values.containsKey(number)) {

			final EnumMap<WordForm, String> formToValue = new EnumMap<>(WordForm.class);
			values.put(number, formToValue);
		}

		return values.get(number);
	}

	private static <T extends Enum<T>, E> Map<T, E> fillEnumMap(Class<T> enumClass, final E value) {

		final EnumMap<T, E> mapValues = new EnumMap<>(enumClass);

		for (final T form : enumClass.getEnumConstants()) {
			mapValues.put(form, value);
		}

		return mapValues;
	}

	@Override
	public String getWord(final GrammaticalNumber number, final WordForm form) {
		return values.get(number).get(form);
	}

}
