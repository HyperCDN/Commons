package de.hypercdn.commons.imp.structure;

import de.hypercdn.commons.api.structure.Structure;
import de.hypercdn.commons.imp.wrap.Inherited;
import de.hypercdn.commons.util.check.Check;

import java.util.function.Predicate;

public class Simple{

	private Class<?> tClass;
	private Predicate<Inherited> predicate;
	private Structure structure;

	public Simple(Class<?> tClass){
		this(tClass, null);
	}

	public Simple(Class<?> tClass, Predicate<Inherited> predicate){
		this.tClass = tClass;
		this.predicate = predicate;
	}

	public Simple(Structure structure){
		this.structure = structure;
	}

	public Class<?> getTClass(){
		return tClass;
	}

	public Predicate<Inherited> getPredicate(){
		return predicate;
	}

	public Simple addPredicate(Predicate<Inherited> predicate){
		if(this.predicate == null){
			this.predicate = predicate;
		}
		else{
			this.predicate = this.predicate.and(predicate);
		}
		return this;
	}

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

	public Structure getStructure(){
		return structure;
	}

}
