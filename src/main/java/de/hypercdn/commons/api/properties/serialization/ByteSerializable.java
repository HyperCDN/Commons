package de.hypercdn.commons.api.properties.serialization;

/**
 * Indicates that an object can be serialized to a byte[]
 */
public interface ByteSerializable extends Serializable{

	/**
	 * Returns the object serialized as byte array
	 *
	 * @return serialized object
	 */
	byte[] serialize();

}
