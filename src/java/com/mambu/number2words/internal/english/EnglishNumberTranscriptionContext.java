package com.mambu.number2words.internal.english;

import com.mambu.number2words.parsing.interfaces.TranscriptionContext;

/**
 * Evaluation context when visiting tokens that were parsed using English semantics.
 * <p>
 * This class is thread safe as it stores no state and it deals with immutable data.
 * </p>
 * 
 * @author aatasiei
 *
 */
class EnglishNumberTranscriptionContext implements TranscriptionContext {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String asWord(Long value) {
		return EnglishNumberMapping.fromNumber(value).getWord();
	}

}
