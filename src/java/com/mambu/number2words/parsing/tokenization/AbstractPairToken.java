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
	 *            - token instance. Not null.
	 * @param right
	 *            - token instance. Not null.
	 */
	public AbstractPairToken(ValueToken left, ValueToken right) {
		this.left = Objects.requireNonNull(left);
		this.right = Objects.requireNonNull(right);
	}

	protected ValueToken getLeftToken() {
		return left;
	}

	protected ValueToken getRightToken() {
		return right;
	}

}
