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
		private final Set<Form> forms;
		private final String value;

		private MappingWordData(final Set<GrammaticalNumber> numbers, final Set<Form> forms, final String value) {
			this.numbers = numbers;
			this.forms = forms;
			this.value = value;
		}

		MappingWordData(final GrammaticalNumber number, final Form form, final String value) {
			this(EnumSet.of(number), EnumSet.of(form), value);
		}

		MappingWordData(final GrammaticalNumber number, final String value) {
			this(EnumSet.of(number), EnumSet.allOf(Form.class), value);
		}

		MappingWordData(final Form form, final String value) {
			this(EnumSet.allOf(GrammaticalNumber.class), EnumSet.of(form), value);
		}

		public static MappingWordData map(final GrammaticalNumber number, final Form form, final String value) {
			return new MappingWordData(number, form, value);
		}

		public static MappingWordData map(final GrammaticalNumber number, final String value) {
			return new MappingWordData(number, value);
		}

		public static MappingWordData map(final Form form, final String value) {
			return new MappingWordData(form, value);
		}
	}

	private final Map<GrammaticalNumber, Map<Form, String>> values;

	// single value
	SimpleWordValue(final String value) {
		values = fillEnumMap(GrammaticalNumber.class, fillEnumMap(Form.class, Objects.requireNonNull(value)));
	}

	SimpleWordValue(final MappingWordData... wordData) {

		if (wordData.length == 0) {
			throw new IllegalArgumentException("No word data provided");
		}

		values = new EnumMap<>(GrammaticalNumber.class);

		for (final MappingWordData data : wordData) {

			for (final GrammaticalNumber number : data.numbers) {

				final Map<Form, String> map = getOrNew(number);

				for (final Form form : data.forms) {
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

	private Map<Form, String> getOrNew(final GrammaticalNumber number) {

		if (!values.containsKey(number)) {

			final EnumMap<Form, String> formToValue = new EnumMap<>(Form.class);
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

	public String getWord(final GrammaticalNumber number, final Form form) {
		return values.get(number).get(form);
	}

	public String getWord(final GrammaticalNumber number) {
		return values.get(number).get(Form.DEFAULT);
	}

	public String getWord(final Form form) {
		return values.get(GrammaticalNumber.SINGULAR).get(form);
	}

	public String getWord() {
		return values.get(GrammaticalNumber.SINGULAR).get(Form.DEFAULT);
	}
}
