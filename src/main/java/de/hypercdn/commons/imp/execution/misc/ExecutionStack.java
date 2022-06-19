package de.hypercdn.commons.imp.execution.misc;

import de.hypercdn.commons.util.StackTraceUtil;

/**
 * Execution stack used to improve stacktraces on execution actions
 */
public class ExecutionStack{

	private volatile StackTraceElement[] previousStack;
	private volatile StackTraceElement[] currentStack;

	/**
	 * Sets the previous stack
	 *
	 * @param stackTraceElements previous stack
	 *
	 * @return current instance
	 */
	public ExecutionStack setPreviousStack(StackTraceElement[] stackTraceElements){
		this.previousStack = stackTraceElements;
		return this;
	}

	/**
	 * Sets the current stack
	 *
	 * @param stackTraceElements current stack
	 *
	 * @return current instance
	 */
	public ExecutionStack setCurrentStack(StackTraceElement[] stackTraceElements){
		this.currentStack = stackTraceElements;
		return this;
	}

	/**
	 * Pushes the provided execution stack into the previous stack
	 *
	 * @param previous execution stack
	 */
	public void push(ExecutionStack previous){
		this.previousStack = previous.getFullContextStack();
	}

	/**
	 * Returns the full context stack of the current context
	 *
	 * @return StackTrace
	 */
	public StackTraceElement[] getFullContextStack(){
		return getFullContextStack(null);
	}

	/**
	 * Returns the full context stack of the current context with an optioanl extension
	 *
	 * @param extension of optional appending
	 *
	 * @return StackTrace
	 */
	public StackTraceElement[] getFullContextStack(StackTraceElement[] extension){
		StackTraceElement[] buffer;
		if(previousStack != null && currentStack != null){
			buffer = StackTraceUtil.merge(previousStack, currentStack);
		}
		else if(currentStack != null){
			buffer = currentStack;
		}
		else{
			return extension;
		}
		if(extension != null){
			return StackTraceUtil.merge(buffer, extension);
		}
		return buffer;
	}

}
