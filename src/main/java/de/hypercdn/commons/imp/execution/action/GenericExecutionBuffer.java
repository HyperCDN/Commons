package de.hypercdn.commons.imp.execution.action;

import de.hypercdn.commons.api.execution.action.ExecutionBuffer;

import java.time.Instant;

public class GenericExecutionBuffer<IN, OUT> implements ExecutionBuffer.Internal<IN, OUT>{

	private Instant timestamp;
	private volatile IN input;
	private volatile OUT output;


	@Override
	public Instant getTimestamp(){
		return timestamp;
	}

	@Override
	public IN getInput(){
		return input;
	}

	@Override
	public OUT getOutput(){
		return output;
	}

	@Override
	public void setData(IN input, OUT output){
		this.input = input;
		this.output = output;
		this.timestamp = Instant.now();
	}

	@Override
	public void reset(){
		this.input = null;
		this.output = null;
		this.timestamp = null;
	}

}
