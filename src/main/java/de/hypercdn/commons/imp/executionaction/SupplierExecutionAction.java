package de.hypercdn.commons.imp.executionaction;

import java.util.function.Supplier;

public class SupplierExecutionAction<OUT> extends GenericExecutionAction<Void, OUT>{

    public SupplierExecutionAction(){
        super();
    }

    public SupplierExecutionAction(Supplier<OUT> supplierAction){
        super(() -> null, (unused) -> supplierAction.get());
    }

}
