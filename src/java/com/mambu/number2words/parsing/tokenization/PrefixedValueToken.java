package com.mambu.number2words.parsing.tokenization;

import com.mambu.number2words.parsing.interfaces.ValueToken;
import com.mambu.number2words.parsing.interfaces.Visitor;

public class PrefixedValueToken extends AbstractPairToken {

	public PrefixedValueToken(ValueToken prefix, ValueToken value) {
		super(value, prefix);
	}

	public ValueToken getValueToken() {
		return getLeftToken();
	}

	public ValueToken getPrefixToken() {
		return getRightToken();
	}

	@Override
	public <V> V accept(Visitor<V> visitor) {
		return visitor.visitPrefixedValue(this);
	}
}
