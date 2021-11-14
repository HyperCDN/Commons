package de.hypercdn.commons.imp.executionaction;

/**
 * The type Runnable execution action.
 */
public class RunnableExecutionAction extends GenericExecutionAction<Void, Void>{

	/**
	 * Instantiates a new Runnable execution action.
	 */
	public RunnableExecutionAction(){
		super();
	}

	/**
	 * Instantiates a new Runnable execution action.
	 *
	 * @param runnable the runnable
	 */
	public RunnableExecutionAction(Runnable runnable){
		super(() -> null, (unused) -> {
			runnable.run();
			return null;
		});
	}

}
