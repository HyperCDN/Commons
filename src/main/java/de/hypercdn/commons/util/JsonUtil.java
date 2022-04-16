package de.hypercdn.commons.util;

import java.util.Map;

public class JsonUtil{

	private JsonUtil(){}

	public static <T> T flatAccess(Map<String, Object> jsonMap, String keyChain){
		var keys = BasicPattern.DOT_PATTERN.split(keyChain);
		Object current = jsonMap;
		for(String key : keys){
			if(current instanceof Map currentMap){
				if(!currentMap.containsKey(key)){
					return null;
				}
				current = currentMap.get(key);
			}
			else if(current.getClass().isArray()){
				try{
					int i = Integer.parseInt(key);
					if(((Object[]) current).length <= i){
						return null;
					}
					current = ((Object[]) current)[i];
				}
				catch(NumberFormatException e){
					return null;
				}
			}
			else{
				return null;
			}
		}
		return (T) current;
	}

}
