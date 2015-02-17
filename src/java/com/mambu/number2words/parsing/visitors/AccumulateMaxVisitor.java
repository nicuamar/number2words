package com.mambu.number2words.parsing.visitors;

import com.mambu.number2words.parsing.interfaces.TranscriptionContext;
import com.mambu.number2words.parsing.interfaces.ValueToken;
import com.mambu.number2words.parsing.tokenization.DecimalValueToken;
import com.mambu.number2words.parsing.tokenization.GroupListToken;
import com.mambu.number2words.parsing.tokenization.LiteralValueToken;
import com.mambu.number2words.parsing.tokenization.MappedValueToken;
import com.mambu.number2words.parsing.tokenization.NullValueToken;
import com.mambu.number2words.parsing.tokenization.PrefixedValueToken;
import com.mambu.number2words.parsing.tokenization.SuffixedValueToken;

/**
 * Adaptor for visitors that use {@link StringBuilder} instances to transcribe {@link ValueToken}s.
 * 
 * @author aatasiei
 *
 */
public class AccumulateMaxVisitor extends VisitorAdaptor<Long> {

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
	public AccumulateMaxVisitor(final TranscriptionContext context) {
		super(context);
	}

	/**
	 * Visits the {@link GroupListToken} tokens.
	 * <p>
	 * Will append the word representations of each of the children.
	 * <p>
	 * Example: 10100: "ten thousand", "one hundred".
	 */
	@Override
	public Long visitGroupList(GroupListToken token) {

		Long maxValue = 0L;

		// for all tokens in the list
		for (ValueToken gr : token.getList()) {

			// accept the visitor
			final Long value = gr.accept(this);

			if (value > maxValue) {
				maxValue = value;
			}
		}

		return maxValue;
	}

	/**
	 * Visits the {@link MappedValueToken} tokens (leaf nodes).
	 * <p>
	 * Will append the word representation mapped by {@link TranscriptionContext context}.
	 * <p>
	 * Example: all values 0-20, then 30, 40, 50, etc...
	 */
	@Override
	public Long visitMappedValue(MappedValueToken token) {

		// mapped value tokens should be represented by a single string
		return token.getMappedValue();
	}

	/**
	 * Visits the {@link PrefixedValueToken} tokens.
	 * <p>
	 * Will append the word representation for the prefix and then the value.
	 * <p>
	 * Example: 22: "twenty" "two".
	 */
	@Override
	public Long visitPrefixedValue(PrefixedValueToken token) {

		// prefix
		Long prefixValue = token.getPrefixToken().accept(this);
		// value
		Long value = token.getValueToken().accept(this);

		return Math.max(prefixValue, value);
	}

	/**
	 * Visits the {@link SuffixedValueToken} tokens.
	 * <p>
	 * Will append the word representation for the value and then the suffix.
	 * <p>
	 * Example: 100: "one" "hundred".
	 */
	@Override
	public Long visitSuffixedValue(SuffixedValueToken token) {

		// value
		Long value = token.getValueToken().accept(this);
		// suffix
		Long suffixValue = token.getSuffixToken().accept(this);

		return Math.max(value, suffixValue);
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
	public Long visitDecimalValue(DecimalValueToken token) {

		throw new IllegalStateException();
	}

	@Override
	public Long visitLiteral(LiteralValueToken token) {
		return 0L;
	}

	@Override
	public Long visitNullValue(NullValueToken token) {
		return 0L;
	}
}
