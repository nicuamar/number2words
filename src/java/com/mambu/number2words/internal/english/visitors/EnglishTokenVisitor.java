package com.mambu.number2words.internal.english.visitors;

import com.mambu.number2words.parsing.interfaces.TranscriptionContext;
import com.mambu.number2words.parsing.interfaces.ValueToken;
import com.mambu.number2words.parsing.tokenization.DecimalValueToken;
import com.mambu.number2words.parsing.tokenization.GroupListToken;
import com.mambu.number2words.parsing.tokenization.MappedValueToken;
import com.mambu.number2words.parsing.tokenization.PrefixedValueToken;
import com.mambu.number2words.parsing.tokenization.SuffixedValueToken;
import com.mambu.number2words.parsing.visitors.VoidVisitorAdaptor;

/**
 * {@link ValueToken} visitor for English. It will build or print a {@link String} representation of a tree of
 * {@link ValueToken}s.
 * <p>
 * Not thread safe.
 * 
 * @author aatasiei
 *
 */
public class EnglishTokenVisitor extends VoidVisitorAdaptor {

	/**
	 * Word separator for English.
	 */
	private static final char WORD_SEPARATOR = ' ';

	/**
	 * String builder to which the {@link ValueToken} word representation will be appended.
	 */
	private final StringBuilder builder;

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
		super(context);

		this.builder = builder;
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
				builder.append(WORD_SEPARATOR);
			}
		}

		if (builder.length() > 0) {
			// remove last separator
			builder.setLength(builder.length() - 1);
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
		builder.append(WORD_SEPARATOR);
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
		builder.append(WORD_SEPARATOR);
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
		builder.append(WORD_SEPARATOR).append(token.getDecimalSeparator()).append(WORD_SEPARATOR);
		// write the fractional part
		token.getFractionalPart().accept(this);

		return null;
	}

}
