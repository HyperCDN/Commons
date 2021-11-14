package de.hypercdn.commons.imp.executionaction;

import de.hypercdn.commons.util.StackTraceUtil;

/**
 * The type Execution stack.
 */
public class ExecutionStack{

	private volatile StackTraceElement[] previousStack;
	private volatile StackTraceElement[] currentStack;

	/**
	 * Set previous stack execution stack.
	 *
	 * @param stackTraceElements the stack trace elements
	 *
	 * @return the execution stack
	 */
	public ExecutionStack setPreviousStack(StackTraceElement[] stackTraceElements){
		this.previousStack = stackTraceElements;
		return this;
	}

	/**
	 * Set current stack execution stack.
	 *
	 * @param stackTraceElements the stack trace elements
	 *
	 * @return the execution stack
	 */
	public ExecutionStack setCurrentStack(StackTraceElement[] stackTraceElements){
		this.currentStack = stackTraceElements;
		return this;
	}

	/**
	 * Push.
	 *
	 * @param previous the previous
	 */
	public void push(ExecutionStack previous){
		this.previousStack = previous.getFullContextStack();
	}

	/**
	 * Get full context stack stack trace element [ ].
	 *
	 * @return the stack trace element [ ]
	 */
	public StackTraceElement[] getFullContextStack(){
		return getFullContextStack(null);
	}

	/**
	 * Get full context stack stack trace element [ ].
	 *
	 * @param extension the extension
	 *
	 * @return the stack trace element [ ]
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
			return null;
		}
		if(extension != null){
			return StackTraceUtil.merge(buffer, extension);
		}
		return buffer;
	}

}
