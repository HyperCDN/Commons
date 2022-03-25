package de.hypercdn.commons.imp.execution.task;

import de.hypercdn.commons.api.execution.task.Executable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;

public abstract class ExecutableBase implements Executable{

	protected Logger logger = LoggerFactory.getLogger(getClass());
	private ExecutionState executionState = ExecutionState.UNKNOWN;
	private Instant lastExecutionTime = Instant.MIN;
	private Instant lastExecutionEndTime = Instant.MIN;

	@Override
	public abstract String getDescription();

	@Override
	public ExecutionState getExecutionState(){
		return executionState;
	}

	@Override
	public synchronized void setExecutionState(ExecutionState executionState){
		this.executionState = executionState;
		switch(executionState.reference()){
			case STARTED -> setLastExecutionTime(Instant.now());
			case FAILED, COMPLETED -> setLastExecutionEndTime(Instant.now());
		}
	}

	@Override
	public Instant getLastExecutionTime(){
		return lastExecutionTime;
	}

	@Override
	public synchronized void setLastExecutionTime(Instant lastExecutionTime){
		this.lastExecutionTime = lastExecutionTime;
	}

	@Override
	public Instant getLastExecutionEndTime(){
		return lastExecutionEndTime;
	}

	@Override
	public synchronized void setLastExecutionEndTime(Instant time){
		this.lastExecutionEndTime = time;
	}

}
