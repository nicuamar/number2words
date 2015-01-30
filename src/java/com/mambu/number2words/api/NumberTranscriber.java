package com.mambu.number2words.api;

import java.math.BigDecimal;

/**
 * Implementations of this interface will provide a way to transcribe numerical values into words.
 * 
 * @author aatasiei
 *
 */
public interface NumberTranscriber {

	/**
	 * Returns the number transcribed into words.
	 * <p>
	 * <b>Note</b>: for now, only positive values are supported.
	 * 
	 * @param number
	 *            - BigDecimal instance to transcribe. Not <code>null</code>. Equal or greater than 0.
	 * @return String containing the transcribed number.
	 */
	String toWords(BigDecimal number);

	/**
	 * Appends the number transcribed into words to the StringBuilder instance.
	 * <p>
	 * <b>Note</b>: for now, only positive values are supported.
	 * 
	 * @param builder
	 *            - StringBuilder instance to which the words will be appended. Not null.
	 * @param number
	 *            - BigDecimal instance to transcribe. Not <code>null</code>. Equal or greater than 0.
	 */
	void appendWords(StringBuilder builder, BigDecimal number);

}
