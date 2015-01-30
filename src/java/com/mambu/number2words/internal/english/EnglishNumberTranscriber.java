package com.mambu.number2words.internal.english;

import java.math.BigDecimal;

import com.mambu.number2words.api.NumberTranscriber;
import com.mambu.number2words.internal.english.tokenization.EnglishNumberTokenizer;
import com.mambu.number2words.internal.english.visitors.EnglishTokenVisitor;
import com.mambu.number2words.parsing.interfaces.TranscriptionContext;
import com.mambu.number2words.parsing.interfaces.ValueToken;

/**
 * Number transcriber for the English language.
 * 
 * @author aatasiei
 *
 */
public class EnglishNumberTranscriber implements NumberTranscriber {

	/**
	 * Tokenizer for English. Implementation is state-less and is thread safe.
	 */
	private final static EnglishNumberTokenizer TOKENIZER = new EnglishNumberTokenizer();
	/**
	 * Evaluation context used to transcribe the tokens.
	 */
	private final static TranscriptionContext CONTEXT = new EnglishNumberTranscriptionContext();

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toWords(BigDecimal number) {

		StringBuilder sb = new StringBuilder();
		appendWords(sb, number);

		return sb.toString();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void appendWords(StringBuilder builder, BigDecimal number) {

		ValueToken root = TOKENIZER.tokenize(number);

		root.accept(new EnglishTokenVisitor(builder, CONTEXT));

	}

}
