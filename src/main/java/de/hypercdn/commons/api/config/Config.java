package de.hypercdn.commons.api.config;

import java.util.List;
import java.util.Map;

/**
 * Represents a config file
 */
public interface Config{

	/**
	 * Retrieves a boolean from the config
	 *
	 * @param keyChain to select the right entry
	 *
	 * @return boolean or null if no object has been found at the specified chain path
	 */
	default Boolean getBoolean(String keyChain){
		return get(keyChain);
	}

	/**
	 * Retrieves a short from the config
	 *
	 * @param keyChain to select the right entry
	 *
	 * @return short or null if no object has been found at the specified chain path
	 */
	default Short getShort(String keyChain){
		return get(keyChain);
	}

	/**
	 * Retrieves an integer from the config
	 *
	 * @param keyChain to select the right entry
	 *
	 * @return integer or null if no object has been found at the specified chain path
	 */
	default Integer getInteger(String keyChain){
		return get(keyChain);
	}

	/**
	 * Retrieves a long from the config
	 *
	 * @param keyChain to select the right entry
	 *
	 * @return long or null if no object has been found at the specified chain path
	 */
	default Long getLong(String keyChain){
		return get(keyChain);
	}

	/**
	 * Retrieves a double from the config
	 *
	 * @param keyChain to select the right entry
	 *
	 * @return double or null if no object has been found at the specified chain path
	 */
	default Double getDouble(String keyChain){
		return get(keyChain);
	}

	/**
	 * Retrieves a float from the config
	 *
	 * @param keyChain to select the right entry
	 *
	 * @return float or null if no object has been found at the specified chain path
	 */
	default Float getFloat(String keyChain){
		return get(keyChain);
	}

	/**
	 * Retrieves an array from the config
	 *
	 * @param keyChain to select the right entry
	 * @param <T>      of the return
	 *
	 * @return array or null if no object has been found at the specified chain path
	 */
	default <T> T[] getArray(String keyChain){
		return get(keyChain);
	}

	/**
	 * Retrieves a list from the config
	 *
	 * @param keyChain to select the right entry
	 * @param <T>      of the return
	 *
	 * @return list or null if no object has been found at the specified chain path
	 */
	default <T> List<T> getList(String keyChain){
		return get(keyChain);
	}

	/**
	 * Retrieves a map from the config
	 *
	 * @param keyChain to select the right entry
	 *
	 * @return map or null if no object has been found at the specified chain path
	 */
	default Map<String, Object> getMap(String keyChain){
		return get(keyChain);
	}

	/**
	 * Retrieves an object from the config
	 *
	 * @param keyChain to select the right entry
	 * @param <T>      of the return
	 *
	 * @return object or null if no object has been found at the specified chain path
	 */
	<T> T get(String keyChain);

	/**
	 * Returns the underlying data structure
	 *
	 * @return config data
	 */
	Map<String, Object> getStructure();

}
