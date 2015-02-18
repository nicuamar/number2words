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
	 * There is no word separator for Simplified Chinese.
	 */
	private static final String WORD_SEPARATOR = "";

	private boolean isFirstPrintableToken = true;

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
	public SimplifiedChineseTokenVisitor(StringBuilder builder, TranscriptionContext context) {
		super(context, builder, WORD_SEPARATOR);
	}

	@Override
	public Void visitNullValue(NullValueToken token) {
		if (!isFirstPrintableToken && !lastPrintWasNull) {
			lastPrintWasNull = true;
			isFirstPrintableToken = false;
			builder.append(context.asWord(0L, GrammaticalNumber.SINGULAR, WordForm.DEFAULT));
		}
		return null;
	}

	@Override
	public Void visitMappedValue(MappedValueToken token) {
		isFirstPrintableToken = false;
		lastPrintWasNull = false;
		return super.visitMappedValue(token);
	}

}
