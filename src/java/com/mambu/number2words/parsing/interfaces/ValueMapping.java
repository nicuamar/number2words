package com.mambu.number2words.parsing.interfaces;

/**
 * Instances of this interface will represent a direct semantic mapping between a number and a word.
 * <p>
 * For example, in English:
 * <li>0 should be directly mapped to "zero"
 * <li>12 should be directly mapped to "twelve"
 * <li>100 should be directly mapped to "hundred", but only as a quantifier (because the transcribed version would be
 * "one hundred"). The same applies for 1,000, 1,000,000 and 1,000,000,000.
 * 
 * @author aatasiei
 *
 */
public interface ValueMapping {

	public static enum MappingType {
		SIMPLE, GROUP_QUANTIFIER, SUBGROUP_QUANTIFIER;
	}

	/**
	 * Gets the word associated with this mapping.
	 * 
	 * @return String instance. Not null.
	 */
	String getWord();

	/**
	 * Gets the number associate with this mapping.
	 * 
	 * @return {@link Long} instance. Not null.
	 */
	Long getValue();

	/**
	 * Specifies if this mapping is just a quantifier (used to separate large numbers into groups - thousands, millions,
	 * etc...)
	 * 
	 * @return true, if the mapping does just designate a quantifier.
	 */
	boolean isGroupQuantifier();

	boolean isSubGroupQuantifier();

}
