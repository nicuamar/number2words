package com.mambu.number2words.parsing.tokenization;

import java.util.Objects;

import com.mambu.number2words.parsing.interfaces.ValueToken;

/**
 * Represents a pair of tokens.
 * 
 * @author aatasiei
 *
 */
public abstract class AbstractPairToken implements ValueToken {

	private ValueToken left;
	private ValueToken right;

	/**
	 * Default constructor.
	 * 
	 * @param left
	 *            - token instance. Not <code>null</code>.
	 * @param right
	 *            - token instance. Not <code>null</code>.
	 */
	public AbstractPairToken(ValueToken left, ValueToken right) {
		this.left = Objects.requireNonNull(left);
		this.right = Objects.requireNonNull(right);
	}

	/**
	 * Gets the left token.
	 * 
	 * @return a {@link ValueToken}. Never <code>null</code>.
	 */
	protected ValueToken getLeftToken() {
		return left;
	}

	/**
	 * Gets the right token.
	 * 
	 * @return a {@link ValueToken}. Never <code>null</code>.
	 */
	protected ValueToken getRightToken() {
		return right;
	}

}
