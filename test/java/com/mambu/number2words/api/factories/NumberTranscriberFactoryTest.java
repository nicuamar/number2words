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
	public void givenEnglishFactoryReturnsAnObject() {
		assertNotNull("Factory returned null for " + Locale.ENGLISH.getLanguage(),
				NumberTranscriberFactory.newTranscriber(Locale.ENGLISH));
	}

	@Test
	public void givenSimplifiedChineseFactoryReturnsAnObject() {
		assertNotNull("Factory returned null for " + Locale.SIMPLIFIED_CHINESE.getLanguage(),
				NumberTranscriberFactory.newTranscriber(Locale.SIMPLIFIED_CHINESE));
	}

	@Test(expected = IllegalArgumentException.class)
	public void givenInvalidLocaleFactoryThrows() {
		NumberTranscriberFactory.newTranscriber(Locale.forLanguageTag("ro"));
	}

	@Test(expected = IllegalArgumentException.class)
	public void givenInvalidLocaleStringFactoryThrows() {
		NumberTranscriberFactory.newTranscriber(Locale.forLanguageTag("ro").getLanguage());
	}

	@Test(expected = IllegalArgumentException.class)
	public void givenInvalidStringFactoryThrows() {
		NumberTranscriberFactory.newTranscriber("test");
	}
}
