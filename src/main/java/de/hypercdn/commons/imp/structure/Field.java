package de.hypercdn.commons.imp.structure;

import de.hypercdn.commons.api.structure.Structure;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * Represents a field of a complex data structure
 */
public class Field{

	private final List<Structure> options = new ArrayList<>();
	private final Object identifier;
	private boolean isOptional;
	private Supplier<Object> defaultSupplier;

	public Field(Structure... options){
		this(null, options);
	}

	public Field(Object identifier, Structure... options){
		this.identifier = identifier;
		this.options.addAll(List.of(options));
	}

	/**
	 * Set the field to be optional
	 *
	 * @param value        to set
	 * @param defaultValue default value
	 *
	 * @return current instance
	 */
	public Field setOptional(boolean value, Object defaultValue){
		return setOptional(value, () -> defaultValue);
	}

	/**
	 * Set the field to be optional
	 *
	 * @param value           to set
	 * @param defaultSupplier for default value
	 *
	 * @return current instance
	 */
	public Field setOptional(boolean value, Supplier<Object> defaultSupplier){
		this.isOptional = value;
		this.defaultSupplier = defaultSupplier;
		return this;
	}

	/**
	 * Return the identifier of the field
	 *
	 * @return identifier
	 */
	public Object getIdentifier(){
		return identifier;
	}

	/**
	 * Check if the field is optional
	 *
	 * @return is optional
	 */
	public boolean isOptional(){
		return isOptional;
	}

	/**
	 * Set the field to be optional
	 *
	 * @param value to set
	 *
	 * @return current instance
	 */
	public Field setOptional(boolean value){
		return setOptional(value, defaultSupplier);
	}

	/**
	 * Return all structure options available for this field
	 *
	 * @return structure options
	 */
	public List<Structure> getOptions(){
		return options;
	}

	/**
	 * Check whether this field has a default value
	 *
	 * @return has default value
	 */
	public boolean hasDefault(){
		return defaultSupplier != null;
	}

	/**
	 * Get the default value for this field
	 *
	 * @return default value
	 */
	public Object getDefault(){
		if(hasDefault()){
			return defaultSupplier.get();
		}
		throw new RuntimeException("No default supplier configured");
	}

}
