package de.hypercdn.commons.api.properties.link;

import de.hypercdn.commons.util.cursor.Cursor;
import de.hypercdn.commons.util.cursor.GenericCursor;

/**
 * Indicates that objects of the implementing class can be linked as layers
 *
 * @param <T>
 */
public interface Layer<T extends Layer<T>>{

	enum Side{
		INNER,
		OUTER
	}

	/**
	 * Points to the next inner object of the layer
	 *
	 * @return inner object
	 */
	T inner();

	/**
	 * Sets the provided object as inner one of the layer
	 *
	 * @param inner object
	 */
	void setInner(T inner);

	/**
	 * Points to the next outer object of the layer
	 *
	 * @return outer layer
	 */
	T outer();

	/**
	 * Sets the provided object as outer one of the layer
	 *
	 * @param outer object
	 */
	void outer(T outer);

	/**
	 * Retrieves a cursor for the layer starting from the current object
	 *
	 * @return cursor
	 */
	default Cursor<T, T> getLayerCursor(){
		return GenericCursor.forLayer(this, Side.INNER);
	}

}
