package de.hypercdn.commons.imp.execution.task;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class InlineExecutionStage<IN, OUT> extends GenericExecutionStage<IN, OUT>{

	private final String name;
	private final String description;
	private final Function<IN, OUT> function;

	/**
	 * Creates a new execution stage
	 *
	 * @param name        name
	 * @param description description
	 * @param function    supplier
	 */
	public InlineExecutionStage(String name, String description, Supplier<OUT> function){
		this(name, description, in -> {
			return function.get();
		});
	}

	/**
	 * Creates a new execution stage
	 *
	 * @param name        name
	 * @param description description
	 * @param function    consumer
	 */
	public InlineExecutionStage(String name, String description, Consumer<IN> function){
		this(name, description, in -> {
			function.accept(in);
			return null;
		});
	}

	/**
	 * Creates a new execution stage
	 *
	 * @param name        name
	 * @param description description
	 * @param function    function
	 */
	public InlineExecutionStage(String name, String description, Function<IN, OUT> function){
		this.name = name;
		this.description = description;
		this.function = function;
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
	public Function<IN, OUT> getActionFunction(){
		return function;
	}

}
