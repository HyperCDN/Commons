package de.hypercdn.commons.api.properties.link;

import de.hypercdn.commons.util.cursor.Cursor;
import de.hypercdn.commons.util.cursor.GenericCursor;

import java.util.Iterator;

/**
 * Indicates that objects of the implementing class can be linked as a chain
 *
 * @param <T>
 */
public interface Chain<T extends Chain<T>> extends Iterable<T>{

	enum Side{
		NEXT,
		PREVIOUS
	}

	/**
	 * Points to the next object within the current chain
	 *
	 * @return next object
	 */
	T next();

	/**
	 * Sets the provided object as next one within the current chain
	 *
	 * @param next object
	 */
	void setNext(T next);

	/**
	 * Points to the previous object within the current chain
	 *
	 * @return previous object
	 */
	T previous();

	/**
	 * Sets the provided object as previous one within the current chain
	 *
	 * @param previous
	 */
	void setPrevious(T previous);

	/**
	 * Retrieves a cursor for the chain starting from the current object
	 *
	 * @return cursor
	 */
	default Cursor<T, T> getChainCursor(Side side){
		return GenericCursor.forChain(this, side);
	}

	@Override
	default Iterator<T> iterator(){
		return getChainCursor(Side.NEXT);
	}

}
