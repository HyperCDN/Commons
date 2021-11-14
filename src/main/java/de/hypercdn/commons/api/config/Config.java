package de.hypercdn.commons.api.config;

import java.util.Map;

/**
 * The interface Config.
 */
public interface Config{

	/**
	 * Get boolean boolean.
	 *
	 * @param keyChain the key chain
	 *
	 * @return the boolean
	 */
	default Boolean getBoolean(String keyChain){
		return get(keyChain);
	}

	/**
	 * Get short short.
	 *
	 * @param keyChain the key chain
	 *
	 * @return the short
	 */
	default Short getShort(String keyChain){
		return get(keyChain);
	}

	/**
	 * Get integer integer.
	 *
	 * @param keyChain the key chain
	 *
	 * @return the integer
	 */
	default Integer getInteger(String keyChain){
		return get(keyChain);
	}

	/**
	 * Get long long.
	 *
	 * @param keyChain the key chain
	 *
	 * @return the long
	 */
	default Long getLong(String keyChain){
		return get(keyChain);
	}

	/**
	 * Get double double.
	 *
	 * @param keyChain the key chain
	 *
	 * @return the double
	 */
	default Double getDouble(String keyChain){
		return get(keyChain);
	}

	/**
	 * Get float float.
	 *
	 * @param keyChain the key chain
	 *
	 * @return the float
	 */
	default Float getFloat(String keyChain){
		return get(keyChain);
	}

	/**
	 * Get object object.
	 *
	 * @param keyChain the key chain
	 *
	 * @return the object
	 */
	default Object getObject(String keyChain){
		return get(keyChain);
	}

	/**
	 * Get array object [ ].
	 *
	 * @param keyChain the key chain
	 *
	 * @return the object [ ]
	 */
	default Object[] getArray(String keyChain){
		return get(keyChain);
	}

	/**
	 * Get map map.
	 *
	 * @param keyChain the key chain
	 *
	 * @return the map
	 */
	default Map<String, Object> getMap(String keyChain){
		return get(keyChain);
	}

	/**
	 * Get t.
	 *
	 * @param <T>      the type parameter
	 * @param keyChain the key chain
	 *
	 * @return the t
	 */
	<T> T get(String keyChain);

	/**
	 * Gets structure.
	 *
	 * @return the structure
	 */
	Map<String, Object> getStructure();

}
