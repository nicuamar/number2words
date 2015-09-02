package com.mambu.number2words.internal.english;

import static org.junit.Assert.assertEquals;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;

import org.junit.Before;
import org.junit.Test;

import com.mambu.number2words.api.NumberTranscriber;
import com.mambu.number2words.internal.common.FileValuesTestHelper;
import com.mambu.number2words.internal.english.tokenization.EnglishNumberTokenizer;

/**
 * Tests for the English number transcription.
 * 
 * @author aatasiei
 *
 */
public class EnglishNumberTranscriberTest {

	private NumberTranscriber transcriber = new EnglishNumberTranscriber(new EnglishNumberTokenizer(),
			new EnglishNumberTranscriptionContext());
	private DecimalFormat decimalFormat;

	@Before
	public void setUp() {

		// Create a DecimalFormat that fits your requirements
		DecimalFormatSymbols symbols = new DecimalFormatSymbols();
		symbols.setGroupingSeparator('_');
		symbols.setDecimalSeparator('.');
		String pattern = "#,##0.0#";
		decimalFormat = new DecimalFormat(pattern, symbols);
		decimalFormat.setParseBigDecimal(true);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNegative() {
		transcriber.toWords(new BigDecimal("-2"));
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

		FileValuesTestHelper.readAndVerifyFile(transcriber, decimalFormat, "/english_numbers_test.txt");
	}
}
