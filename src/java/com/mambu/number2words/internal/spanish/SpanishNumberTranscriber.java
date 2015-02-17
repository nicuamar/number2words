package com.mambu.number2words.internal.spanish;

import com.mambu.number2words.internal.common.AbstractNumberTranscriber;
import com.mambu.number2words.internal.spanish.visitors.SpanishTokenVisitor;
import com.mambu.number2words.parsing.interfaces.NumberTokenizer;
import com.mambu.number2words.parsing.interfaces.TranscriptionContext;
import com.mambu.number2words.parsing.interfaces.Visitor;

/**
 * Number transcriber for the Spanish language.
 * 
 * @author aatasiei
 *
 */
public class SpanishNumberTranscriber extends AbstractNumberTranscriber {

	/**
	 * Constructor that is used to initialize this transcriber with a tokenizer and context.
	 * 
	 * @param tokenizer
	 *            - tokenizer for Spanish. Not <code>null</code>.
	 * @param context
	 *            - evaluation context when transcribing tokens. Not <code>null</code>.
	 */
	public SpanishNumberTranscriber(final NumberTokenizer tokenizer, final TranscriptionContext context) {
		super(tokenizer, context);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Visitor<Void> getTokenVisitor(final StringBuilder builder, final TranscriptionContext context) {
		return new SpanishTokenVisitor(builder, context);
	}

}
