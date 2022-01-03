package de.hypercdn.commons.imp.structure;

import de.hypercdn.commons.api.structure.Structure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Complex implements Structure{

	private final Map<Object, Field> fields = new HashMap<>();

	public Complex(Field... fields){
		for(var field : fields){
			this.fields.put(field.getIdentifier(), field);
		}
	}

	public Field getField(Object identifier){
		return fields.get(identifier);
	}

	public List<Field> getFields(){
		return new ArrayList<>(this.fields.values());
	}

}
