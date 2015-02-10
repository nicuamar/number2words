package com.mambu.number2words.internal.common;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;

import com.mambu.number2words.api.NumberTranscriber;

/**
 * Utilities class that can be used to verify a {@link NumberTranscriber} using tab separated values from a file.
 * 
 * @author aatasiei
 *
 */
public class FileValuesTestHelper {

	private FileValuesTestHelper() {
		// utilities class
	}

	private static void verifyValueAndWordsMatch(final NumberTranscriber transcriber,
			final DecimalFormat decimalFormat, final String line) throws ParseException {

		// values are tab separated
		assertThat("No tab found in: " + line, line.indexOf('\t'), not(equalTo(-1)));

		final String[] values = line.split("\t");

		assertThat("More than one tab was found in: " + line, values.length, equalTo(2));

		final BigDecimal number = (BigDecimal) decimalFormat.parse(values[0]);

		String words = null;

		try {
			words = transcriber.toWords(number);
		} catch (NullPointerException npe) {
			fail(values[0] + " resulted in a npe");
		}

		assertThat(values[0] + " failed transcription", words, equalTo(values[1]));
	}

	public static void readAndVerifyFile(final NumberTranscriber transcriber, final DecimalFormat decimalFormat,
			final String fileName) throws IOException, ParseException {

		try (final InputStream file = FileValuesTestHelper.class.getResourceAsStream(fileName);
				final BufferedReader reader = new BufferedReader(new InputStreamReader(file))) {

			int lineNo = 0;
			String line;

			while ((line = reader.readLine()) != null) {

				if (line.trim().isEmpty() || line.startsWith("#")) {
					// ignore empty or commented lines
					continue;
				}

				verifyValueAndWordsMatch(transcriber, decimalFormat, line);

				++lineNo;
			}

			assertThat("File was empty", lineNo, not(equalTo(0)));
		}
	}

}
