package de.hypercdn.commons.util.check;

import java.util.Objects;

/**
 * Represents the result of a check
 */
public class CheckResult{

	private final boolean ok;
	private final String message;

	protected CheckResult(){
		this.ok = true;
		this.message = null;
	}

	protected CheckResult(String message){
		Objects.requireNonNull(message);
		this.ok = message.isBlank();
		this.message = message;
	}

	public boolean isOk(){
		return ok;
	}

	public String getMessage(){
		return message;
	}

}
