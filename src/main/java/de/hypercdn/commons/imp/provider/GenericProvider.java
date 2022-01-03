package de.hypercdn.commons.imp.provider;

import de.hypercdn.commons.api.provider.Provider;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * Generic provider implementation
 *
 * @param <I> input type
 * @param <O> output type
 */
public class GenericProvider<I, O> implements Provider<I, O>{

	private final Function<I, O> supplier;
	private final ConcurrentHashMap<I, O> elements = new ConcurrentHashMap<>();

	public GenericProvider(Function<I, O> supplier){
		Objects.requireNonNull(supplier);
		this.supplier = supplier;
	}

	@Override
	public synchronized O getStored(I i){
		return elements.get(i);
	}

	@Override
	public synchronized O getAndStore(I i){
		O o = elements.get(i);
		if(o != null){
			return o;
		}
		elements.put(i, supplier.apply(i));
		return elements.get(i);
	}

	@Override
	public synchronized void removeStored(I i){
		elements.remove(i);
	}

	@Override
	public void clear(){
		elements.clear();
	}

	@Override
	public int size(){
		return elements.size();
	}

}
