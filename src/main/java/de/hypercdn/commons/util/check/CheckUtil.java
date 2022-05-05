package de.hypercdn.commons.util.check;

import java.util.Collection;

public class CheckUtil{

	private CheckUtil(){}

	public static <IN> Number sizeOf(IN in){
		if(in instanceof Collection<?> c){
			return c.size();
		}
		else if(in instanceof String s){
			return s.length();
		}
		else if(in != null && in.getClass().isArray()){
			return ((Object[]) in).length;
		}
		return null;
	}

}
