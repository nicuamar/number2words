package com.mambu.number2words.internal.common.mapping;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Test;

import com.mambu.number2words.parsing.interfaces.ValueMapping;
import com.mambu.number2words.parsing.interfaces.WordValue;

/**
 * Tests for {@link QuantifyingMappingsHelper}.
 * 
 * @author aatasiei
 *
 */
public class QuantifyingMappingsHelperTest {

	// enum with mapped values in ascending order and groups/subgroups
	private static enum TestEnumValid implements ValueMapping {

		ZERO(0), ONE(1), TEN(1, MappingType.SUBGROUP_QUANTIFIER), HUNDRED(100, MappingType.GROUP_QUANTIFIER), THOUSAND(
				1000, MappingType.GROUP_QUANTIFIER);

		private long value;
		private MappingType mappingType;

		private TestEnumValid(long value) {
			this(value, MappingType.SIMPLE);
		}

		TestEnumValid(long value, MappingType mappingType) {
			this.value = value;
			this.mappingType = mappingType;
		}

		@Override
		public WordValue getWordValue() {
			// not necessary for the tests
			return null;
		}

		@Override
		public Long getValue() {
			return value;
		}

		@Override
		public boolean isGroupQuantifier() {
			return MappingType.GROUP_QUANTIFIER == mappingType;
		}

		@Override
		public boolean isSubGroupQuantifier() {
			return MappingType.SUBGROUP_QUANTIFIER == mappingType;
		}

	}

	// enum with mapped values in descending order (which is not allowed)
	private static enum TestEnumInvalidOrder implements ValueMapping {

		ZERO(0), ONE(-1);

		private long value;

		TestEnumInvalidOrder(long value) {
			this.value = value;
		}

		@Override
		public WordValue getWordValue() {
			// not necessary for the tests
			return null;
		}

		@Override
		public Long getValue() {
			return value;
		}

		@Override
		public boolean isGroupQuantifier() {
			// not necessary for the tests
			return false;
		}

		@Override
		public boolean isSubGroupQuantifier() {
			// not necessary for the tests
			return false;
		}

	}

	// enum with mapped values in ascending order but with no groups
	private static enum TestEnumValidOrderNoGroups implements ValueMapping {

		ZERO(0), ONE(1);

		private long value;

		TestEnumValidOrderNoGroups(long value) {
			this.value = value;
		}

		@Override
		public WordValue getWordValue() {
			// not necessary for the tests
			return null;
		}

		@Override
		public Long getValue() {
			return value;
		}

		@Override
		public boolean isGroupQuantifier() {
			// not necessary for the tests
			return false;
		}

		@Override
		public boolean isSubGroupQuantifier() {
			// not necessary for the tests
			return false;
		}

	}

	/**
	 * Test method for {@link QuantifyingMappingsHelper#groupQuantifiers(java.lang.Class)} .
	 */
	@Test
	public void testGroupQuantifiers() {
		// execute
		List<TestEnumValid> result = QuantifyingMappingsHelper.groupQuantifiers(TestEnumValid.class);

		// verify
		assertThat(result, not(empty()));
		assertThat(result, contains(TestEnumValid.THOUSAND, TestEnumValid.HUNDRED));
	}

	/**
	 * Test method for {@link QuantifyingMappingsHelper#groupQuantifiers(java.lang.Class)} .
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testGroupQuantifiersForInvalidOrderEnum() {
		// execute
		QuantifyingMappingsHelper.groupQuantifiers(TestEnumInvalidOrder.class);
	}

	/**
	 * Test method for {@link QuantifyingMappingsHelper#largestConsecutiveMapping(java.lang.Class)} .
	 */
	@Test
	public void testLargestConsecutiveMapping() {
		// execute
		TestEnumValid result = QuantifyingMappingsHelper.largestConsecutiveMapping(TestEnumValid.class);

		// verify
		assertThat(result, equalTo(TestEnumValid.ONE));
	}

	/**
	 * Test method for {@link QuantifyingMappingsHelper#subGroupQuantifiers(java.lang.Class)} .
	 */
	@Test
	public void testSubGroupQuantifiers() {
		// execute
		List<TestEnumValid> result = QuantifyingMappingsHelper.subGroupQuantifiers(TestEnumValid.class);

		// verify
		assertThat(result, not(empty()));
		assertThat(result, contains(TestEnumValid.TEN));
	}

	/**
	 * Test method for {@link QuantifyingMappingsHelper#minGroupQuantifier(java.lang.Class)} .
	 */
	@Test
	public void testMinGroupQuantifier() {
		// execute
		TestEnumValid result = QuantifyingMappingsHelper.minGroupQuantifier(TestEnumValid.class);

		// verify
		assertThat(result, equalTo(TestEnumValid.HUNDRED));
	}

	/**
	 * Test method for {@link QuantifyingMappingsHelper#maxGroupQuantifier(java.lang.Class)} .
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testMinGroupQuantifierForEnumWithNoGroups() {
		// execute
		QuantifyingMappingsHelper.minGroupQuantifier(TestEnumValidOrderNoGroups.class);
	}

	/**
	 * Test method for {@link QuantifyingMappingsHelper#maxGroupQuantifier(java.lang.Class)} .
	 */
	@Test
	public void testMaxGroupQuantifier() {
		// execute
		TestEnumValid result = QuantifyingMappingsHelper.maxGroupQuantifier(TestEnumValid.class);

		// verify
		assertThat(result, equalTo(TestEnumValid.THOUSAND));
	}

	/**
	 * Test method for {@link QuantifyingMappingsHelper#maxGroupQuantifier(java.lang.Class)} .
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testMaxGroupQuantifierForEnumWithNoGroups() {
		// execute
		QuantifyingMappingsHelper.maxGroupQuantifier(TestEnumValidOrderNoGroups.class);
	}

}
