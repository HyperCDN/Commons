package de.hypercdn.commons.imp.wrap;

/**
 * The type Inherited.
 */
public class Inherited{


	private final Inherited parent;
	private final Object object;

	/**
	 * Instantiates a new Inherited.
	 *
	 * @param object the object
	 */
	public Inherited(Object object){
		this(null, object);
	}

	/**
	 * Instantiates a new Inherited.
	 *
	 * @param parent the parent
	 * @param object the object
	 */
	public Inherited(Inherited parent, Object object){
		this.parent = parent;
		this.object = object;
	}

	/**
	 * Get base inherited.
	 *
	 * @return the inherited
	 */
	public Inherited getBase(){
		var current = this;
		while(current.getParent() != null){
			current = current.getParent();
		}
		return current;
	}

	/**
	 * Get parent inherited.
	 *
	 * @return the inherited
	 */
	public Inherited getParent(){
		return parent;
	}

	/**
	 * Get object u.
	 *
	 * @param <U> the type parameter
	 *
	 * @return the u
	 */
	public <U> U getObject(){
		return (U) object;
	}

}
