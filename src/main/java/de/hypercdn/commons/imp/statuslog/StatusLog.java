package de.hypercdn.commons.imp.statuslog;

import org.slf4j.event.Level;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Status log.
 */
public class StatusLog{

	private final String message;
	private final List<StatusLog> children = new ArrayList<>();
	private Level level;

	/**
	 * Instantiates a new Status log.
	 */
	public StatusLog(){
		this("", Level.INFO);
	}

	/**
	 * Instantiates a new Status log.
	 *
	 * @param message the message
	 */
	public StatusLog(String message){
		this(message, Level.INFO);
	}

	/**
	 * Instantiates a new Status log.
	 *
	 * @param message the message
	 * @param level   the level
	 */
	public StatusLog(String message, Level level){
		this.message = message;
		this.level = level;
	}

	/**
	 * Get message string.
	 *
	 * @return the string
	 */
	public String getMessage(){
		return getMessage(0);
	}

	/**
	 * Get message string.
	 *
	 * @param shift the shift
	 *
	 * @return the string
	 */
	public String getMessage(int shift){
		return "\t".repeat(shift) + message;
	}

	/**
	 * Get full message string.
	 *
	 * @param level the level
	 *
	 * @return the string
	 */
	public String getFullMessage(Level level){
		return getFullMessage(level, 0);
	}

	private String getFullMessage(Level level, int layer){
		var builder = new StringBuilder();
		if(hasLevel(level)){
			if(message.isBlank()){
				for(var other : children){
					builder.append(other.getFullMessage(level, layer + 1));
				}
			}
			else{
				builder.append(getMessage(layer)).append(" (").append(getLevel()).append(")").append("\n");
				for(var other : children){
					builder.append(other.getFullMessage(level, layer + 1));
				}
			}
		}
		return builder.toString();
	}

	/**
	 * Get level level.
	 *
	 * @return the level
	 */
	public Level getLevel(){
		return level;
	}

	/**
	 * Set level status log.
	 *
	 * @param level the level
	 *
	 * @return the status log
	 */
	public StatusLog setLevel(Level level){
		this.level = level;
		return this;
	}

	/**
	 * Has level boolean.
	 *
	 * @param level the level
	 *
	 * @return the boolean
	 */
	public boolean hasLevel(Level level){
		return getLevel().toInt() >= level.toInt();
	}

	/**
	 * Add child status log.
	 *
	 * @param statusLog the status log
	 *
	 * @return the status log
	 */
	public StatusLog addChild(StatusLog statusLog){
		if(hasLevel(statusLog.getLevel())){
			setLevel(statusLog.getLevel());
		}
		children.add(statusLog);
		return this;
	}

	/**
	 * Add children status log.
	 *
	 * @param statusLogList the status log list
	 *
	 * @return the status log
	 */
	public StatusLog addChildren(List<StatusLog> statusLogList){
		for(var entry : statusLogList){
			addChild(entry);
		}
		return this;
	}

}
