package de.hypercdn.commons.imp.config;

import de.hypercdn.commons.api.config.Config;
import de.hypercdn.commons.util.JsonUtil;

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
		return JsonUtil.flatAccess(structure, keyChain);
	}

	@Override
	public Map<String, Object> getStructure(){
		return structure;
	}

}
