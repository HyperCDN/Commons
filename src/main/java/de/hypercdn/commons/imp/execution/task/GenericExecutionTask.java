package de.hypercdn.commons.imp.execution.task;

import de.hypercdn.commons.api.execution.task.ExecutionStage;
import de.hypercdn.commons.api.execution.task.ExecutionTask;
import de.hypercdn.commons.imp.execution.action.RunnableExecutionAction;

import java.util.List;

public abstract class GenericExecutionTask extends ExecutableBase implements ExecutionTask{

	@Override
	public abstract List<ExecutionStage<Object, Object>> getStages();

	@Override
	public RunnableExecutionAction asExecutionAction(){
		return new RunnableExecutionAction(() -> {
			setState(new ExecutionState(ExecutionState.Reference.STARTED, null));
			logger.trace(this.toString());
			Object holder = null;
			boolean skip = false;
			Exception exception = null;
			for(var stage : getStages()){
				try{
					if(skip){
						stage.setState(new ExecutionState(ExecutionState.Reference.SKIPPED, exception != null ? "Skipped due to previous failure" : "Skipped"));
						continue;
					}
					Object finalHolder = holder;
					holder = stage.asExecutionActionWith(() -> finalHolder).execute();
					if(holder == null){
						skip = true;
					}
				}
				catch(Exception e){
					skip = true;
					exception = e;
					setState(new ExecutionState(ExecutionState.Reference.FAILED, e));
				}
			}
			if(exception == null){
				setState(new ExecutionState(ExecutionState.Reference.COMPLETED, null));
			}
			logger.trace(this.toString());
		});
	}

	@Override
	public String toString(){
		var builder = new StringBuilder();
		builder.append(
			String.format("Task \"%s\" | %s | at %s for %s | %s",
				getName(),
				getState().reference(),
				getLastExecutionTime(),
				getLastExecutionDuration(),
				getDescription()
			)
		);
		for(var stage : getStages()){
			builder.append("\n\t").append(stage.toString());
		}
		return builder.toString();
	}

}
