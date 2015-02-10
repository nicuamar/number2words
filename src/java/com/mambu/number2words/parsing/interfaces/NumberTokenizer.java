package com.mambu.number2words.parsing.interfaces;

import java.math.BigDecimal;

/**
 * {@link NumberTokenizer}s will provide a way to transform a {@link BigDecimal} value into a {@link ValueToken}.
 * 
 * @author aatasiei
 *
 */
public interface NumberTokenizer {

	/**
	 * Tokenizes a non-negative decimal number.
	 * 
	 * @param number
	 *            - the number to tokenize.
	 * @return a {@link ValueToken} instance. Never <code>null</code>.
	 */
	ValueToken tokenize(final BigDecimal number);

}
