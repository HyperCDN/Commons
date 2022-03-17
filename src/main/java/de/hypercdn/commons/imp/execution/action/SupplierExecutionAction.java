package de.hypercdn.commons.imp.execution.action;

import java.util.function.Supplier;

/**
 * Implementation of an execution action supplying data
 *
 * @param <OUT> type
 */
public class SupplierExecutionAction<OUT> extends GenericExecutionAction<Void, OUT>{

	public SupplierExecutionAction(){
		super();
	}

	public SupplierExecutionAction(Supplier<OUT> supplierAction){
		super(() -> null, (unused) -> supplierAction.get());
	}

}
