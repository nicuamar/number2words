package com.mambu.number2words.internal.simplifiedchinese.financial;

import com.mambu.number2words.internal.common.AbstractNumberTranscriber;
import com.mambu.number2words.internal.simplifiedchinese.visitors.SimplifiedChineseTokenVisitor;
import com.mambu.number2words.parsing.interfaces.NumberTokenizer;
import com.mambu.number2words.parsing.interfaces.TranscriptionContext;
import com.mambu.number2words.parsing.interfaces.Visitor;

/**
 * Number transcriber for the Simplified Chinese language using Financial numerals.
 * 
 * @author aatasiei
 *
 */
public class SimplifiedChineseFinancialNumberTranscriber extends AbstractNumberTranscriber {

	/**
	 * Constructor that is used to initialize this transcriber with a tokenizer and context.
	 * 
	 * @param tokenizer
	 *            - tokenizer for Simplified Chinese. Not <code>null</code>.
	 * @param context
	 *            - evaluation context when transcribing tokens. Not <code>null</code>.
	 */
	public SimplifiedChineseFinancialNumberTranscriber(final NumberTokenizer tokenizer,
			final TranscriptionContext context) {
		super(tokenizer, context);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Visitor<Void> getTokenVisitor(final StringBuilder builder, final TranscriptionContext context) {
		return new SimplifiedChineseTokenVisitor(builder, context);
	}

}
