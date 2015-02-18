package com.mambu.number2words.internal.spanish;

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
import com.mambu.number2words.internal.spanish.tokenization.SpanishNumberTokenizer;

/**
 * Tests for the Spanish number transcription.
 * 
 * @author aatasiei
 *
 */
public class SpanishNumberTranscriberTest {

	private NumberTranscriber transcriber = new SpanishNumberTranscriber(new SpanishNumberTokenizer(),
			new SpanishNumberTranscriptionContext());
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

	@Test(expected = IllegalArgumentException.class)
	public void testNegative() {
		transcriber.toWords(new BigDecimal("-2"));
	}

	@Test
	public void testZero() {
		assertEquals("cero", transcriber.toWords(BigDecimal.ZERO));
	}

	@Test
	public void testTens() {
		String[] expected = new String[] { "cero", "diez", "veinte", "treinta", "cuarenta", "cincuenta", "sesenta",
				"setenta", "ochenta", "noventa" };

		for (int i = 0; i < expected.length; ++i) {
			assertEquals(expected[i], transcriber.toWords(BigDecimal.TEN.multiply(new BigDecimal(i))));
		}
	}

	@Test
	public void testTenToTwenty() {
		String[] expected = new String[] { "diez", "once", "doce", "trece", "catorce", "quince", "dieciseis",
				"diecisiete", "dieciocho", "diecinueve" };

		for (int i = 0; i < expected.length; ++i) {
			assertEquals(expected[i], transcriber.toWords(BigDecimal.TEN.add(new BigDecimal(i))));
		}
	}

	@Test
	public void testTwentyToThirty() {
		String[] expected = new String[] { "veinte", "veintiuno", "veintidos", "veintitres", "veinticuatro",
				"veinticinco", "veintiseis", "veintisiete", "veintiocho", "veintinueve" };

		for (int i = 0; i < expected.length; ++i) {
			assertEquals(
					expected[i],
					transcriber.toWords(BigDecimal.TEN.multiply(BigDecimal.valueOf(2L)).add(
							BigDecimal.valueOf((long) i))));
		}
	}

	@Test
	public void testThirtiesAndUp() {
		String[] expectedPrefix = new String[] { "treinta", "cuarenta", "cincuenta", "sesenta", "setenta", "ochenta",
				"noventa" };

		String[] expectedValue = new String[] { "uno", "dos", "tres", "cuatro", "cinco", "seis", "siete", "ocho",
				"nueve" };

		for (int prefixIdx = 0; prefixIdx < expectedPrefix.length; ++prefixIdx) {

			final BigDecimal initialValue = BigDecimal.valueOf(30L).add(
					BigDecimal.TEN.multiply(BigDecimal.valueOf((long) prefixIdx)));

			for (int valueIdx = 0; valueIdx < expectedValue.length; ++valueIdx) {

				final BigDecimal toTranscribe = initialValue.add(BigDecimal.valueOf((long) valueIdx + 1));

				assertEquals(expectedPrefix[prefixIdx] + " y " + expectedValue[valueIdx],
						transcriber.toWords(toTranscribe));
			}
		}
	}

	@Test
	public void testPowersOfTen() {

		String[] expected = new String[] { "uno", "diez", "cien", "mil", "diez mil", "cien mil", "un millon",
				"diez millones", "cien millones", "mil millones", "diez mil millones", "cien mil millones",
				"un billon", "diez billones" };

		for (int i = 0; i < expected.length; ++i) {
			assertEquals(expected[i], transcriber.toWords(BigDecimal.TEN.pow(i)));
		}
	}

	@Test
	public void testValuesFromFile() throws FileNotFoundException, IOException, ParseException {

		FileValuesTestHelper.readAndVerifyFile(transcriber, decimalFormat, "/spanish_numbers_test.txt");
	}
}
