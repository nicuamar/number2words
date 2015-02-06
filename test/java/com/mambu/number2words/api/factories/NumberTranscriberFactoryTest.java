package com.mambu.number2words.api.factories;

import static org.junit.Assert.assertNotNull;

import java.util.Locale;

import org.junit.Test;

/**
 * Factory tests.
 * 
 * @author aatasiei
 *
 */
public class NumberTranscriberFactoryTest {

	@Test
	public void giveEnglishFactoryReturnsAnObject() {
		assertNotNull("Factory returned null for " + Locale.ENGLISH.getLanguage(),
				NumberTranscriberFactory.newTranscriber(Locale.ENGLISH));
	}

	@Test
	public void giveSimplifiedChineseFactoryReturnsAnObject() {
		assertNotNull("Factory returned null for " + Locale.SIMPLIFIED_CHINESE.getLanguage(),
				NumberTranscriberFactory.newTranscriber(Locale.SIMPLIFIED_CHINESE));
	}

	@Test(expected = IllegalArgumentException.class)
	public void giveInvalidLocaleFactoryThrows() {
		NumberTranscriberFactory.newTranscriber(Locale.forLanguageTag("ro"));
	}

	@Test(expected = IllegalArgumentException.class)
	public void giveInvalidLocaleStringFactoryThrows() {
		NumberTranscriberFactory.newTranscriber(Locale.forLanguageTag("ro").getLanguage());
	}

}
