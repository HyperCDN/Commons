package de.hypercdn.commons.imp.execution.action.helper;

import de.hypercdn.commons.imp.execution.action.GenericExecutionAction;

/**
 * Implementation of an execution action executing runnables
 */
public class RunnableExecutionAction extends GenericExecutionAction<Void, Void>{

	public RunnableExecutionAction(){
		super();
	}

	public RunnableExecutionAction(Runnable runnable){
		super(() -> null, (unused) -> {
			runnable.run();
			return null;
		});
	}

}
