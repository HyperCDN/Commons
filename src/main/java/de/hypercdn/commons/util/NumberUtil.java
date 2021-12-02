package de.hypercdn.commons.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;

/**
 * The type Number util.
 */
public class NumberUtil{

	private NumberUtil(){}

	/**
	 * Is integer boolean.
	 *
	 * @param number the number
	 *
	 * @return the boolean
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
	 * Is decimal boolean.
	 *
	 * @param number the number
	 *
	 * @return the boolean
	 */
	public static boolean isDecimal(Number number){
		return !isInteger(number);
	}


	/**
	 * Compare int.
	 *
	 * @param a the a
	 * @param b the b
	 *
	 * @return the int
	 */
	public static int compare(Number a, Number b){
		Objects.requireNonNull(a);
		Objects.requireNonNull(b);
		return new BigDecimal(a.toString()).compareTo(new BigDecimal(b.toString()));
	}

}
