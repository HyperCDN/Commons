package de.hypercdn.commons.util;

import java.util.regex.Pattern;

public class BasicPattern {

    public static final Pattern DOT_PATTERN = Pattern.compile("\\.");

    public static final Pattern SINGLE_SPACE_PATTERN = Pattern.compile("\\s");

    public static final Pattern MULTI_SPACE_PATTERN = Pattern.compile("\\s+");

}
