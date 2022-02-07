package de.hypercdn.commons.imp.structure;

import de.hypercdn.commons.api.structure.Structure;
import de.hypercdn.commons.imp.wrap.Inherited;
import de.hypercdn.commons.util.check.Check;

import java.util.function.Predicate;

/**
 * Represents a simple data structure (object or array)
 */
public class Simple implements Structure{

	private Class<?> tClass;
	private Predicate<Inherited> predicate;

	private Structure structure;
	private boolean structureIsArray;

	public Simple(Class<?> tClass){
		this(tClass, null);
	}

	public Simple(Class<?> tClass, Predicate<Inherited> predicate){
		this.tClass = tClass;
		this.predicate = predicate;
	}

	public Simple(Structure structure, boolean asArray){
		this.structure = structure;
		this.structureIsArray = asArray;
	}

	/**
	 * Returns the class of the data type
	 *
	 * @return type
	 */
	public Class<?> getTClass(){
		return tClass;
	}

	/**
	 * Returns the predicate used to test the values of this structure
	 *
	 * @return predicate
	 */
	public Predicate<Inherited> getPredicate(){
		return predicate;
	}

	/**
	 * Adds a predicate to the currently set one to test values of this structure
	 *
	 * @param predicate to add
	 *
	 * @return current instance
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
	 * Adds a check to the predicates used to test values of this structure
	 *
	 * @param check to add
	 *
	 * @return current instance
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
	 * Returns the sub structure of this type
	 *
	 * @return structure
	 */
	public Structure getStructure(){
		return structure;
	}

	/**
	 * Indicates that the sub structure should be read as array
	 *
	 * @return structure
	 */
	public boolean structureIsArray(){
		if(structure != null){
			return structureIsArray;
		}
		throw new RuntimeException("Does not contain a structure");
	}

}
