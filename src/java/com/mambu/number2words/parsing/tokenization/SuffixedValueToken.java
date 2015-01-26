package com.mambu.number2words.parsing.tokenization;

import com.mambu.number2words.parsing.interfaces.ValueToken;
import com.mambu.number2words.parsing.interfaces.Visitor;

public class SuffixedValueToken extends AbstractPairToken {

	public SuffixedValueToken(ValueToken value, ValueToken suffix) {
		super(value, suffix);
	}

	public ValueToken getValueToken() {
		return getLeftToken();
	}

	public ValueToken getSuffixToken() {
		return getRightToken();
	}

	@Override
	public <V> V accept(Visitor<V> visitor) {
		return visitor.visitSuffixedValue(this);
	}
}
