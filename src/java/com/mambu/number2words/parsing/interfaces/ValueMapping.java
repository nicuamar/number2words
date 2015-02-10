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

	/**
	 * Defines the type of mapping a certain {@link ValueMapping} represents.
	 * 
	 * @author aatasiei
	 *
	 */
	public static enum MappingType {
		/**
		 * This represents a simple number to {@link String words} mapping.
		 */
		SIMPLE,
		/**
		 * This represents a mapping that can be used to quantify sub-groups.
		 * <p>
		 * For example, when the smallest group quantifier is 1000, 123 can be divided into 3 sub-groups: 1 x 100, 2 x
		 * 10 and 3 x 1. <br/>
		 * In this case, 100 and 10 would be marked as {@link MappingType#SUBGROUP_QUANTIFIER}.
		 * <p>
		 * 1 is, by default, both a {@link MappingType#SUBGROUP_QUANTIFIER} and a {@link MappingType#GROUP_QUANTIFIER}
		 * but its value must not be marked as such.
		 */
		SUBGROUP_QUANTIFIER,
		/**
		 * This represents a mapping that can be used to quantify groups.
		 * <p>
		 * For example, when the smallest group quantifier is 1000, 87_352_321 can be divided into the following groups:
		 * 87 x 1_000_000, 352 x 1_000, 321 x 1.<br/>
		 * In this case, 1_000_000 and 1_000 would be marked as {@link MappingType#GROUP_QUANTIFIER}.
		 * <p>
		 * 1 is, by default, both a {@link MappingType#SUBGROUP_QUANTIFIER} and a {@link MappingType#GROUP_QUANTIFIER}
		 * but its value must not be marked as such.
		 */
		GROUP_QUANTIFIER;
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
	 * Specifies if this mapping is a group quantifier (used to separate large numbers into sections - thousands,
	 * millions, etc...)
	 * 
	 * @return true, if the mapping does designate a group quantifier.
	 * @see MappingType#GROUP_QUANTIFIER
	 */
	boolean isGroupQuantifier();

	/**
	 * Specifies if this mapping is a sub-group quantifier (used to separate groups into smaller sections - hundreds,
	 * tens, etc...)
	 * 
	 * @return true, if the mapping does designate a sub-group quantifier.
	 * @see MappingType#SUBGROUP_QUANTIFIER
	 */
	boolean isSubGroupQuantifier();

}
