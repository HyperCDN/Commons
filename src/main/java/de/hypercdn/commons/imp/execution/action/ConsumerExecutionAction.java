package de.hypercdn.commons.imp.execution.action;

import java.util.function.Consumer;

/**
 * Implementation of an execution action executing consumers
 */
public class ConsumerExecutionAction<IN> extends GenericExecutionAction<IN, Void>{

	public ConsumerExecutionAction(){
		super();
	}

	public ConsumerExecutionAction(Consumer<IN> supplierAction){
		super(() -> null, input -> {
			supplierAction.accept(input);
			return null;
		});
	}

}
