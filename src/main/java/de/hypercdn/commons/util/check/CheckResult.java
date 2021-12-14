package de.hypercdn.commons.util.check;

import java.util.Objects;

/**
 * The type Check result.
 */
public class CheckResult{

	private final boolean ok;
	private final String message;

	/**
	 * Instantiates a new Check result.
	 */
	protected CheckResult(){
		this.ok = true;
		this.message = null;
	}

	/**
	 * Instantiates a new Check result.
	 *
	 * @param message the message
	 */
	protected CheckResult(String message){
		Objects.requireNonNull(message);
		this.ok = message.isBlank();
		this.message = message;
	}

	/**
	 * Is ok boolean.
	 *
	 * @return the boolean
	 */
	public boolean isOk(){
		return ok;
	}

	/**
	 * Get message string.
	 *
	 * @return the string
	 */
	public String getMessage(){
		return message;
	}

}
