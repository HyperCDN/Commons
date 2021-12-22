package de.hypercdn.commons.imp.structure;

import de.hypercdn.commons.api.structure.Structure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The type Complex.
 */
public class Complex implements Structure{

	private final Map<Object, Field> fields = new HashMap<>();

	/**
	 * Instantiates a new Complex.
	 *
	 * @param fields the fields
	 */
	public Complex(Field... fields){
		for(var field : fields){
			this.fields.put(field.getIdentifier(), field);
		}
	}

	/**
	 * Get field field.
	 *
	 * @param identifier the identifier
	 *
	 * @return the field
	 */
	public Field getField(Object identifier){
		return fields.get(identifier);
	}

	/**
	 * Get fields list.
	 *
	 * @return the list
	 */
	public List<Field> getFields(){
		return new ArrayList<>(this.fields.values());
	}

}
