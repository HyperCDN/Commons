package de.hypercdn.commons.util;

import java.math.BigInteger;

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

}
