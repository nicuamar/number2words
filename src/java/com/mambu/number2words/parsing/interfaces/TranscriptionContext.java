package com.mambu.number2words.parsing.interfaces;

import com.mambu.number2words.parsing.interfaces.WordValue.Form;
import com.mambu.number2words.parsing.interfaces.WordValue.GrammaticalNumber;

/**
 * Implementations of this interface will provide access to semantic information necessary in the transcription process.
 * 
 * @author aatasiei
 */
public interface TranscriptionContext {

	/**
	 * Gets the representation of this number as a word.
	 * 
	 * @param value
	 *            - Long instance. Not <code>null</code>.
	 * @param form
	 * @param number
	 * @return a {@link String} instance. Never <code>null</code>.
	 */
	String asWord(Long value, GrammaticalNumber number, Form form);

}
