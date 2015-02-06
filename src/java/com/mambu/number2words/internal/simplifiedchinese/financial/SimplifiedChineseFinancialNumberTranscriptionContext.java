package com.mambu.number2words.internal.simplifiedchinese.financial;

import com.mambu.number2words.parsing.interfaces.TranscriptionContext;

public class SimplifiedChineseFinancialNumberTranscriptionContext implements
		TranscriptionContext<SimplifiedChineseFinancialNumberMapping> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String asWord(Long value) {
		return SimplifiedChineseFinancialNumberMapping.fromNumber(value).getWord();
	}

}
