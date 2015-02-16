package com.mambu.number2words.parsing.interfaces;

public interface WordValue {

	public enum GrammaticalNumber {
		SINGULAR, PLURAL
	}

	public enum Form {
		SHORTENED, DEFAULT
	}

	/**
	 * Gets the word associated with this mapping.
	 * 
	 * @return String instance. Not null.
	 */
	String getWord();

	String getWord(final GrammaticalNumber number, final Form form);
	String getWord(final GrammaticalNumber number);
	String getWord(final Form form);
}
