package com.mambu.number2words.parsing.tokenization;

import com.mambu.number2words.parsing.interfaces.ValueToken;
import com.mambu.number2words.parsing.interfaces.Visitor;

/**
 * Token representing an empty group.
 * <p>
 * For example: 1000 should only display "one thousand" not "one thousand zero".
 * 
 * @author aatasiei
 *
 */
public class NullValueToken implements ValueToken {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <V> V accept(Visitor<V> visitor) {
		return visitor.visitNullValue(this);
	}

	/**
	 * @return "nill"
	 */
	@Override
	public String toString() {
		return "nill";
	}
}
