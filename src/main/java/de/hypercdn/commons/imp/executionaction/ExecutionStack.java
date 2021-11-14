package de.hypercdn.commons.imp.executionaction;

import de.hypercdn.commons.util.StackTraceUtil;

public class ExecutionStack{

	private volatile StackTraceElement[] previousStack;
	private volatile StackTraceElement[] currentStack;

	public ExecutionStack setPreviousStack(StackTraceElement[] stackTraceElements){
		this.previousStack = stackTraceElements;
		return this;
	}

	public ExecutionStack setCurrentStack(StackTraceElement[] stackTraceElements){
		this.currentStack = stackTraceElements;
		return this;
	}

	public void push(ExecutionStack previous){
		this.previousStack = previous.getFullContextStack();
	}

	public StackTraceElement[] getFullContextStack(){
		return getFullContextStack(null);
	}

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
