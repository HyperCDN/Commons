package de.hypercdn.commons.imp.structure;

import de.hypercdn.commons.api.structure.Structure;
import de.hypercdn.commons.imp.wrap.Inherited;
import de.hypercdn.commons.util.check.Check;

import java.util.function.Predicate;

/**
 * The type Simple.
 */
public class Simple{

	private Class<?> tClass;
	private Predicate<Inherited> predicate;
	private Structure structure;

	/**
	 * Instantiates a new Simple.
	 *
	 * @param tClass the t class
	 */
	public Simple(Class<?> tClass){
		this(tClass, null);
	}

	/**
	 * Instantiates a new Simple.
	 *
	 * @param tClass    the t class
	 * @param predicate the predicate
	 */
	public Simple(Class<?> tClass, Predicate<Inherited> predicate){
		this.tClass = tClass;
		this.predicate = predicate;
	}

	/**
	 * Instantiates a new Simple.
	 *
	 * @param structure the structure
	 */
	public Simple(Structure structure){
		this.structure = structure;
	}

	/**
	 * Get t class class.
	 *
	 * @return the class
	 */
	public Class<?> getTClass(){
		return tClass;
	}

	/**
	 * Get predicate predicate.
	 *
	 * @return the predicate
	 */
	public Predicate<Inherited> getPredicate(){
		return predicate;
	}

	/**
	 * Add predicate simple.
	 *
	 * @param predicate the predicate
	 *
	 * @return the simple
	 */
	public Simple addPredicate(Predicate<Inherited> predicate){
		if(this.predicate == null){
			this.predicate = predicate;
		}
		else{
			this.predicate = this.predicate.and(predicate);
		}
		return this;
	}

	/**
	 * Add check simple.
	 *
	 * @param check the check
	 *
	 * @return the simple
	 */
	public Simple addCheck(Check<Inherited> check){
		addPredicate(i -> {
			try{
				check.with(i);
				return true;
			}
			catch(Exception e){
				e.printStackTrace();
				return false;
			}
		});
		return this;
	}

	/**
	 * Get structure structure.
	 *
	 * @return the structure
	 */
	public Structure getStructure(){
		return structure;
	}

}
