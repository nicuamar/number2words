package com.mambu.number2words.parsing.interfaces;

/**
 * A {@link ValueToken} is an immutable object that resulted from the tokenization of a number and can be transcribed
 * into words.
 * 
 * @author aatasiei
 *
 */
public interface ValueToken {

	/**
	 * Accepts the current node using the provided visitor.
	 *
	 * @param <V>
	 *            - the return type of the <code>visitor</code>.
	 * @param visitor
	 *            - the provided visitor.
	 * @return value returned by the visitor.
	 */
	<V> V accept(Visitor<V> visitor);
}
