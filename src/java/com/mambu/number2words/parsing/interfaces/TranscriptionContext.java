package com.mambu.number2words.parsing.interfaces;

import com.mambu.number2words.parsing.interfaces.WordValue.GrammaticalNumber;
import com.mambu.number2words.parsing.interfaces.WordValue.WordForm;

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
	 * @param number
	 *            - the word's <i>grammatical</i> number. Not <code>null</code>.
	 * @param form
	 *            - the word's <i>form</i> (or variation). Not <code>null</code>.
	 * @return a {@link String} instance. Never <code>null</code>.
	 */
	String asWord(Long value, GrammaticalNumber number, WordForm form);

}
