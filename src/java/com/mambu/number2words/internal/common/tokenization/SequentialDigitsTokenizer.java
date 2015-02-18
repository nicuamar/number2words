package com.mambu.number2words.internal.common.tokenization;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.mambu.number2words.parsing.interfaces.NumberTokenizer;
import com.mambu.number2words.parsing.interfaces.ValueToken;
import com.mambu.number2words.parsing.tokenization.GroupListToken;
import com.mambu.number2words.parsing.tokenization.MappedValueToken;

public class SequentialDigitsTokenizer implements NumberTokenizer {

	@Override
	public ValueToken tokenize(BigDecimal number) {
		final BigInteger integer = number.toBigIntegerExact();

		BigInteger toTokenize = integer;

		final List<ValueToken> tokens = new ArrayList<>();

		while (toTokenize.compareTo(BigInteger.ZERO) != 0) {

			final BigInteger[] values = toTokenize.divideAndRemainder(BigInteger.TEN);

			toTokenize = values[0];

			final BigInteger currentDigit = values[1];

			tokens.add(new MappedValueToken(currentDigit.longValue()));
		}

		Collections.reverse(tokens);

		return new GroupListToken(tokens);
	}

}
