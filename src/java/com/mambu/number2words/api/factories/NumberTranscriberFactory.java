package com.mambu.number2words.api.factories;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.mambu.number2words.api.NumberTranscriber;
import com.mambu.number2words.internal.english.EnglishNumberTranscriber;
import com.mambu.number2words.internal.english.EnglishNumberTranscriptionContext;
import com.mambu.number2words.internal.english.tokenization.EnglishNumberTokenizer;
import com.mambu.number2words.internal.simplifiedchinese.financial.SimplifiedChineseFinancialNumberTranscriber;
import com.mambu.number2words.internal.simplifiedchinese.financial.SimplifiedChineseFinancialNumberTranscriptionContext;
import com.mambu.number2words.internal.simplifiedchinese.financial.tokenization.SimplifiedChineseFinancialNumberTokenizer;
import com.mambu.number2words.internal.spanish.SpanishNumberTranscriber;
import com.mambu.number2words.internal.spanish.SpanishNumberTranscriptionContext;
import com.mambu.number2words.internal.spanish.tokenization.SpanishNumberTokenizer;
import com.mambu.number2words.parsing.interfaces.NumberTokenizer;
import com.mambu.number2words.parsing.interfaces.TranscriptionContext;

/**
 * Factory class used to inject {@link NumberTranscriber} instances.
 * 
 * @author aatasiei
 *
 */
public final class NumberTranscriberFactory {

	// FUTURE: (aatasiei) look at providing a way to register transcribers and make the configuration in some other
	// static file.

	/*
	 * The codes should be the same as result of Locale#getLangauge() call, which might not match the ISO standard.
	 */
	private static final String ENGLISH_CODE = "en";
	private static final String SIMPLIFIED_CHINESE_CODE = "zh";
	private static final String SPANISH = "es";

	/**
	 * Mapping language codes to {@link NumberTokenizer}. Implementations should be thread safe.
	 */
	private static final Map<String, NumberTokenizer> TOKENIZERS = new HashMap<>();

	/**
	 * Mapping language codes to {@link TranscriptionContext}. Implementations should be thread safe.
	 */
	private static final Map<String, TranscriptionContext> CONTEXTS = new HashMap<>();

	static {
		// these should be state-less, thread safe implementations.

		// ENGLISH
		TOKENIZERS.put(ENGLISH_CODE, new EnglishNumberTokenizer());
		CONTEXTS.put(ENGLISH_CODE, new EnglishNumberTranscriptionContext());

		// SIMPLIFIED CHINESE - Financial numbers
		TOKENIZERS.put(SIMPLIFIED_CHINESE_CODE, new SimplifiedChineseFinancialNumberTokenizer());
		CONTEXTS.put(SIMPLIFIED_CHINESE_CODE, new SimplifiedChineseFinancialNumberTranscriptionContext());

		// SPANISH
		TOKENIZERS.put(SPANISH, new SpanishNumberTokenizer());
		CONTEXTS.put(SPANISH, new SpanishNumberTranscriptionContext());
	}

	/**
	 * Private constructor. No instances allowed.
	 */
	private NumberTranscriberFactory() {
		// factory class
	}

	/**
	 * Factory that, given a {@link Locale}, provides a {@link NumberTranscriber} implementation.
	 * 
	 * @param locale
	 *            Locale used to identify the language used when transcribing the numbers. Not <code>null</code>.
	 * @return a {@link NumberTranscriber} instance.
	 * @throws {@link IllegalArgumentException} when Locale is not supported.
	 */
	public static NumberTranscriber newTranscriber(Locale locale) {

		final String key = locale.getLanguage();

		if (TOKENIZERS.containsKey(key)) {

			return newTranscriber(key);
		}

		throw new IllegalArgumentException();
	}

	/**
	 * Factory method that given a {@link String} that matches a {@link Locale#getLanguage()} previously registered,
	 * will return a {@link NumberTranscriber} implementation.
	 * 
	 * @param key
	 *            - the {@link String} language code key. Not <code>null</code>.
	 * @return a {@link NumberTranscriber} instance.
	 * @throws {@link IllegalArgumentException} when Locale is not supported.
	 */
	protected static NumberTranscriber newTranscriber(final String key) {

		switch (key) {

		case ENGLISH_CODE:

			return new EnglishNumberTranscriber(TOKENIZERS.get(key), CONTEXTS.get(key));

		case SIMPLIFIED_CHINESE_CODE:
			// for Simplified Chinese we use the financial numerals
			return new SimplifiedChineseFinancialNumberTranscriber(TOKENIZERS.get(key), CONTEXTS.get(key));

		case SPANISH:

			return new SpanishNumberTranscriber(TOKENIZERS.get(key), CONTEXTS.get(key));
		}

		throw new IllegalArgumentException();
	}

}
