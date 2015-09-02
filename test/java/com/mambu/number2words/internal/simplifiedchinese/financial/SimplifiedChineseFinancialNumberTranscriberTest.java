package com.mambu.number2words.internal.simplifiedchinese.financial;

import static com.mambu.number2words.internal.simplifiedchinese.financial.SimplifiedChineseTestConstants._0;
import static com.mambu.number2words.internal.simplifiedchinese.financial.SimplifiedChineseTestConstants._1;
import static com.mambu.number2words.internal.simplifiedchinese.financial.SimplifiedChineseTestConstants._10;
import static com.mambu.number2words.internal.simplifiedchinese.financial.SimplifiedChineseTestConstants._100;
import static com.mambu.number2words.internal.simplifiedchinese.financial.SimplifiedChineseTestConstants._1000;
import static com.mambu.number2words.internal.simplifiedchinese.financial.SimplifiedChineseTestConstants._1000_0000;
import static com.mambu.number2words.internal.simplifiedchinese.financial.SimplifiedChineseTestConstants._1000_0000_0000;
import static com.mambu.number2words.internal.simplifiedchinese.financial.SimplifiedChineseTestConstants._100_0000;
import static com.mambu.number2words.internal.simplifiedchinese.financial.SimplifiedChineseTestConstants._100_0000_0000;
import static com.mambu.number2words.internal.simplifiedchinese.financial.SimplifiedChineseTestConstants._10_0000;
import static com.mambu.number2words.internal.simplifiedchinese.financial.SimplifiedChineseTestConstants._10_0000_0000;
import static com.mambu.number2words.internal.simplifiedchinese.financial.SimplifiedChineseTestConstants._11;
import static com.mambu.number2words.internal.simplifiedchinese.financial.SimplifiedChineseTestConstants._12;
import static com.mambu.number2words.internal.simplifiedchinese.financial.SimplifiedChineseTestConstants._13;
import static com.mambu.number2words.internal.simplifiedchinese.financial.SimplifiedChineseTestConstants._14;
import static com.mambu.number2words.internal.simplifiedchinese.financial.SimplifiedChineseTestConstants._15;
import static com.mambu.number2words.internal.simplifiedchinese.financial.SimplifiedChineseTestConstants._16;
import static com.mambu.number2words.internal.simplifiedchinese.financial.SimplifiedChineseTestConstants._17;
import static com.mambu.number2words.internal.simplifiedchinese.financial.SimplifiedChineseTestConstants._18;
import static com.mambu.number2words.internal.simplifiedchinese.financial.SimplifiedChineseTestConstants._19;
import static com.mambu.number2words.internal.simplifiedchinese.financial.SimplifiedChineseTestConstants._1_0000;
import static com.mambu.number2words.internal.simplifiedchinese.financial.SimplifiedChineseTestConstants._1_0000_0000;
import static com.mambu.number2words.internal.simplifiedchinese.financial.SimplifiedChineseTestConstants._1_0000_0000_0000;
import static com.mambu.number2words.internal.simplifiedchinese.financial.SimplifiedChineseTestConstants._20;
import static com.mambu.number2words.internal.simplifiedchinese.financial.SimplifiedChineseTestConstants._30;
import static com.mambu.number2words.internal.simplifiedchinese.financial.SimplifiedChineseTestConstants._40;
import static com.mambu.number2words.internal.simplifiedchinese.financial.SimplifiedChineseTestConstants._50;
import static com.mambu.number2words.internal.simplifiedchinese.financial.SimplifiedChineseTestConstants._60;
import static com.mambu.number2words.internal.simplifiedchinese.financial.SimplifiedChineseTestConstants._70;
import static com.mambu.number2words.internal.simplifiedchinese.financial.SimplifiedChineseTestConstants._80;
import static com.mambu.number2words.internal.simplifiedchinese.financial.SimplifiedChineseTestConstants._90;
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
	public void testTens() {
		String[] expected = new String[] { _0, _10, _20, _30, _40, _50, _60, _70, _80, _90 };

		for (int i = 0; i < expected.length; ++i) {
			assertEquals(expected[i], transcriber.toWords(BigDecimal.TEN.multiply(new BigDecimal(i))));
		}
	}

	@Test
	public void testTenToTwenty() {
		String[] expected = new String[] { _10, _11, _12, _13, _14, _15, _16, _17, _18, _19 };

		for (int i = 0; i < expected.length; ++i) {
			assertEquals(expected[i], transcriber.toWords(BigDecimal.TEN.add(new BigDecimal(i))));
		}
	}

	@Test
	public void testPowersOfTen() {

		String[] expected = new String[] { _1, _10, _100, _1000, _1_0000, _10_0000, _100_0000, _1000_0000,
				_1_0000_0000, _10_0000_0000, _100_0000_0000, _1000_0000_0000, _1_0000_0000_0000 };

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
