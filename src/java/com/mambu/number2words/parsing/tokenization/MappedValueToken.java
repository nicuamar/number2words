package com.mambu.number2words.parsing.tokenization;

import com.mambu.number2words.parsing.interfaces.ValueToken;
import com.mambu.number2words.parsing.interfaces.Visitor;

public class MappedValueToken implements ValueToken {

	private long value;

	public MappedValueToken(long value) {
		if (value < 0) {
			throw new IllegalArgumentException();
		}
		this.value = value;
	}

	public long getMappedValue() {
		return this.value;
	}

	@Override
	public <V> V accept(Visitor<V> visitor) {
		return visitor.visitMappedValue(this);
	}
}
