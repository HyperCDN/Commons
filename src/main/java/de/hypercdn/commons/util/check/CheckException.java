package de.hypercdn.commons.util.check;

/**
 * The type Check exception.
 */
public class CheckException extends RuntimeException{

	private final Object object;
	private final CheckResult checkResult;

	/**
	 * Instantiates a new Check exception.
	 *
	 * @param object      the object
	 * @param checkResult the check result
	 */
	public CheckException(Object object, CheckResult checkResult){
		super("Object "+object+" does not match the following requirements: "+checkResult.getMessage());
		this.object = object;
		this.checkResult = checkResult;
	}

	/**
	 * Get object object.
	 *
	 * @return the object
	 */
	public Object getObject(){
		return object;
	}

	/**
	 * Get check result check result.
	 *
	 * @return the check result
	 */
	public CheckResult getCheckResult(){
		return checkResult;
	}

}
