package com.mambu.number2words.internal.english;

import com.mambu.number2words.internal.common.AbstractNumberTranscriber;
import com.mambu.number2words.internal.english.visitors.EnglishTokenVisitor;
import com.mambu.number2words.parsing.interfaces.NumberTokenizer;
import com.mambu.number2words.parsing.interfaces.TranscriptionContext;
import com.mambu.number2words.parsing.interfaces.Visitor;

/**
 * Number transcriber for the English language.
 * 
 * @author aatasiei
 *
 */
public class EnglishNumberTranscriber extends AbstractNumberTranscriber<EnglishNumberMapping> {

	/**
	 * Constructor that is used to initialize this transcriber with a custom tokenizer or context. This might be useful
	 * in testing.
	 * 
	 * @param tokenizer
	 *            - tokenizer for English. Not <code>null</code>.
	 * @param context
	 *            - evaluation context when transcribing tokens. Not <code>null</code>.
	 */
	public EnglishNumberTranscriber(final NumberTokenizer tokenizer,
			final TranscriptionContext<EnglishNumberMapping> context) {
		super(tokenizer, context);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Visitor<Void> getTokenVisitor(final StringBuilder builder,
			final TranscriptionContext<EnglishNumberMapping> context) {
		return new EnglishTokenVisitor(builder, context);
	}

}
