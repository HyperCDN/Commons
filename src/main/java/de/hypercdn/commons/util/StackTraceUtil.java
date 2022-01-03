package de.hypercdn.commons.util;

/**
 * Util class for working with stack traces
 */
public class StackTraceUtil{

	private StackTraceUtil(){}

	/**
	 * Returns a stacktrace at the current call position
	 *
	 * @return stacktrace
	 */
	public static StackTraceElement[] currentStacktrace(){
		StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
		StackTraceElement[] noTraceStackTrace = new StackTraceElement[stackTrace.length - 2];
		System.arraycopy(stackTrace, 2, noTraceStackTrace, 0, stackTrace.length - 2);
		return noTraceStackTrace;
	}

	/**
	 * Merges two stacktraces with an identical base
	 *
	 * @param bottom stack
	 * @param top    stack
	 *
	 * @return combined stacktrace
	 */
	public static StackTraceElement[] merge(StackTraceElement[] bottom, StackTraceElement[] top){
		int difPos = top.length;
		for(int bottomPos = 0; bottomPos < Math.min(bottom.length, top.length); bottomPos++){
			if(bottom[bottomPos].equals(top[bottomPos])){
				difPos = bottomPos;
				break;
			}
		}
		StackTraceElement[] newStacktrace = new StackTraceElement[bottom.length + difPos];
		System.arraycopy(top, 0, newStacktrace, 0, difPos);
		System.arraycopy(bottom, 0, newStacktrace, difPos, bottom.length);
		return newStacktrace;
	}

}
