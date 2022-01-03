package de.hypercdn.commons.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;

/**
 * Util class for working with numbers
 */
public class NumberUtil{

	private NumberUtil(){}

	/**
	 * Checks if a number is an integer
	 *
	 * @param number to test
	 *
	 * @return true if it is an integer
	 */
	public static boolean isInteger(Number number){
		try{
			new BigInteger(number.toString());
			return true;
		}
		catch(Exception e){
			return false;
		}
	}

	/**
	 * Checks if a number is a decimal
	 *
	 * @param number to test
	 *
	 * @return true if it is a decimal
	 */
	public static boolean isDecimal(Number number){
		return !isInteger(number);
	}

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

}
