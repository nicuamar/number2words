package com.mambu.number2words.internal.simplifiedchinese.financial;

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
import com.mambu.number2words.internal.simplifiedchinese.financial.tokenization.SimplifiedChineseFinancialNumberTokenizer;

/**
 * Tests for the Simplified Chinese number transcription.
 * 
 * @author aatasiei
 *
 */
public class SimplifiedChineseFinancialNumberTranscriberTest {

	private NumberTranscriber transcriber = new SimplifiedChineseFinancialNumberTranscriber(
			new SimplifiedChineseFinancialNumberTokenizer(), new SimplifiedChineseFinancialNumberTranscriptionContext());
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
	public void testTens() {
		String[] expected = new String[] { "零", "拾", "贰拾", "叁拾", "肆拾", "伍拾", "陆拾", "柒拾", "捌拾", "玖拾" };

		for (int i = 0; i < expected.length; ++i) {
			assertEquals(expected[i], transcriber.toWords(BigDecimal.TEN.multiply(new BigDecimal(i))));
		}
	}

	@Test
	public void testTenToTwenty() {
		String[] expected = new String[] { "拾", "拾壹", "拾贰", "拾叁", "拾肆", "拾伍", "拾陆", "拾柒", "拾捌", "拾玖" };

		for (int i = 0; i < expected.length; ++i) {
			assertEquals(expected[i], transcriber.toWords(BigDecimal.TEN.add(new BigDecimal(i))));
		}
	}

	@Test
	public void testPowersOfTen() {

		String[] expected = new String[] { "壹", "拾", "壹佰", "壹仟", "壹萬", "拾萬", "壹佰萬", "壹仟萬", "壹億", "拾億", "壹佰億", "壹仟億",
				"壹兆" };

		for (int i = 0; i < expected.length; ++i) {
			assertEquals(expected[i], transcriber.toWords(BigDecimal.TEN.pow(i)));
		}
	}

	@Test
	public void testValuesFromFile() throws FileNotFoundException, IOException, ParseException {

		FileValuesTestHelper.readAndVerifyFile(transcriber, decimalFormat,
				"/simplified_chinese_financial_numbers_test.txt");
	}
}
