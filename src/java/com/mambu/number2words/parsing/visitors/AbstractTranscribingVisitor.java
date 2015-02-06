package com.mambu.number2words.parsing.visitors;

import java.util.Objects;

import com.mambu.number2words.parsing.interfaces.TranscriptionContext;
import com.mambu.number2words.parsing.interfaces.ValueToken;
import com.mambu.number2words.parsing.tokenization.DecimalValueToken;
import com.mambu.number2words.parsing.tokenization.GroupListToken;
import com.mambu.number2words.parsing.tokenization.MappedValueToken;
import com.mambu.number2words.parsing.tokenization.PrefixedValueToken;
import com.mambu.number2words.parsing.tokenization.SuffixedValueToken;

/**
 * Adaptor for visitors that use {@link StringBuilder} instances to transcribe {@link ValueToken}s.
 * 
 * @author aatasiei
 *
 */
public abstract class AbstractTranscribingVisitor extends VoidVisitorAdaptor {

	/**
	 * String builder to which the {@link ValueToken} word representation will be appended.
	 */
	private final StringBuilder builder;

	/**
	 * String separator to be appended between words.
	 */
	private final String wordSeparator;

	/**
	 * Default constructor
	 * 
	 * @param context
	 *            - the context that holds the number to word mapping information.
	 * 
	 * @param builder
	 *            - {@link StringBuilder} the {@link ValueToken} word representation will be appended.
	 * 
	 */
	protected AbstractTranscribingVisitor(final TranscriptionContext<?> context, final StringBuilder builder,
			final String wordSeparator) {
		super(context);

		this.builder = Objects.requireNonNull(builder, "Builder can not be null");

		this.wordSeparator = Objects.requireNonNull(wordSeparator,
				"Word separator can not be null (can be set to empty)");
	}

	/**
	 * Visits the {@link GroupListToken} tokens.
	 * <p>
	 * Will append the word representations of each of the children.
	 * <p>
	 * Example: 10100: "ten thousand", "one hundred".
	 */
	@Override
	public Void visitGroupList(GroupListToken token) {

		// for all tokens in the list
		for (ValueToken gr : token.getList()) {
			int oldLength = builder.length();

			// accept the visitor
			gr.accept(this);

			// if the builder was modified, append a separator
			if (oldLength != builder.length()) {
				builder.append(wordSeparator);
			}
		}

		if (builder.length() > 0 && wordSeparator.length() > 0) {
			// remove last separator
			builder.setLength(builder.length() - wordSeparator.length());
		}

		return null;
	}

	/**
	 * Visits the {@link MappedValueToken} tokens (leaf nodes).
	 * <p>
	 * Will append the word representation mapped by {@link TranscriptionContext context}.
	 * <p>
	 * Example: all values 0-20, then 30, 40, 50, etc...
	 */
	@Override
	public Void visitMappedValue(MappedValueToken token) {

		// mapped value tokens should be represented by a single string
		final String word = context.asWord(token.getMappedValue());
		builder.append(word);

		return null;
	}

	/**
	 * Visits the {@link PrefixedValueToken} tokens.
	 * <p>
	 * Will append the word representation for the prefix and then the value.
	 * <p>
	 * Example: 22: "twenty" "two".
	 */
	@Override
	public Void visitPrefixedValue(PrefixedValueToken token) {

		// write prefix
		token.getPrefixToken().accept(this);
		// write separator
		builder.append(wordSeparator);
		// write value
		token.getValueToken().accept(this);

		return null;
	}

	/**
	 * Visits the {@link SuffixedValueToken} tokens.
	 * <p>
	 * Will append the word representation for the value and then the suffix.
	 * <p>
	 * Example: 100: "one" "hundred".
	 */
	@Override
	public Void visitSuffixedValue(SuffixedValueToken token) {

		// write value
		token.getValueToken().accept(this);
		// write separator
		builder.append(wordSeparator);
		// write suffix
		token.getSuffixToken().accept(this);

		return null;
	}

	/**
	 * Visits the {@link DecimalValueToken} tokens.
	 * <p>
	 * Will append the word representation for the value before the decimal point, then add the decimal point
	 * representation, then the word representation for the value after the decimal point. *
	 * <p>
	 * Example: 100.12: "one hundred" "and" "twelve"
	 */
	@Override
	public Void visitDecimalValue(DecimalValueToken token) {

		// write the integer part
		token.getIntegerPart().accept(this);
		// write the decimal separator
		builder.append(wordSeparator).append(token.getDecimalSeparator()).append(wordSeparator);
		// write the fractional part
		token.getFractionalPart().accept(this);

		return null;
	}
}
