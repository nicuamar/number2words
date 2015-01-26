package com.mambu.number2words.parsing.tokenization;

import java.util.Collections;
import java.util.List;

import com.mambu.number2words.parsing.interfaces.ValueToken;
import com.mambu.number2words.parsing.interfaces.Visitor;

public class GroupListToken implements ValueToken {

	private List<ValueToken> groupList;

	public GroupListToken(List<ValueToken> groupList) {
		this.groupList = Collections.unmodifiableList(groupList);
	}

	public List<ValueToken> getList() {
		return groupList;
	}

	@Override
	public <V> V accept(Visitor<V> visitor) {
		return visitor.visitGroupList(this);
	}
}
