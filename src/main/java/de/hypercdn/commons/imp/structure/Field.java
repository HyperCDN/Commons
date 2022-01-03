package de.hypercdn.commons.imp.structure;

import de.hypercdn.commons.api.structure.Structure;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

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

	public Field setOptional(boolean value, Object defaultValue){
		return setOptional(value, () -> defaultValue);
	}

	public Field setOptional(boolean value, Supplier<Object> defaultSupplier){
		this.isOptional = value;
		this.defaultSupplier = defaultSupplier;
		return this;
	}

	public Object getIdentifier(){
		return identifier;
	}

	public boolean isOptional(){
		return isOptional;
	}

	public Field setOptional(boolean value){
		return setOptional(value, defaultSupplier);
	}

	public List<Structure> getOptions(){
		return options;
	}

	public boolean hasDefault(){
		return defaultSupplier != null;
	}

	public Object getDefault(){
		if(hasDefault()){
			return defaultSupplier.get();
		}
		return null;
	}

}
