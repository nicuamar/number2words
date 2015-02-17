package com.mambu.number2words.internal.spanish;

import com.mambu.number2words.internal.spanish.mapping.SpanishNumberMapping;
import com.mambu.number2words.parsing.interfaces.TranscriptionContext;
import com.mambu.number2words.parsing.interfaces.WordValue.Form;
import com.mambu.number2words.parsing.interfaces.WordValue.GrammaticalNumber;

/**
 * Evaluation context when visiting tokens that were parsed using English semantics.
 * <p>
 * This class is thread safe as it stores no state and it deals with immutable data.
 * </p>
 * 
 * @author aatasiei
 *
 */
public class SpanishNumberTranscriptionContext implements TranscriptionContext {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String asWord(Long value, GrammaticalNumber number, Form form) {
		return SpanishNumberMapping.fromNumber(value).getWordValue().getWord(number, form);
	}

}
