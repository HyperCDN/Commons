package de.hypercdn.commons.util.cursor;

import java.util.Iterator;

public abstract class Cursor<CONTAINER_TYPE, ELEMENT_TYPE> implements Iterator<ELEMENT_TYPE>, Iterable<ELEMENT_TYPE>{

	/**
	 * Record representing the state of a cursor
	 *
	 * @param <CONTAINER_TYPE>
	 * @param <ELEMENT_TYPE>
	 */
	public record State<CONTAINER_TYPE, ELEMENT_TYPE>(Cursor<CONTAINER_TYPE, ELEMENT_TYPE> cursor, CONTAINER_TYPE container, ELEMENT_TYPE element, int index, boolean end){}

	protected State<CONTAINER_TYPE, ELEMENT_TYPE> state;

	/**
	 * Returns the current state of this cursor
	 *
	 * @return current state
	 */
	public final State<CONTAINER_TYPE, ELEMENT_TYPE> getState(){
		return state;
	}

	/**
	 * Sets the state of the cursor to the provided one
	 *
	 * @param state new
	 */
	protected final void setState(State<CONTAINER_TYPE, ELEMENT_TYPE> state){
		this.state = state;
	}

	@Override
	public final boolean hasNext(){
		return hasNextState(getState());
	}

	@Override
	public final ELEMENT_TYPE next(){
		if(!hasNext()){
			return null;
		}
		var currentState = state;
		setState(nextState(currentState));
		return currentState.element;
	}

	/**
	 * Called to check whether the cursor has a next state for iteration
	 *
	 * @param currentState of the cursor
	 *
	 * @return true if another state is available
	 */
	protected abstract boolean hasNextState(State<CONTAINER_TYPE, ELEMENT_TYPE> currentState);

	/**
	 * Called to retrieve the next state for the cursor
	 *
	 * @param currentState of the cursor
	 *
	 * @return new state of the cursor
	 */
	protected abstract State<CONTAINER_TYPE, ELEMENT_TYPE> nextState(State<CONTAINER_TYPE, ELEMENT_TYPE> currentState);

	@Override
	public Iterator<ELEMENT_TYPE> iterator(){
		return this;
	}

}
