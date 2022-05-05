package de.hypercdn.commons.util;

import java.math.BigDecimal;
import java.util.Objects;

public class CompareUtil{

	private CompareUtil(){}


	/**
	 * Compares two numbers against each other
	 *
	 * @param a number
	 * @param b number
	 *
	 * @return comparison result
	 */
	public static int compare(Number a, Number b){
		Objects.requireNonNull(a);
		Objects.requireNonNull(b);
		return new BigDecimal(a.toString()).compareTo(new BigDecimal(b.toString()));
	}

	/**
	 * Interprets the result of a comparator with the given operator
	 *
	 * @param result   of the comparison, between -1 and 1
	 * @param operator to match
	 *
	 * @return true of the operator matched the comparator result
	 */
	public static boolean interpretComparatorResult(int result, Operator operator){
		return switch(operator){
			case EQUAL -> result == 0;
			case NOT_EQUAL -> result != 0;
			case GREATER -> result > 0;
			case GREATER_EQUAL -> result >= 0;
			case LESS -> result < 0;
			case LESS_EQUAl -> result <= 0;
		};
	}

	public enum Operator{
		EQUAL,
		NOT_EQUAL,
		GREATER,
		GREATER_EQUAL,
		LESS,
		LESS_EQUAl
	}

}
