package de.hypercdn.commons.util.cursor;

import de.hypercdn.commons.api.properties.link.Chain;
import de.hypercdn.commons.api.properties.link.Layer;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

/**
 * generic implementation of a cursor
 *
 * @param <CONTAINER_TYPE>
 * @param <ENTITY_TYPE>
 */
public class GenericCursor<CONTAINER_TYPE, ENTITY_TYPE> extends Cursor<CONTAINER_TYPE, ENTITY_TYPE>{

	private Function<State<CONTAINER_TYPE, ENTITY_TYPE>, Boolean> nextStateTest;
	private Function<State<CONTAINER_TYPE, ENTITY_TYPE>, State<CONTAINER_TYPE, ENTITY_TYPE>> nextStateProvider;

	/**
	 * Creates a new instance of this class
	 *
	 * @param initialStateProvider initial state
	 * @param nextStateTest        next-state test
	 * @param nextStateProvider    next-state provider
	 */
	public GenericCursor(Function<Cursor<CONTAINER_TYPE, ENTITY_TYPE>, State<CONTAINER_TYPE, ENTITY_TYPE>> initialStateProvider, Function<State<CONTAINER_TYPE, ENTITY_TYPE>, Boolean> nextStateTest, Function<State<CONTAINER_TYPE, ENTITY_TYPE>, State<CONTAINER_TYPE, ENTITY_TYPE>> nextStateProvider){
		this.state = initialStateProvider.apply(this);
		this.nextStateTest = nextStateTest;
		this.nextStateProvider = nextStateProvider;
	}

	/**
	 * Sets the next-state test to the supplied one
	 *
	 * @param nextStateTest to use
	 */
	public void setNextStateTest(Function<State<CONTAINER_TYPE, ENTITY_TYPE>, Boolean> nextStateTest){
		Objects.requireNonNull(nextStateTest);
		this.nextStateTest = nextStateTest;
	}

	/**
	 * Sets the next-state provider to the supplied one
	 *
	 * @param nextStateProvider to use
	 */
	public void setNextStateProvider(Function<State<CONTAINER_TYPE, ENTITY_TYPE>, State<CONTAINER_TYPE, ENTITY_TYPE>> nextStateProvider){
		Objects.requireNonNull(nextStateProvider);
		this.nextStateProvider = nextStateProvider;
	}

	@Override
	protected boolean hasNextState(State<CONTAINER_TYPE, ENTITY_TYPE> currentState){
		if(nextStateTest == null){
			throw new RuntimeException("Next-State check missing");
		}
		return nextStateTest.apply(currentState);
	}

	@Override
	protected State<CONTAINER_TYPE, ENTITY_TYPE> nextState(State<CONTAINER_TYPE, ENTITY_TYPE> currentState){
		if(nextStateProvider == null){
			throw new RuntimeException("Next-State provider missing");
		}
		var nextState = nextStateProvider.apply(currentState);
		if(nextState == null){
			throw new RuntimeException("Next-State provider returned null instead of a new state");
		}
		return nextState;
	}

	/**
	 * Creates a cursor to iterate the provided array
	 *
	 * @param obj           array
	 * @param <ENTITY_TYPE> array type
	 *
	 * @return cursor
	 */
	public static <ENTITY_TYPE> GenericCursor<ENTITY_TYPE[], ENTITY_TYPE> forArrays(ENTITY_TYPE[] obj){
		return new GenericCursor<>(
			(instance) -> new State<>(instance, obj, obj[0], 0, false),
			(state) -> !state.end(),
			(state) -> {
				if(state.container().length > state.index() + 1 || state.end()){
					return new State<>(state.cursor(), state.container(), null, state.end() ? state.index() : state.index() + 1, true);
				}
				else{
					return new State<>(state.cursor(), state.container(), state.container()[state.index() + 1], state.index() + 1, false);
				}
			}
		);
	}

	/**
	 * Creates a cursor to iterate the provided list
	 *
	 * @param obj           list
	 * @param <ENTITY_TYPE> list type
	 *
	 * @return cursor
	 */
	public static <ENTITY_TYPE> GenericCursor<List<ENTITY_TYPE>, ENTITY_TYPE> forLists(List<ENTITY_TYPE> obj){
		return new GenericCursor<>(
			(instance) -> new State<>(instance, obj, obj.get(0), 0, false),
			(state) -> !state.end(),
			(state) -> {
				if(state.container().size() > state.index() + 1 || state.end()){
					return new State<>(state.cursor(), state.container(), null, state.end() ? state.index() : state.index() + 1, true);
				}
				else{
					return new State<>(state.cursor(), state.container(), state.container().get(state.index() + 1), state.index() + 1, false);
				}
			}
		);
	}

	/**
	 * Creates a cursor to iterate the provided layers
	 *
	 * @param obj           layers
	 * @param side          to iterate
	 * @param <ENTITY_TYPE> layer type
	 *
	 * @return cursor
	 */
	public static <ENTITY_TYPE extends Layer<ENTITY_TYPE>> GenericCursor<ENTITY_TYPE, ENTITY_TYPE> forLayer(Layer<ENTITY_TYPE> obj, Layer.Side side){
		return new GenericCursor<>(
			(instance) -> new State<>(instance, (ENTITY_TYPE) obj, (ENTITY_TYPE) obj, 0, false),
			(state) -> !state.end(),
			(state) -> {
				var objx = switch(side){
					case OUTER -> state.container().outer();
					case INNER -> state.container().inner();
				};
				if(objx == null || state.end()){
					return new State<>(state.cursor(), state.container(), null, state.end() ? state.index() : state.index() + 1, true);
				}
				else{
					return new State<>(state.cursor(), objx, objx, state.index() + 1, false);
				}
			}
		);
	}

	/**
	 * Creates a cursor to iterate the provided chain
	 *
	 * @param obj           chain
	 * @param side          to iterate
	 * @param <ENTITY_TYPE> chain type
	 *
	 * @return cursor
	 */
	public static <ENTITY_TYPE extends Chain<ENTITY_TYPE>> GenericCursor<ENTITY_TYPE, ENTITY_TYPE> forChain(Chain<ENTITY_TYPE> obj, Chain.Side side){
		return new GenericCursor<>(
			(instance) -> new State<>(instance, (ENTITY_TYPE) obj, (ENTITY_TYPE) obj, 0, false),
			(state) -> !state.end(),
			(state) -> {
				var objx = switch(side){
					case NEXT -> state.container().next();
					case PREVIOUS -> state.container().previous();
				};
				if(state.container().next() == null || state.end()){
					return new State<>(state.cursor(), state.container(), null, state.end() ? state.index() : state.index() + 1, true);
				}
				else{
					return new State<>(state.cursor(), objx, objx, state.index() + 1, false);
				}
			}
		);
	}

}
