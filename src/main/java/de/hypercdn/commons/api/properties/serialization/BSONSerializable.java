package de.hypercdn.commons.api.properties.serialization;

import org.bson.Document;

/**
 * Indicates that an object can be serialized as document through bson
 */
public interface BSONSerializable extends Serializable{

	/**
	 * Returns the object serialized as document
	 *
	 * @return serialized object
	 */
	Document serializeDocument();

	/**
	 * Returns the object serialized as json string
	 *
	 * @return serialized object
	 */
	default String serializeJSON(){
		return serializeDocument().toJson();
	}

}
