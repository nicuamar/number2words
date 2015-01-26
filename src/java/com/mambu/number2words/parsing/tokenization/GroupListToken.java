package com.mambu.number2words.parsing.tokenization;

import java.util.Collections;
import java.util.List;

import com.mambu.number2words.parsing.interfaces.ValueToken;
import com.mambu.number2words.parsing.interfaces.Visitor;

/**
 * Token that represents a list of groups.
 * <p>
 * The groups are defined differently depending on the language and are usually 3 or 4 digits long.
 * <p>
 * For example, in English, 10100200 would be broken into 3 groups: 10(000000), 100(000), and 200. All the extra zeros
 * are stored actually stored in the "order" the tokens appear in the list.
 * 
 * @author aatasiei
 *
 */
public class GroupListToken implements ValueToken {

	/**
	 * List of {@link ValueToken}s.
	 */
	private List<ValueToken> groupList;

	/**
	 * Constructor for {@link GroupListToken}.
	 * 
	 * @param groupList
	 *            - a list of {@link ValueToken}s. Not <code>null</code>. Token takes ownership.
	 */
	public GroupListToken(List<ValueToken> groupList) {
		// tokens are immutable
		this.groupList = Collections.unmodifiableList(groupList);
	}

	/**
	 * Gets the list of {@link ValueToken}s that this token holds.
	 * 
	 * @return a immutable list of {@link ValueToken}s. Never <code>null</code>.
	 */
	public List<ValueToken> getList() {
		return groupList;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <V> V accept(Visitor<V> visitor) {
		return visitor.visitGroupList(this);
	}
}
