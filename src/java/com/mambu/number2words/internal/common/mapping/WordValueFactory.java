package com.mambu.number2words.internal.common.mapping;

import com.mambu.number2words.internal.common.mapping.SimpleWordValue.WordMappingData;
import com.mambu.number2words.parsing.interfaces.WordValue;
import com.mambu.number2words.parsing.interfaces.WordValue.GrammaticalNumber;
import com.mambu.number2words.parsing.interfaces.WordValue.WordForm;

/**
 * Contains factory methods that can be used to create {@link WordValue} instances.
 * 
 * @author aatasiei
 *
 */
public final class WordValueFactory {

	private WordValueFactory() {
		// factory class
	}

	/**
	 * Creates a new mapping data object associating the {@link String} value with the passed {@link GrammaticalNumber}
	 * and {@link WordForm}.
	 * 
	 * @param number
	 *            - the <i>grammatical</i> number. Not <code>null</code>.
	 * @param form
	 *            - the <i>word form</i>. Not <code>null</code>.
	 * @param value
	 *            - the {@link String} value. Not <code>null</code>.
	 * @return a mapping data object that, in combination with other mapping data objects, can be used to create a new
	 *         {@link WordValue}. Never <code>null</code>.
	 */
	public static WordMappingData map(final GrammaticalNumber number, final WordForm form, final String value) {
		return new WordMappingData(number, form, value);
	}

	/**
	 * Creates a new mapping data object associating the {@link String} value with the passed {@link GrammaticalNumber}
	 * and all {@link WordForm}s.
	 * 
	 * @param number
	 *            - the <i>grammatical</i> number. Not <code>null</code>.
	 * @param value
	 *            - the {@link String} value. Not <code>null</code>.
	 * @return a mapping data object that, in combination with other mapping data objects, can be used to create a new
	 *         {@link WordValue}. Never <code>null</code>.
	 */
	public static WordMappingData map(final GrammaticalNumber number, final String value) {
		return new WordMappingData(number, value);
	}

	/**
	 * Creates a new mapping data object associating the {@link String} value with the passed {@link WordForm} and all
	 * {@link GrammaticalNumber}s.
	 * 
	 * @param form
	 *            - the <i>word form</i>. Not <code>null</code>.
	 * @param value
	 *            - the {@link String} value. Not <code>null</code>.
	 * @return a mapping data object that, in combination with other mapping data objects, can be used to create a new
	 *         {@link WordValue}. Never <code>null</code>.
	 */
	public static WordMappingData map(final WordForm form, final String value) {
		return new WordMappingData(form, value);
	}

	/**
	 * Creates a new {@link WordValue} that will return the {@link String} value for all {@link GrammaticalNumber} and
	 * {@link WordForm} combinations.
	 * 
	 * @param value
	 *            - the {@link String} word. Not <code>null</code>.
	 * @return a {@link WordValue}. Never <code>null</code>.
	 */
	public static WordValue wordFrom(final String value) {
		return new SimpleWordValue(value);
	}

	/**
	 * Creates a new {@link WordValue} combining the information from all the word mapping data provided.
	 * <p>
	 * The objects in the array are allowed to override the values set by previous objects, but they must cover all the
	 * ( {@link GrammaticalNumber}, {@link WordForm}) combinations. Otherwise the {@link WordValue} creation will fail.
	 * 
	 * @param wordData
	 *            - array of initialization data. Not <code>null</code> or empty.
	 * @return a new {@link WordValue} instance. Never <code>null</code>.
	 * 
	 * @throws IllegalArgumentException
	 *             when the data provided was not sufficient to initialize the {@link WordValue}.
	 */
	public static WordValue wordFrom(final WordMappingData... wordData) {
		return new SimpleWordValue(wordData);
	}

}
