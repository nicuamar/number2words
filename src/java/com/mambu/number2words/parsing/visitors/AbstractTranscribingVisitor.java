package com.mambu.number2words.parsing.visitors;

import java.util.Objects;

import com.mambu.number2words.parsing.interfaces.TranscriptionContext;
import com.mambu.number2words.parsing.interfaces.ValueToken;
import com.mambu.number2words.parsing.interfaces.WordValue.GrammaticalNumber;
import com.mambu.number2words.parsing.interfaces.WordValue.WordForm;
import com.mambu.number2words.parsing.tokenization.GroupListToken;
import com.mambu.number2words.parsing.tokenization.LiteralValueToken;
import com.mambu.number2words.parsing.tokenization.MappedValueToken;
import com.mambu.number2words.parsing.tokenization.PrefixedValueToken;
import com.mambu.number2words.parsing.tokenization.SuffixedValueToken;

/**
 * Adaptor for visitors that use {@link StringBuilder} instances to transcribe {@link ValueToken}s.
 * 
 * @author aatasiei
 *
 */
public abstract class AbstractTranscribingVisitor extends VisitorAdaptor<Void> {

	/**
	 * String builder to which the {@link ValueToken} word representation will be appended.
	 */
	protected final StringBuilder builder;

	/**
	 * String separator to be appended between words.
	 */
	protected final String wordSeparator;

	/**
	 * {@link ValueToken} evaluation context for this visitor.
	 */
	protected final TranscriptionContext context;

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
	protected AbstractTranscribingVisitor(final TranscriptionContext context, final StringBuilder builder,
			final String wordSeparator) {

		this.context = Objects.requireNonNull(context, "Transcription context can not be null.");
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
		final String word = context.asWord(token.getMappedValue(), GrammaticalNumber.SINGULAR, WordForm.DEFAULT);

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

	@Override
	public Void visitLiteral(LiteralValueToken literalValueToken) {

		builder.append(literalValueToken.getValue());

		return null;
	}
}
