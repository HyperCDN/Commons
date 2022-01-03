package de.hypercdn.commons.imp.wrap;

public class Inherited{


	private final Inherited parent;
	private final Object object;

	public Inherited(Object object){
		this(null, object);
	}

	public Inherited(Inherited parent, Object object){
		this.parent = parent;
		this.object = object;
	}

	public Inherited getBase(){
		var current = this;
		while(current.getParent() != null){
			current = current.getParent();
		}
		return current;
	}

	public Inherited getParent(){
		return parent;
	}

	public <U> U getObject(){
		return (U) object;
	}

}
