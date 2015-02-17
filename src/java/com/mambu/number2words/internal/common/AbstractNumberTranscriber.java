package com.mambu.number2words.internal.common;

import java.math.BigDecimal;

import com.mambu.number2words.api.NumberTranscriber;
import com.mambu.number2words.parsing.interfaces.NumberTokenizer;
import com.mambu.number2words.parsing.interfaces.TranscriptionContext;
import com.mambu.number2words.parsing.interfaces.ValueMapping;
import com.mambu.number2words.parsing.interfaces.ValueToken;
import com.mambu.number2words.parsing.interfaces.Visitor;

/**
 * <p>
 * Abstract transcriber that provides default methods to tokenize and transform {@link BigDecimal}s into {@link String
 * words}.
 * <p>
 * It exposes the combined functionality of a {@link NumberTokenizer} and {@link TranscriptionContext} that is defined
 * over an enumeration of {@link ValueMapping} instances. Subclasses will provider an appropriate {@link Visitor
 * Visitor&lt;Void&gt;} with which the tokens can be transformed into {@link String words}.
 * 
 * @author aatasiei
 *
 * @param <T>
 *            {@link Enum} type that implements {@link ValueMapping} interface, providing a 1-1 mapping between certain
 *            numbers and string literals (i.e., for English: 1 -> "one", 11 -> "eleven", 100 -> "hundred", etc.)
 */
public abstract class AbstractNumberTranscriber implements NumberTranscriber {

	/**
	 * The tokenizer for a specific language.
	 */
	private NumberTokenizer tokenizer;
	/**
	 * Evaluation context used to transcribe the tokens.
	 */
	private TranscriptionContext context;

	/**
	 * Default constructor.
	 */
	protected AbstractNumberTranscriber(final NumberTokenizer numberTokenizer,
			final TranscriptionContext transcriptionContext) {
		this.tokenizer = numberTokenizer;
		this.context = transcriptionContext;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toWords(final BigDecimal number) {

		final StringBuilder sb = new StringBuilder();

		appendWords(sb, number);

		return sb.toString();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void appendWords(final StringBuilder builder, final BigDecimal number) {

		final ValueToken root = tokenizer.tokenize(number);

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
	protected abstract Visitor<?> getTokenVisitor(final StringBuilder builder, final TranscriptionContext context);

}
