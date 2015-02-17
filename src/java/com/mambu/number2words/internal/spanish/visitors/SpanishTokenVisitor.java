package com.mambu.number2words.internal.spanish.visitors;

import com.mambu.number2words.parsing.interfaces.TranscriptionContext;
import com.mambu.number2words.parsing.interfaces.ValueToken;
import com.mambu.number2words.parsing.interfaces.WordValue.Form;
import com.mambu.number2words.parsing.interfaces.WordValue.GrammaticalNumber;
import com.mambu.number2words.parsing.tokenization.DecimalValueToken;
import com.mambu.number2words.parsing.tokenization.GroupListToken;
import com.mambu.number2words.parsing.tokenization.MappedValueToken;
import com.mambu.number2words.parsing.tokenization.PrefixedValueToken;
import com.mambu.number2words.parsing.tokenization.SuffixedValueToken;
import com.mambu.number2words.parsing.visitors.AbstractTranscribingVisitor;
import com.mambu.number2words.parsing.visitors.AccumulateMaxVisitor;

/**
 * {@link ValueToken} visitor for English numbers. It will build or print a {@link String} representation of a tree of
 * {@link ValueToken}s.
 * <p>
 * Not thread safe.
 * 
 * @author aatasiei
 *
 */
public class SpanishTokenVisitor extends AbstractTranscribingVisitor {

	/**
	 * Word separator for English.
	 */
	private static final String WORD_SEPARATOR = " ";

	private AccumulateMaxVisitor maxAccumulator;

	private GrammaticalNumber wordNumber = GrammaticalNumber.SINGULAR;

	private Form wordForm = Form.DEFAULT;

	/**
	 * Default constructor.
	 * 
	 * @param builder
	 *            - {@link StringBuilder} the {@link ValueToken} word representation will be appended.
	 * 
	 * @param context
	 *            - the context that holds the number to word mapping information.
	 * 
	 */
	public SpanishTokenVisitor(StringBuilder builder, TranscriptionContext context) {
		super(context, builder, WORD_SEPARATOR);
		this.maxAccumulator = new AccumulateMaxVisitor(context);
	}

	@Override
	public Void visitDecimalValue(DecimalValueToken token) {
		throw new IllegalStateException();
	}

	@Override
	public Void visitSuffixedValue(SuffixedValueToken token) {

		Long maxSuffixValue = maximum(token.getSuffixToken());

		Long maxValue = maximum(token.getValueToken());

		// write value
		visitToken(token.getValueToken(), wordNumber == GrammaticalNumber.PLURAL,
				shouldShortenBasedOnSuffix(maxValue, maxSuffixValue));

		// write separator
		builder.append(wordSeparator);

		// write suffix
		visitToken(token.getSuffixToken(), maxValue > 1L, wordForm == Form.SHORTENED);

		return null;
	}

	private Long maximum(ValueToken token) {
		return token.accept(maxAccumulator);
	}

	private boolean shouldShortenBasedOnSuffix(Long maxValue, Long maxSuffix) {
		if (maxSuffix >= 1000L) {
			return maxValue == 100L || maxValue == 1L;
		}

		return false;
	}

	private void visitToken(final ValueToken token, boolean isPlural, boolean isShortened) {

		// using the stack to store the previous values

		GrammaticalNumber oldNumber = this.wordNumber;
		Form oldForm = this.wordForm;

		// setting the visitation state for mapped values depending on the parameters

		this.wordNumber = isPlural ? GrammaticalNumber.PLURAL : GrammaticalNumber.SINGULAR;
		this.wordForm = isShortened ? Form.SHORTENED : Form.DEFAULT;

		token.accept(this);

		// restore the old state
		this.wordNumber = oldNumber;
		this.wordForm = oldForm;

	}

	/**
	 * Checks whether the next mapped value token should be lengthened (to the default length).
	 * 
	 * This is used to
	 * 
	 * @param maxValue
	 * @param maxPrefix
	 * @return
	 */
	private boolean shouldLengthenBasedOnPrefix(Long maxValue, Long maxPrefix) {
		return maxPrefix == 100L && maxValue > 0L;
	}

	@Override
	public Void visitPrefixedValue(final PrefixedValueToken token) {

		final Long maxPrefixValue = maximum(token.getPrefixToken());
		final Long maxValue = maximum(token.getValueToken());

		// write prefix
		// handling using "ciento" vs just "cien"
		visitToken(token.getPrefixToken(), wordNumber == GrammaticalNumber.PLURAL,
				!shouldLengthenBasedOnPrefix(maxValue, maxPrefixValue));

		// write separator
		builder.append(wordSeparator);

		// write value
		// handling "cento uno"
		visitToken(token.getValueToken(), wordNumber == GrammaticalNumber.PLURAL, false);
		return null;
	}

	@Override
	public Void visitMappedValue(final MappedValueToken token) {

		final String word = context.asWord(token.getMappedValue(), wordNumber, wordForm);

		builder.append(word);

		return null;
	};

	/**
	 * Visits the {@link GroupListToken} tokens.
	 * <p>
	 * Will append the word representations of each of the children.
	 * <p>
	 * Example: 10100: "ten thousand", "one hundred".
	 */
	@Override
	public Void visitGroupList(final GroupListToken token) {

		Long previousMax = 0L;

		// for all tokens in the list
		for (ValueToken gr : token.getList()) {

			final int oldLength = builder.length();

			final Long currentMax = maximum(gr);

			// accept the visitor
			// hundreds are top level value tokens
			visitToken(gr, previousMax > 1L, (gr instanceof MappedValueToken && currentMax == 100L));

			// if the builder was modified, append a separator
			if (oldLength != builder.length()) {
				builder.append(wordSeparator);
			}

			previousMax = currentMax;
		}

		if (builder.length() > 0 && wordSeparator.length() > 0) {
			// remove last separator
			builder.setLength(builder.length() - wordSeparator.length());
		}

		return null;
	}
}
