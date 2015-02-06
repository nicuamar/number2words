package com.mambu.number2words.internal.common;

import java.math.BigDecimal;

import com.mambu.number2words.api.NumberTranscriber;
import com.mambu.number2words.parsing.interfaces.NumberTokenizer;
import com.mambu.number2words.parsing.interfaces.TranscriptionContext;
import com.mambu.number2words.parsing.interfaces.ValueMapping;
import com.mambu.number2words.parsing.interfaces.ValueToken;
import com.mambu.number2words.parsing.interfaces.Visitor;

public abstract class AbstractNumberTranscriber<T extends Enum<T> & ValueMapping> implements NumberTranscriber {

	/**
	 * The tokenizer for a specific language.
	 */
	private NumberTokenizer tokenizer;
	/**
	 * Evaluation context used to transcribe the tokens.
	 */
	private TranscriptionContext<T> context;

	/**
	 * Default constructor.
	 */
	protected AbstractNumberTranscriber(NumberTokenizer numberTokenizer, TranscriptionContext<T> transcriptionContext) {
		super();
		this.tokenizer = numberTokenizer;
		this.context = transcriptionContext;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toWords(BigDecimal number) {

		StringBuilder sb = new StringBuilder();
		appendWords(sb, number);

		return sb.toString();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void appendWords(StringBuilder builder, BigDecimal number) {

		ValueToken root = tokenizer.tokenize(number);

		root.accept(getTokenVisitor(builder, context));

	}

	/**
	 * Factory method for the custom token {@link Visitor} implementation.
	 * 
	 * @param builder
	 *            - the builder the visitor uses to output the results. Not <code>null</code>.
	 * @param context
	 *            - the {@link TranscriptionContext} used for token evaluation. Not <code>null</code>.
	 * @return {@link Visitor Visitor&lt;Void&gt;} implementation. Never <code>null</code>.
	 */
	protected abstract Visitor<Void> getTokenVisitor(StringBuilder builder, TranscriptionContext<T> context);

}
