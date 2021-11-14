package de.hypercdn.commons.api.config;

import java.util.Map;

public interface Config{

	default Boolean getBoolean(String keyChain){
		return get(keyChain);
	}

	default Short getShort(String keyChain){
		return get(keyChain);
	}

	default Integer getInteger(String keyChain){
		return get(keyChain);
	}

	default Long getLong(String keyChain){
		return get(keyChain);
	}

	default Double getDouble(String keyChain){
		return get(keyChain);
	}

	default Float getFloat(String keyChain){
		return get(keyChain);
	}

	default Object getObject(String keyChain){
		return get(keyChain);
	}

	default Object[] getArray(String keyChain){
		return get(keyChain);
	}

	default Map<String, Object> getMap(String keyChain){
		return get(keyChain);
	}

	<T> T get(String keyChain);

	Map<String, Object> getStructure();

}
