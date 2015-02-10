package com.mambu.number2words.parsing.interfaces;

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
	 * @return a {@link String} instance. Never <code>null</code>.
	 */
	String asWord(Long value);

}
