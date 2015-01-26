package com.mambu.number2words.internal.english.visitors;

import com.mambu.number2words.parsing.interfaces.TranscriptionContext;
import com.mambu.number2words.parsing.interfaces.ValueToken;
import com.mambu.number2words.parsing.tokenization.DecimalValueToken;
import com.mambu.number2words.parsing.tokenization.GroupListToken;
import com.mambu.number2words.parsing.tokenization.MappedValueToken;
import com.mambu.number2words.parsing.tokenization.PrefixedValueToken;
import com.mambu.number2words.parsing.tokenization.SuffixedValueToken;
import com.mambu.number2words.parsing.visitors.VoidVisitorAdaptor;

public class EnglishTokenVisitor extends VoidVisitorAdaptor {

	private static final char WORD_SEPARATOR = ' ';
	private final StringBuilder builder;

	public EnglishTokenVisitor(StringBuilder builder, TranscriptionContext context) {
		super(context);

		this.builder = builder;
	}

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

	@Override
	public Void visitMappedValue(MappedValueToken token) {

		// mapped value tokens should be represented by a single string
		final String word = context.asWord(token.getMappedValue());
		builder.append(word);

		return null;
	}

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
