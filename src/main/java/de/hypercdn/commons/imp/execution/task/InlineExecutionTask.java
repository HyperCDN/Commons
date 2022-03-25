package de.hypercdn.commons.imp.execution.task;

import de.hypercdn.commons.api.execution.task.ExecutionStage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InlineExecutionTask extends GenericExecutionTask{

	private final String name;
	private final String description;
	private final List<ExecutionStage<Object, Object>> stages;

	/**
	 * Creates a new execution task
	 *
	 * @param name        name
	 * @param description description
	 * @param stages      as vararg
	 */
	public InlineExecutionTask(String name, String description, ExecutionStage<?, ?>... stages){
		this(name, description, new ArrayList<>(){{
			Arrays.stream(stages).forEach(stage -> add((ExecutionStage<Object, Object>) stage));
		}});
	}

	/**
	 * Creates a new execution task
	 *
	 * @param name        name
	 * @param description description
	 * @param stages      as list
	 */
	public InlineExecutionTask(String name, String description, List<ExecutionStage<Object, Object>> stages){
		this.name = name;
		this.description = description;
		this.stages = stages;
	}

	@Override
	public ExecutionState getExecutionState(){
		return super.getExecutionState();
	}

	@Override
	public String getName(){
		return name;
	}

	@Override
	public String getDescription(){
		return description;
	}

	@Override
	public List<ExecutionStage<Object, Object>> getStages(){
		return new ArrayList<>(stages);
	}

}
