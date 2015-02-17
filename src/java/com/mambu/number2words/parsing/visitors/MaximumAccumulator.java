package com.mambu.number2words.parsing.visitors;

import com.mambu.number2words.parsing.interfaces.ValueToken;
import com.mambu.number2words.parsing.tokenization.GroupListToken;
import com.mambu.number2words.parsing.tokenization.LiteralValueToken;
import com.mambu.number2words.parsing.tokenization.MappedValueToken;
import com.mambu.number2words.parsing.tokenization.NullValueToken;
import com.mambu.number2words.parsing.tokenization.PrefixedValueToken;
import com.mambu.number2words.parsing.tokenization.SuffixedValueToken;

/**
 * Visitor that retrieves maximum values form {@link ValueToken}s trees.
 * 
 * @author aatasiei
 *
 */
public class MaximumAccumulator extends VisitorAdaptor<Long> {

	/**
	 * Default constructor
	 */
	public MaximumAccumulator() {
	}

	/**
	 * Visits the {@link GroupListToken} tokens.
	 * <p>
	 * Will retrieve the maximum from all the tokens within the list and then return the maximum amongst them.
	 */
	@Override
	public Long visitGroupList(final GroupListToken token) {

		Long maxValue = 0L;

		// for all tokens in the list
		for (final ValueToken gr : token.getList()) {

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
	 * Will retrieve the value associated with this token.
	 */
	@Override
	public Long visitMappedValue(MappedValueToken token) {

		return token.getMappedValue();
	}

	/**
	 * Visits the {@link PrefixedValueToken} tokens.
	 * <p>
	 * Will retrieve the maximum between the tokens contained.
	 */
	@Override
	public Long visitPrefixedValue(PrefixedValueToken token) {

		// prefix
		final Long prefixValue = token.getPrefixToken().accept(this);
		// value
		final Long value = token.getValueToken().accept(this);

		return Math.max(prefixValue, value);
	}

	/**
	 * Visits the {@link SuffixedValueToken} tokens.
	 * <p>
	 * Will retrieve the maximum between the tokens contained.
	 */
	@Override
	public Long visitSuffixedValue(SuffixedValueToken token) {

		// value
		final Long value = token.getValueToken().accept(this);
		// suffix
		final Long suffixValue = token.getSuffixToken().accept(this);

		return Math.max(value, suffixValue);
	}

	/**
	 * Visits {@link LiteralValueToken} tokens. The maximum for these is 0.
	 */
	@Override
	public Long visitLiteral(LiteralValueToken token) {
		return 0L;
	}

	/**
	 * Visits {@link NullValueToken} tokens. The maximum for these is 0.
	 */
	@Override
	public Long visitNullValue(NullValueToken token) {
		return 0L;
	}
}
