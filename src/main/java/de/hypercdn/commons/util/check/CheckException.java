package de.hypercdn.commons.util.check;

/**
 * Represents an exception internally used to create check results
 */
public class CheckException extends RuntimeException{

	private final Object object;
	private final CheckResult checkResult;

	public CheckException(Object object, CheckResult checkResult){
		super("Object " + object + " does not match the following requirements: " + checkResult.getMessage());
		this.object = object;
		this.checkResult = checkResult;
	}

	public Object getObject(){
		return object;
	}

	public CheckResult getCheckResult(){
		return checkResult;
	}

}
