package com.mambu.number2words.parsing.interfaces;

import com.mambu.number2words.parsing.tokenization.GroupListToken;
import com.mambu.number2words.parsing.tokenization.LiteralValueToken;
import com.mambu.number2words.parsing.tokenization.MappedValueToken;
import com.mambu.number2words.parsing.tokenization.NullValueToken;
import com.mambu.number2words.parsing.tokenization.PrefixedValueToken;
import com.mambu.number2words.parsing.tokenization.SuffixedValueToken;

/**
 * Visitor for tokens resulted from the tokenization of a number.
 * <p>
 * This should be implemented for token traversal.
 * 
 * @author aatasiei
 *
 * @param <V>
 *            the type that should be returned for all visits. This can be helpful in making the visitor state-less.
 */
public interface Visitor<V> {

	/**
	 * Visits the null tokens.
	 * 
	 * @param token
	 *            - the token to visit.
	 * @return the result of the visit. May be <code>null</code>.
	 */
	public V visitNullValue(NullValueToken token);

	/**
	 * Visits the group list tokens.
	 * 
	 * @param token
	 *            - the token to visit.
	 * @return the result of the visit. May be <code>null</code>.
	 */
	public V visitGroupList(GroupListToken token);

	/**
	 * Visits the value mapped tokens.
	 * 
	 * @param token
	 *            - the token to visit.
	 * @return the result of the visit. May be <code>null</code>.
	 */
	public V visitMappedValue(MappedValueToken token);

	/**
	 * Visits the value tokens with prefixes.
	 * 
	 * @param token
	 *            - the token to visit.
	 * @return the result of the visit. May be <code>null</code>.
	 */
	public V visitPrefixedValue(PrefixedValueToken token);

	/**
	 * Visits the value tokens with suffixes.
	 * 
	 * @param token
	 *            - the token to visit.
	 * @return the result of the visit. May be <code>null</code>.
	 */
	public V visitSuffixedValue(SuffixedValueToken token);

	public V visitLiteral(LiteralValueToken literalValueToken);

}
