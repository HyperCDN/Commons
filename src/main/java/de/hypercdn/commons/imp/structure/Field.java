package de.hypercdn.commons.imp.structure;

import de.hypercdn.commons.api.structure.Structure;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * The type Field.
 */
public class Field{

	private final List<Structure> options = new ArrayList<>();
	private final Object identifier;
	private boolean isOptional;
	private Supplier<Object> defaultSupplier;

	/**
	 * Instantiates a new Field.
	 *
	 * @param options the options
	 */
	public Field(Structure... options){
		this(null, options);
	}

	/**
	 * Instantiates a new Field.
	 *
	 * @param identifier the identifier
	 * @param options    the options
	 */
	public Field(Object identifier, Structure... options){
		this.identifier = identifier;
		this.options.addAll(List.of(options));
	}

	/**
	 * Set optional field.
	 *
	 * @param value        the value
	 * @param defaultValue the default value
	 *
	 * @return the field
	 */
	public Field setOptional(boolean value, Object defaultValue){
		return setOptional(value, () -> defaultValue);
	}

	/**
	 * Set optional field.
	 *
	 * @param value           the value
	 * @param defaultSupplier the default supplier
	 *
	 * @return the field
	 */
	public Field setOptional(boolean value, Supplier<Object> defaultSupplier){
		this.isOptional = value;
		this.defaultSupplier = defaultSupplier;
		return this;
	}

	/**
	 * Get identifier object.
	 *
	 * @return the object
	 */
	public Object getIdentifier(){
		return identifier;
	}

	/**
	 * Is optional boolean.
	 *
	 * @return the boolean
	 */
	public boolean isOptional(){
		return isOptional;
	}

	/**
	 * Set optional field.
	 *
	 * @param value the value
	 *
	 * @return the field
	 */
	public Field setOptional(boolean value){
		return setOptional(value, defaultSupplier);
	}

	/**
	 * Get options list.
	 *
	 * @return the list
	 */
	public List<Structure> getOptions(){
		return options;
	}

	/**
	 * Has default boolean.
	 *
	 * @return the boolean
	 */
	public boolean hasDefault(){
		return defaultSupplier != null;
	}

	/**
	 * Get default object.
	 *
	 * @return the object
	 */
	public Object getDefault(){
		if(hasDefault()){
			return defaultSupplier.get();
		}
		return null;
	}

}
