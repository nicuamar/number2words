package com.mambu.number2words.parsing.interfaces;

/**
 * Represents a word that can be mapped to a number.
 * <p>
 * Usually this is done using a single {@link String} <i>one</i>, <i>twelve</i>, etc. In some situations, the word might
 * need to be modified to match certain rules (grammar, etc..). These variations are represented using
 * {@link GrammaticalNumber} and {@link WordForm}.
 * 
 * @author aatasiei
 *
 */
public interface WordValue {

	/**
	 * Represents the <i>grammatical</i> number of a word (singular or plural).
	 * 
	 * @author aatasiei
	 *
	 */
	enum GrammaticalNumber {
		SINGULAR, PLURAL;
	}

	/**
	 * Represents the mapping's <i>forms</i> or <i>variations</i>. This is should be used when words are shortened in
	 * special circumstances.
	 * 
	 * @author aatasiei
	 *
	 */
	enum WordForm {
		/**
		 * Marks a shortened version of a mapping.
		 */
		SHORTENED,
		/**
		 * Marks the default version of a mapping.
		 */
		DEFAULT;
	}

	/**
	 * Gets the word associated with this mapping.
	 * 
	 * @param number
	 *            - the <i>grammatical</i> number. Not <code>null</code>.
	 * @param form
	 *            - the <i>word form</i>. Not <code>null</code>.
	 * 
	 * @return String instance. Never <code>null</code>.
	 */
	String getWord(final GrammaticalNumber number, final WordForm form);
}
