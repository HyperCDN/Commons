package de.hypercdn.commons.imp.executionaction;

import java.util.function.Supplier;

/**
 * The type Supplier execution action.
 *
 * @param <OUT> the type parameter
 */
public class SupplierExecutionAction<OUT> extends GenericExecutionAction<Void, OUT>{

	/**
	 * Instantiates a new Supplier execution action.
	 */
	public SupplierExecutionAction(){
		super();
	}

	/**
	 * Instantiates a new Supplier execution action.
	 *
	 * @param supplierAction the supplier action
	 */
	public SupplierExecutionAction(Supplier<OUT> supplierAction){
		super(() -> null, (unused) -> supplierAction.get());
	}

}
