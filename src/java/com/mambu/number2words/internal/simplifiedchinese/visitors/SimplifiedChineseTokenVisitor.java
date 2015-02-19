package com.mambu.number2words.internal.simplifiedchinese.visitors;

import com.mambu.number2words.parsing.interfaces.TranscriptionContext;
import com.mambu.number2words.parsing.interfaces.ValueToken;
import com.mambu.number2words.parsing.interfaces.WordValue.GrammaticalNumber;
import com.mambu.number2words.parsing.interfaces.WordValue.WordForm;
import com.mambu.number2words.parsing.tokenization.MappedValueToken;
import com.mambu.number2words.parsing.tokenization.NullValueToken;
import com.mambu.number2words.parsing.visitors.AbstractTranscribingVisitor;

/**
 * {@link ValueToken} visitor for Simplified Chinese numbers. It will build or print a {@link String} representation of
 * a tree of {@link ValueToken}s.
 * <p>
 * Not thread safe.
 * <p>
 * This implementation can be used be used regardless of which type of numerals are used.
 * 
 * @author aatasiei
 *
 */
public class SimplifiedChineseTokenVisitor extends AbstractTranscribingVisitor {

	/**
	 * {@link NullValueToken}s may need to be printed. When it gets printed this is the value used.
	 */
	private static final long NULL_TOKEN_VALUE = 0L;

	/**
	 * There is no word separator for Simplified Chinese.
	 */
	private static final String WORD_SEPARATOR = "";

	/**
	 * Flag that will be true until after the first printable token is visited.
	 */
	private boolean isFirstPrintableToken = true;

	/**
	 * Flag that tells whether the last printed token was a {@link NullValueToken}. If that is <code>true</code> then
	 * the next {@link NullValueToken} must be ignored.
	 */
	private boolean lastPrintWasNull = false;

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
	public SimplifiedChineseTokenVisitor(final StringBuilder builder, final TranscriptionContext context) {
		super(context, builder, WORD_SEPARATOR);
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Null tokens represents empty groups and in Simplified Chinese empty groups need to be marked when they appear
	 * before other non-zero values.
	 */
	@Override
	public Void visitNullValue(final NullValueToken token) {

		// don't print null tokens when their the first printable token found
		// or when the previous printed token was a null
		if (!isFirstPrintableToken && !lastPrintWasNull) {

			isFirstPrintableToken = false;
			lastPrintWasNull = true;

			builder.append(context.asWord(NULL_TOKEN_VALUE, GrammaticalNumber.SINGULAR, WordForm.DEFAULT));
		}

		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Void visitMappedValue(final MappedValueToken token) {

		isFirstPrintableToken = false;
		lastPrintWasNull = false;

		return super.visitMappedValue(token);
	}

}
