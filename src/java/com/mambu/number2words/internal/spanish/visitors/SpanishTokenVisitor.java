package com.mambu.number2words.internal.spanish.visitors;

import java.util.Objects;

import com.mambu.number2words.parsing.interfaces.TranscriptionContext;
import com.mambu.number2words.parsing.interfaces.ValueToken;
import com.mambu.number2words.parsing.interfaces.WordValue.GrammaticalNumber;
import com.mambu.number2words.parsing.interfaces.WordValue.WordForm;
import com.mambu.number2words.parsing.tokenization.GroupListToken;
import com.mambu.number2words.parsing.tokenization.MappedValueToken;
import com.mambu.number2words.parsing.tokenization.PrefixedValueToken;
import com.mambu.number2words.parsing.tokenization.SuffixedValueToken;
import com.mambu.number2words.parsing.visitors.AbstractTranscribingVisitor;
import com.mambu.number2words.parsing.visitors.MaximumAccumulator;

/**
 * {@link ValueToken} visitor for Spanish numbers. It will build or print a {@link String} representation of a tree of
 * {@link ValueToken}s.
 * <p>
 * Not thread safe.
 * 
 * @author aatasiei
 *
 */
public class SpanishTokenVisitor extends AbstractTranscribingVisitor {

	/**
	 * Word separator for Spanish.
	 */
	private static final String WORD_SEPARATOR = " ";

	/**
	 * Visitor used to determine the maximum value on a ValueToken tree.
	 */
	private MaximumAccumulator maxAccumulator;

	/**
	 * The grammatical number (SINGULAR/PLURAL) of the word being printed ("millon" vs "millones").
	 */
	private GrammaticalNumber wordNumber = GrammaticalNumber.SINGULAR;

	/**
	 * The form of the word being printed. Some words have shorter variations that are used in certain situations ("uno"
	 * vs "un", "ciento" vs "cien").
	 */
	private WordForm wordForm = WordForm.DEFAULT;

	/**
	 * Default constructor.
	 * 
	 * @param builder
	 *            - {@link StringBuilder} the {@link ValueToken} word representation will be appended.
	 * 
	 * @param context
	 *            - the context that holds the number to word mapping information.
	 * 
	 */
	public SpanishTokenVisitor(StringBuilder builder, TranscriptionContext context) {
		super(context, builder, WORD_SEPARATOR);
		this.maxAccumulator = new MaximumAccumulator();
	}

