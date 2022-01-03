package de.hypercdn.commons.imp.config;

import de.hypercdn.commons.api.config.Config;
import de.hypercdn.commons.util.BasicPattern;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Generic implementation of a config
 */
public class GenericConfig implements Config{

	private final Map<String, Object> structure;

	public GenericConfig(){
		this(new HashMap<>());
	}

	public GenericConfig(Map<String, Object> structure){
		Objects.requireNonNull(structure);
		this.structure = structure;
	}

	@Override
	public <T> T get(String keyChain){
		var keys = BasicPattern.DOT_PATTERN.split(keyChain);
		Object current = structure;
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

	@Override
	public Map<String, Object> getStructure(){
		return structure;
	}

}
