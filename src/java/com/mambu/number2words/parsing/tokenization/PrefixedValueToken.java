package com.mambu.number2words.parsing.tokenization;

import com.mambu.number2words.parsing.interfaces.ValueToken;
import com.mambu.number2words.parsing.interfaces.Visitor;

/**
 * Token that represents a value that is prefixed by another value.
 * <p>
 * For example: "twenty" <b>"one"</b>
 * 
 * @author aatasiei
 *
 */
public class PrefixedValueToken extends AbstractPairToken {

	/**
	 * Constructor for {@link PrefixedValueToken}
	 * 
	 * @param prefix
	 *            - the prefix token. Not <code>null</code>.
	 * @param value
	 *            - the value token to prefix. Not <code>null</code>.
	 */
	public PrefixedValueToken(ValueToken prefix, ValueToken value) {
		super(value, prefix);
	}

	/**
	 * Gets the value token being prefixed.
	 * 
	 * @return a {@link ValueToken} instance. Never <code>null</code>.
	 */
	public ValueToken getValueToken() {
		return getLeftToken();
	}

	/**
	 * Gets the prefix token.
	 * 
	 * @return a {@link ValueToken} instance. Never <code>null</code>.
	 */
	public ValueToken getPrefixToken() {
		return getRightToken();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <V> V accept(Visitor<V> visitor) {
		return visitor.visitPrefixedValue(this);
	}

	/**
	 * @return "["+ prefix + ", " + value + "]"
	 */
	@Override
	public String toString() {
		return "[" + getPrefixToken().toString() + ", " + getValueToken().toString() + "]";
	}
}
