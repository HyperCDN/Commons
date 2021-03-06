package de.hypercdn.commons.api.structure;

import de.hypercdn.commons.imp.statuslog.StatusLog;
import de.hypercdn.commons.imp.structure.Complex;
import de.hypercdn.commons.imp.structure.Field;
import de.hypercdn.commons.imp.structure.Simple;
import de.hypercdn.commons.imp.tuples.Pair;
import de.hypercdn.commons.imp.wrap.Inherited;
import org.slf4j.event.Level;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Represents a data structure
 */
public interface Structure{

	/**
	 * Tests the supplied data for the provided data structure
	 *
	 * @param structure data structure
	 * @param object    to test
	 *
	 * @return status log containing the test results
	 */
	static StatusLog test(Structure structure, Object object){
		Objects.requireNonNull(structure);
		Objects.requireNonNull(object);
		// wrap if needed
		var sub = object instanceof Inherited ? (Inherited) object : new Inherited(object);

		if(structure instanceof Complex complex && sub.getObject() instanceof Map<?, ?>){
			return testMap(complex, sub);
		}
		else if(structure instanceof Simple simple && sub.getObject().getClass().isArray()){
			return testArray(simple, sub);
		}
		else if(structure instanceof Simple simple && sub.getObject() instanceof List<?> list){
			return testArray(simple, new Inherited(sub.getParent(), list.toArray()));
		}
		else if(structure instanceof Simple simple){
			if(simple.getStructure() != null && simple.structureIsArray()){
				return testArray(simple, sub);
			}
			return testObject(simple, sub, false);
		}
		return new StatusLog("Failed to test unexpected entity type", Level.ERROR);
	}

	/**
	 * Tests the supplied inherited data containing a mao for the provided specification for complex objects
	 *
	 * @param complex   specification
	 * @param Inherited data
	 *
	 * @return status log containing the test results
	 */
	static StatusLog testMap(Complex complex, Inherited Inherited){
		Objects.requireNonNull(complex);
		Objects.requireNonNull(Inherited);
		Map<?, ?> map = Inherited.getObject();
		var fields = complex.getFields();
		var tmpResults = new ArrayList<StatusLog>();
		if(!fields.isEmpty() && fields.get(0).getIdentifier() == null){
			for(var object : map.values()){
				tmpResults.add(testField(fields.get(0), new Inherited(Inherited, object)));
			}
			return new StatusLog().addChildren(tmpResults);
		}
		else{
			for(var field : fields){
				tmpResults.add(testField(field, new Inherited(Inherited, map.get(field.getIdentifier()))));
			}
			var unexpectedKeys = new HashSet<>(map.keySet());
			unexpectedKeys.removeAll(fields.stream().map(Field::getIdentifier).collect(Collectors.toCollection(HashSet::new)));
			for(var key : unexpectedKeys){
				tmpResults.add(new StatusLog("Field \"" + key + "\" unexpected", Level.WARN));
			}
			return new StatusLog().addChildren(tmpResults);
		}
	}

	/**
	 * Tests the supplied inherited data containing an array for the provided specification for simple objects
	 *
	 * @param simple    specification
	 * @param Inherited data
	 *
	 * @return status log containing the test results
	 */
	static StatusLog testArray(Simple simple, Inherited Inherited){
		Objects.requireNonNull(simple);
		Objects.requireNonNull(Inherited);
		var arrayCandidate = Inherited.getObject();
		if(!arrayCandidate.getClass().isArray()){
			return new StatusLog("Object \"" + arrayCandidate + "\" does not match expectations", Level.ERROR);
		}
		var array = (Object[]) arrayCandidate;
		if((simple.getTClass() != null && simple.getTClass().isArray() != array.getClass().isArray()) || (simple.getStructure() != null && !simple.structureIsArray())){
			return new StatusLog("Object \"" + Arrays.toString(array) + "\" does not match expectations", Level.ERROR);
		}
		var resultTmp = new ArrayList<StatusLog>();
		for(var object : array){
			resultTmp.add(testObject(simple, new Inherited(Inherited, object), true));
		}
		return new StatusLog().addChildren(resultTmp);
	}

	/**
	 * Tests the supplied inherited data for the provided specification for simple objects
	 *
	 * @param simple    specification
	 * @param Inherited data
	 * @param fromArray if the supplied interhited data is inside an array
	 *
	 * @return status log containing the test results
	 */
	static StatusLog testObject(Simple simple, Inherited Inherited, boolean fromArray){
		Objects.requireNonNull(simple);
		Objects.requireNonNull(Inherited);
		var object = Inherited.getObject();
		if(simple.getStructure() != null){
			return test(simple.getStructure(), new Inherited(Inherited, object));
		}
		if((simple.getTClass() == null || (simple.getTClass().isArray() && fromArray ? simple.getTClass().componentType().isInstance(object) : simple.getTClass().isInstance(object))) && (simple.getPredicate() == null || simple.getPredicate().test(Inherited))){
			return new StatusLog("Object \"" + object + "\" does match expectations", Level.INFO);
		}
		return new StatusLog("Object \"" + object + "\" does not match expectations", Level.ERROR);
	}

	/**
	 * Tests the supplied inherited data for the provided field specification
	 *
	 * @param field     specification
	 * @param Inherited data
	 *
	 * @return status log containing the test results
	 */
	static StatusLog testField(Field field, Inherited Inherited){
		Objects.requireNonNull(field);
		Objects.requireNonNull(Inherited);
		var object = Inherited.getObject();
		if(object == null && !field.isOptional()){
			return new StatusLog("Field \"" + field.getIdentifier() + "\" does not exist", Level.ERROR);
		}
		if(object == null && field.isOptional()){
			return new StatusLog("Field \"" + field.getIdentifier() + "\" does not exist but is optional", Level.INFO);
		}
		var resultTmp = new ArrayList<Pair<StatusLog, Structure>>();
		boolean override = false;
		for(var struct : field.getOptions()){
			var result = test(struct, Inherited);
			if(!result.hasLevel(Level.ERROR)){
				override = true;
			}
			resultTmp.add(new Pair<>(result, struct));
		}
		if(!resultTmp.isEmpty() && (field.getIdentifier() == null || field.getIdentifier().toString().isBlank())){
			var result = resultTmp.get(0).a();
			if(override){
				result.setLevel(Level.INFO);
			}
			return result;
		}
		var result = new StatusLog("Field \"" + field.getIdentifier() + "\"")
			.addChildren(resultTmp.stream().map(Pair::a).toList());
		if(override){
			result.setLevel(Level.INFO);
		}
		return result;
	}

}
