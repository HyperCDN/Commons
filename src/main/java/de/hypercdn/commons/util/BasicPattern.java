package de.hypercdn.commons.util;

import java.util.regex.Pattern;

/**
 * The type Basic pattern.
 */
public class BasicPattern{

	/**
	 * The constant DOT_PATTERN.
	 */
	public static final Pattern DOT_PATTERN = Pattern.compile("\\.");

	/**
	 * The constant SINGLE_SPACE_PATTERN.
	 */
	public static final Pattern SINGLE_SPACE_PATTERN = Pattern.compile("\\s");

	/**
	 * The constant MULTI_SPACE_PATTERN.
	 */
	public static final Pattern MULTI_SPACE_PATTERN = Pattern.compile("\\s+");

}
