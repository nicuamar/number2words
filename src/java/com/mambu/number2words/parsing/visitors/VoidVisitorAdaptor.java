package com.mambu.number2words.parsing.visitors;

import java.util.Objects;

import com.mambu.number2words.parsing.interfaces.TranscriptionContext;
import com.mambu.number2words.parsing.interfaces.ValueToken;
import com.mambu.number2words.parsing.interfaces.Visitor;
import com.mambu.number2words.parsing.tokenization.DecimalValueToken;
import com.mambu.number2words.parsing.tokenization.GroupListToken;
import com.mambu.number2words.parsing.tokenization.LiteralValueToken;
import com.mambu.number2words.parsing.tokenization.MappedValueToken;
import com.mambu.number2words.parsing.tokenization.NullValueToken;
import com.mambu.number2words.parsing.tokenization.PrefixedValueToken;
import com.mambu.number2words.parsing.tokenization.SuffixedValueToken;

/**
 * Adaptor for {@link Visitor} using {@link Void} as return type for all visits.
 * 
 * @author aatasiei
 *
 */
public abstract class VoidVisitorAdaptor implements Visitor<Void> {

	/**
	 * {@link ValueToken} evaluation context for this visitor.
	 */
	protected final TranscriptionContext context;

	/**
	 * Default constructor
	 * 
	 * @param context
	 *            - context holding the necessary visiting information.
	 */
	protected VoidVisitorAdaptor(TranscriptionContext context) {
		this.context = Objects.requireNonNull(context, "Transcription context can not be null.");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Void visit(ValueToken token) {
		// default, do nothing
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Void visitNullValue(NullValueToken token) {
		// default, do nothing
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Void visitGroupList(GroupListToken token) {
		// default, do nothing
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Void visitMappedValue(MappedValueToken token) {
		// default, do nothing
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Void visitPrefixedValue(PrefixedValueToken token) {
		// default, do nothing
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Void visitSuffixedValue(SuffixedValueToken token) {
		// default, do nothing
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Void visitDecimalValue(DecimalValueToken token) {
		// default, do nothing
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Void visitLiteral(LiteralValueToken literalValueToken) {
		// default, do nothing
		return null;
	}
}
