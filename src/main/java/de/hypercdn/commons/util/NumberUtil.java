package de.hypercdn.commons.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;

public class NumberUtil{

	private NumberUtil(){}

	public static boolean isInteger(Number number){
		try{
			new BigInteger(number.toString());
			return true;
		}
		catch(Exception e){
			return false;
		}
	}

	public static boolean isDecimal(Number number){
		return !isInteger(number);
	}


	public static int compare(Number a, Number b){
		Objects.requireNonNull(a);
		Objects.requireNonNull(b);
		return new BigDecimal(a.toString()).compareTo(new BigDecimal(b.toString()));
	}

}
