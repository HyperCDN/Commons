package de.hypercdn.commons.api.properties.serialization;

import org.bson.Document;

import java.nio.charset.StandardCharsets;

public interface BSONSerializable extends Serializable{

	Document serializeDocument();

	default String serializeJSON(){
		return serializeDocument().toJson();
	}

	@Override
	default byte[] serialize(){
		return serializeJSON().getBytes(StandardCharsets.UTF_8);
	}

}
