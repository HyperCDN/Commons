package de.hypercdn.commons.imp.executionaction;

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
