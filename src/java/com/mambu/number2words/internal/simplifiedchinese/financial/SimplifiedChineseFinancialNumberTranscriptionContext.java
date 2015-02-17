package com.mambu.number2words.internal.simplifiedchinese.financial;

import com.mambu.number2words.internal.simplifiedchinese.financial.mapping.SimplifiedChineseFinancialNumberMapping;
import com.mambu.number2words.parsing.interfaces.TranscriptionContext;
import com.mambu.number2words.parsing.interfaces.WordValue.GrammaticalNumber;
import com.mambu.number2words.parsing.interfaces.WordValue.WordForm;

/**
 * Evaluation context when visiting tokens that were parsed using Simplified Chinese semantics using Financial numerals.
 * <p>
 * This class is thread safe as it stores no state and it deals with immutable data.
 * </p>
 * 
 * @author aatasiei
 *
 */
public class SimplifiedChineseFinancialNumberTranscriptionContext implements TranscriptionContext {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String asWord(Long value, GrammaticalNumber number, WordForm form) {
		return SimplifiedChineseFinancialNumberMapping.fromNumber(value).getWordValue().getWord(number, form);
	}

}
