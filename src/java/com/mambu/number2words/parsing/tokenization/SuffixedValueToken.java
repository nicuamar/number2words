package com.mambu.number2words.parsing.tokenization;

import com.mambu.number2words.parsing.interfaces.ValueToken;
import com.mambu.number2words.parsing.interfaces.Visitor;

/**
 * Token that represents a value that is suffixed by another value.
 * <p>
 * For example: <b>"one"</b> "hundred"
 * 
 * @author aatasiei
 *
 */
public class SuffixedValueToken extends AbstractPairToken {

	/**
	 * Constructor for {@link SuffixedValueToken}
	 * 
	 * @param value
	 *            - the value token to prefix. Not <code>null</code>.
	 * @param suffix
	 *            - the suffix token. Not <code>null</code>.
	 */
	public SuffixedValueToken(ValueToken value, ValueToken suffix) {
		super(value, suffix);
	}

	/**
	 * Gets the value token being suffixed.
	 * 
	 * @return a {@link ValueToken} instance. Never <code>null</code>.
	 */
	public ValueToken getValueToken() {
		return getLeftToken();
	}

	/**
	 * Gets the suffix token.
	 * 
	 * @return a {@link ValueToken} instance. Never <code>null</code>.
	 */
	public ValueToken getSuffixToken() {
		return getRightToken();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <V> V accept(Visitor<V> visitor) {
		return visitor.visitSuffixedValue(this);
	}
}
