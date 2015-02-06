package com.mambu.number2words.internal.english;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.mambu.number2words.internal.english.visitors.EnglishTokenVisitor;
import com.mambu.number2words.parsing.interfaces.Visitor;
import com.mambu.number2words.parsing.tokenization.MappedValueToken;
import com.mambu.number2words.parsing.tokenization.PrefixedValueToken;
import com.mambu.number2words.parsing.tokenization.SuffixedValueToken;

/**
 * Tests for the English language number to words mappings.
 * 
 * @author aatasiei
 *
 */
public class EnglishNumberTranscriptionContextTest {

	private EnglishNumberTranscriptionContext evaluator = new EnglishNumberTranscriptionContext();
	private StringBuilder builder;
	private Visitor<Void> visitor;

	@Before
	public void setUp() throws Exception {
		builder = new StringBuilder();
		visitor = new EnglishTokenVisitor(builder, evaluator);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNegative() {
		new MappedValueToken(-1L).accept(visitor);
	}

	@Test
	public void testZeroToNine() {
		String[] expectedValues = new String[] { "zero", "one", "two", "three", "four", "five", "six", "seven",
				"eight", "nine" };

		for (int i = 0; i < 10; i++) {

			builder.setLength(0);
			new MappedValueToken(i).accept(visitor);

			assertEquals(expectedValues[i], builder.toString());
		}

	}
	@Test
	public void testTenToNineTeen() {
		String[] expectedValues = new String[] { "ten", "eleven", "twelve", "thirteen", "fourteen", "fifteen",
				"sixteen", "seventeen", "eighteen", "nineteen" };

		for (int i = 0; i < 10; i++) {

			builder.setLength(0);
			new MappedValueToken(i + 10).accept(visitor);

			assertEquals(expectedValues[i], builder.toString());

		}

	}
	@Test
	public void testTens() {
		String[] expectedValues = new String[] { "", "ten", "twenty", "thirty", "forty", "fifty", "sixty", "seventy",
				"eighty", "ninety" };

		for (int i = 1; i < 10; i++) {

			builder.setLength(0);
			new MappedValueToken(i * 10).accept(visitor);

			assertEquals(expectedValues[i], builder.toString());

		}

	}
	@Test
	public void testTwenties() {
		String[] expectedValues = new String[] { "", "one", "two", "three", "four", "five", "six", "seven", "eight",
				"nine" };

		for (int i = 1; i < 10; i++) {

			builder.setLength(0);
			new PrefixedValueToken(new MappedValueToken(20), new MappedValueToken(i)).accept(visitor);

			assertEquals("twenty " + expectedValues[i], builder.toString());
		}
	}

	@Test
	public void testHundred() {
		new MappedValueToken(100).accept(visitor);
		assertEquals("hundred", builder.toString());
	}
	@Test
	public void testOneHundred() {
		new SuffixedValueToken(new MappedValueToken(1), new MappedValueToken(100)).accept(visitor);

		assertEquals("one hundred", builder.toString());
	}
	@Test
	public void testThousand() {
		new MappedValueToken(1_000).accept(visitor);
		assertEquals("thousand", builder.toString());
	}
	@Test
	public void testMillion() {
		new MappedValueToken(1_000_000).accept(visitor);
		assertEquals("million", builder.toString());
	}
	@Test
	public void testBillion() {
		new MappedValueToken(1_000_000_000).accept(visitor);
		assertEquals("billion", builder.toString());
	}
}
