package com.mambu.number2words.internal.english;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;

import org.junit.Before;
import org.junit.Test;

import com.mambu.number2words.api.NumberTranscriber;
import com.mambu.number2words.internal.english.EnglishNumberTranscriber;

public class EnglishNumberTranscriberTest {

	private NumberTranscriber transcriber = new EnglishNumberTranscriber();
	private DecimalFormat decimalFormat;

	@Before
	public void setUp() throws Exception {

		// Create a DecimalFormat that fits your requirements
		DecimalFormatSymbols symbols = new DecimalFormatSymbols();
		symbols.setGroupingSeparator('_');
		symbols.setDecimalSeparator('.');
		String pattern = "#,##0.0#";
		decimalFormat = new DecimalFormat(pattern, symbols);
		decimalFormat.setParseBigDecimal(true);
	}

	@Test
	public void testZero() {
		assertEquals("zero", transcriber.toWords(BigDecimal.ZERO));
	}

	@Test
	public void testTens() {
		String[] expected = new String[] { "zero", "ten", "twenty", "thirty", "forty", "fifty", "sixty", "seventy",
				"eighty", "ninety" };

		for (int i = 0; i < expected.length; ++i) {
			assertEquals(expected[i], transcriber.toWords(BigDecimal.TEN.multiply(new BigDecimal(i))));
		}
	}

	@Test
	public void testTenToTwenty() {
		String[] expected = new String[] { "ten", "eleven", "twelve", "thirteen", "fourteen", "fifteen", "sixteen",
				"seventeen", "eighteen", "nineteen" };

		for (int i = 0; i < expected.length; ++i) {
			assertEquals(expected[i], transcriber.toWords(BigDecimal.TEN.add(new BigDecimal(i))));
		}
	}

	@Test
	public void testPowersOfTen() {

		String[] expected = new String[] { "one", "ten", "one hundred", "one thousand", "ten thousand",
				"one hundred thousand", "one million", "ten million", "one hundred million", "one billion",
				"ten billion", "one hundred billion" };

		for (int i = 0; i < expected.length; ++i) {
			assertEquals(expected[i], transcriber.toWords(BigDecimal.TEN.pow(i)));
		}
	}

	@Test
	public void testValuesFromFile() throws FileNotFoundException, IOException, ParseException {

		try (InputStream file = this.getClass().getResourceAsStream("/english_numbers_test.properties");
				BufferedReader reader = new BufferedReader(new InputStreamReader(file))) {

			int lineNo = 0;
			String line;
			while ((line = reader.readLine()) != null) {
				if (line.trim().isEmpty() || line.startsWith("#")) {
					// ignore empty or commented lines
					continue;
				}
				verifyValueAndWordsMatch(line);
				++lineNo;
			}

			assertFalse("File was empty", lineNo == 0);
		}
	}

	private void verifyValueAndWordsMatch(String line) throws ParseException {
		assertFalse("No tab found in: " + line, line.indexOf('\t') == -1);

		String[] values = line.split("\t");

		assertEquals("More than one tab was found in: " + line, 2, values.length);

		BigDecimal number = (BigDecimal) decimalFormat.parse(values[0]);

		String words = null;
		try {
			words = transcriber.toWords(number);
		} catch (NullPointerException npe) {
			fail(values[0] + " resulted in a npe");
		}
		assertEquals(values[0] + " failed transcription", values[1], words);
	}
}
