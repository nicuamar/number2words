package com.mambu.number2words.parsing.interfaces;

public interface WordValue {

	public enum GrammaticalNumber {
		SINGULAR, PLURAL;

		public static final GrammaticalNumber getOrDefault(GrammaticalNumber number) {
			return number != null ? number : GrammaticalNumber.SINGULAR;
		}

		public boolean isPlural() {
			return this == PLURAL;
		};
	}

	public enum WordForm {
		SHORTENED, DEFAULT;

		public static final WordForm getOrDefault(WordForm form) {
			return form != null ? form : WordForm.DEFAULT;
		}

		public boolean isShortened() {
			return this == SHORTENED;
		};
	}

	/**
	 * Gets the word associated with this mapping.
	 * 
	 * @return String instance. Not null.
	 */
	String getWord(final GrammaticalNumber number, final WordForm form);
}
