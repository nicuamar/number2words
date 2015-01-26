package com.mambu.number2words.api.factories;

import static org.junit.Assert.assertNotNull;

import java.util.Locale;

import org.junit.Test;

public class NumberTranscriberFactoryTest {

	@Test
	public void giveEnglishFactoryReturnsAnObject() {
		assertNotNull("Factory returned null", NumberTranscriberFactory.newTranscriber(Locale.ENGLISH));
	}

	@Test(expected = IllegalArgumentException.class)
	public void giveInvalidLocaleFactoryThrows() {
		NumberTranscriberFactory.newTranscriber(Locale.forLanguageTag("ro"));
	}

}
