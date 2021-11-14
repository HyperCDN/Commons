package de.hypercdn.commons.util;

/**
 * The type Stack trace util.
 */
public class StackTraceUtil{

	private StackTraceUtil(){}

	/**
	 * Current stacktrace stack trace element [ ].
	 *
	 * @return the stack trace element [ ]
	 */
	public static StackTraceElement[] currentStacktrace(){
		StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
		StackTraceElement[] noTraceStackTrace = new StackTraceElement[stackTrace.length - 2];
		System.arraycopy(stackTrace, 2, noTraceStackTrace, 0, stackTrace.length - 2);
		return noTraceStackTrace;
	}

	/**
	 * Merge stack trace element [ ].
	 *
	 * @param bottom the bottom
	 * @param top    the top
	 *
	 * @return the stack trace element [ ]
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