	/**
	 * Computes the maximum of the sub-tree represented by the passed parameter.
	 * 
	 * @param token
	 *            - the root of the sub-tree. Not <code>null</code>.
	 * @return the maximum value that appears in the sub-tree.
	 */
	private Long maximum(final ValueToken token) {
		return token.accept(maxAccumulator);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Void visitGroupList(final GroupListToken token) {

		Long previousMax = 0L;

		// for all tokens in the list
		for (final ValueToken gr : token.getList()) {

			final int oldLength = builder.length();

			final Long currentMax = maximum(gr);

			// using the previous maximum to determine whether the quantifier should use a plural.
			// using the current maximum to determine whether directly mapped values should be shortened.

			// accept the visitor
			accept(gr, determineNumberForQuantifier(previousMax), determineFormForGroup(gr, currentMax));

			// if the builder was modified, append a separator
			if (oldLength != builder.length()) {
				builder.append(wordSeparator);
			}

			previousMax = currentMax;
		}

		if (builder.length() > 0 && wordSeparator.length() > 0) {
			// remove last separator
			builder.setLength(builder.length() - wordSeparator.length());
		}

		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Void visitMappedValue(final MappedValueToken token) {

		final String word = context.asWord(token.getMappedValue(), wordNumber, wordForm);

		builder.append(word);

		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Void visitPrefixedValue(final PrefixedValueToken token) {

		final Long maxPrefixValue = maximum(token.getPrefixToken());
		final Long maxValue = maximum(token.getValueToken());

		// write prefix
		// using "ciento" vs just "cien" (when prefixing sub-group values)
		acceptWithForm(token.getPrefixToken(), determinePrefixForm(maxValue, maxPrefixValue));

		// write separator
		builder.append(wordSeparator);

		// write value
		// handling "ciento uno" ("uno" could have been shortened to "un" before this)
		acceptWithForm(token.getValueToken(), WordForm.DEFAULT);

		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Void visitSuffixedValue(SuffixedValueToken token) {

		final Long maxValue = maximum(token.getValueToken());
		final Long maxSuffixValue = maximum(token.getSuffixToken());

		// write value
		acceptWithForm(token.getValueToken(), determineValueFormBasedOnSuffix(maxValue, maxSuffixValue));

		// write separator
		builder.append(wordSeparator);

		// write suffix
		acceptWithNumber(token.getSuffixToken(), determineNumberForQuantifier(maxValue));

		return null;
	}

	/**
	 * Determines the form of a mapped value token, that is a top level token in a group list, based on contextual
	 * information.
	 * 
	 * This is used to determine whether "cien" should be printed. "cien" should appear when 100 is alone (as group
	 * value).
	 * 
	 * @param token
	 *            - the token that this Form would apply to. <code>null</code>s are ignored.
	 * @param currentMax
	 *            - the result of {@link #maximum(ValueToken)} called on <code>token</token>. Not <code>null</code>.
	 * @return the {@link WordForm} that should be applied on the token.
	 */
	private WordForm determineFormForGroup(final ValueToken token, final Long currentMax) {

		// 100 is a top level value tokens. if found it should be shortened to "cien"
		if (token instanceof MappedValueToken && currentMax == 100L) {
			return WordForm.SHORTENED;
		}
		return wordForm;
	}

	/**
	 * Determines the form of a mapped value token, that serves as a prefix, based on contextual information.
	 * 
	 * This is used to determine whether "ciento" should be written. "ciento" should appear when 100 prefixes other
	 * numbers.
	 * 
	 * @param value
	 *            - the value that would be prefixed (computed from a maximum).
	 * @param prefix
	 *            - the prefix value (computed from a maximum).
	 * @return the {@link WordForm} of the prefix.
	 */
	private WordForm determinePrefixForm(final Long value, final Long prefix) {
		if (prefix == 100L && value > 0L) {
			return WordForm.DEFAULT;
		}
		return wordForm;
	}

	/**
	 * Determines whether "cien" or "un" should be used (when the quantifiers are "millon" or higher).
	 * 
	 * @param value
	 *            - the value that would be suffixed (computed from a maximum). Not <code>null</code>.
	 * @param suffix
	 *            - the suffix value (computed from a maximum). Not <code>null</code>.
	 * @return the form of the value token.
	 */
	private WordForm determineValueFormBasedOnSuffix(final Long value, final Long suffix) {
		// when the suffix is "millon(es)" or higher, 1 and 100 are shortened to "un" and "cien"
		if ((suffix >= 1000L) && (value == 100L || value == 1L)) {
			return WordForm.SHORTENED;
		}

		return wordForm;
	}

	/**
	 * Determines the grammatical number for quantifiers. If the group value is 1, the quantifier should display using
	 * SINGULAR. PLURAL otherwise.
	 * 
	 * @param value
	 *            - the group value. Not <code>null</code>.
	 * @return the number of the group quantifier.
	 */
	private GrammaticalNumber determineNumberForQuantifier(final Long value) {
		// when a group has a value > 1, then a plural may be used ("millones" instead of "millon")
		return value > 1L ? GrammaticalNumber.PLURAL : GrammaticalNumber.SINGULAR;
	}

	/**
	 * Shorthand for {@link #accept(ValueToken, GrammaticalNumber, WordForm)} when not modifying the grammatical number.
	 * 
	 * @param token
	 *            - the token to accept. Not <code>null</code>.
	 * @param form
	 *            - the word form for the token. Not <code>null</code>.
	 */
	private void acceptWithForm(final ValueToken token, final WordForm form) {
		accept(token, wordNumber, form);
	}

	/**
	 * Shorthand for {@link #accept(ValueToken, GrammaticalNumber, WordForm)} when not modifying the word form.
	 * 
	 * @param token
	 *            - the token to accept. Not <code>null</code>.
	 * @param number
	 *            - the grammatical number for the token. Not <code>null</code>.
	 */
	private void acceptWithNumber(final ValueToken token, final GrammaticalNumber number) {
		accept(token, number, wordForm);
	}

	/**
	 * Accepts the token, using the grammatical number and word form provided, while storing and restoring the current
	 * values for {@link #wordForm} and {@link #wordNumber}.
	 * 
	 * @param token
	 *            - the token to accept. Not <code>null</code>.
	 * @param number
	 *            - the grammatical number for the token. Not <code>null</code>.
	 * @param form
	 *            - the word form for the token. Not <code>null</code>.
	 */
	private void accept(final ValueToken token, final GrammaticalNumber number, final WordForm form) {

		// using the stack to store the previous values

		final GrammaticalNumber oldNumber = this.wordNumber;
		final WordForm oldForm = this.wordForm;

		// setting the visitation state for mapped values depending on the parameters

		this.wordNumber = Objects.requireNonNull(number);
		this.wordForm = Objects.requireNonNull(form);

		token.accept(this);

		// restore the old state
		this.wordNumber = oldNumber;
		this.wordForm = oldForm;

	}

}
