package com.mambu.number2words.internal.english.visitors;

import com.mambu.number2words.parsing.interfaces.TranscriptionContext;
import com.mambu.number2words.parsing.interfaces.ValueToken;
import com.mambu.number2words.parsing.visitors.AbstractTranscribingVisitor;

/**
 * {@link ValueToken} visitor for English numbers. It will build or print a {@link String} representation of a tree of
 * {@link ValueToken}s.
 * <p>
 * Not thread safe.
 * 
 * @author aatasiei
 *
 */
public class EnglishTokenVisitor extends AbstractTranscribingVisitor {

	/**
	 * Word separator for English.
	 */
	private static final String WORD_SEPARATOR = " ";

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
	public EnglishTokenVisitor(StringBuilder builder, TranscriptionContext context) {
		super(context, builder, WORD_SEPARATOR);
	}

}
