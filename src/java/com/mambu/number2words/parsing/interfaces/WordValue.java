package com.mambu.number2words.parsing.interfaces;

public interface WordValue {

	public enum GrammaticalNumber {
		SINGULAR, PLURAL;

		public static final GrammaticalNumber getOrDefault(GrammaticalNumber number) {
			return number != null ? number : GrammaticalNumber.SINGULAR;
		};
	}

	public enum Form {
		SHORTENED, DEFAULT;

		public static final Form getOrDefault(Form form) {
			return form != null ? form : Form.DEFAULT;
		};
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
