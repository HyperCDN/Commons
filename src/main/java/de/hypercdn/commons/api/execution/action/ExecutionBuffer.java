package de.hypercdn.commons.api.execution.action;

import java.time.Instant;

public interface ExecutionBuffer<IN, OUT>{

	Instant getTimestamp();

	IN getInput();

	OUT getOutput();

	interface Internal<IN, OUT> extends ExecutionBuffer<IN, OUT>{

		void setData(IN input, OUT output);

		void reset();

	}

}
