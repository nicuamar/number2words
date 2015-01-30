package com.mambu.number2words.api.factories;

import java.util.Locale;

import com.mambu.number2words.api.NumberTranscriber;
import com.mambu.number2words.internal.english.EnglishNumberTranscriber;

/**
 * Factory class used to inject {@link NumberTranscriber} instances.
 * 
 * @author aatasiei
 *
 */
public final class NumberTranscriberFactory {

	private static final String ENGLISH_CODE = new Locale("en").getLanguage();

	private NumberTranscriberFactory() {
		// factory class
	}

	/**
	 * Factory that, given a {@link Locale}, provides a {@link NumberTranscriber} implementation.
	 * 
	 * @param locale
	 *            Locale used to identify the language used when transcribing the numbers. Not <code>null</code>.
	 * @return a {@link NumberTranscriber} instance.
	 * @trows {@link IllegalArgumentException} when Locale is not supported.
	 */
	public static NumberTranscriber newTranscriber(Locale locale) {
		if (locale.getLanguage().equals(ENGLISH_CODE)) {
			return new EnglishNumberTranscriber();
		}
		throw new IllegalArgumentException();
	}

}
