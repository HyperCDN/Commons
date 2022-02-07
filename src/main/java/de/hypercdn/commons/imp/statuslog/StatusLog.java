package de.hypercdn.commons.imp.statuslog;

import org.slf4j.event.Level;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of a log with an attached status
 */
public class StatusLog{

	private final String message;
	private final List<StatusLog> children = new ArrayList<>();
	private Level level;

	public StatusLog(){
		this("", Level.INFO);
	}

	public StatusLog(String message){
		this(message, Level.INFO);
	}

	public StatusLog(String message, Level level){
		this.message = message;
		this.level = level;
	}

	/**
	 * Returns the message of the current object
	 *
	 * @return message
	 */
	public String getMessage(){
		return getMessage(0);
	}

	/**
	 * Returns the message of the current object indented by shifts
	 *
	 * @param shift amount
	 *
	 * @return message
	 */
	public String getMessage(int shift){
		return "\t".repeat(shift) + message;
	}

	/**
	 * Returns the full message stored within this object and its childs
	 *
	 * @param level log level
	 *
	 * @return message
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
	 * Returns the current level of the log
	 *
	 * @return level
	 */
	public Level getLevel(){
		return level;
	}

	/**
	 * Sets the current level of the log
	 *
	 * @param level to set
	 *
	 * @return current instance
	 */
	public StatusLog setLevel(Level level){
		this.level = level;
		return this;
	}

	/**
	 * Checks if the log has the provided level or above
	 *
	 * @param level to check
	 *
	 * @return boolean
	 */
	public boolean hasLevel(Level level){
		return getLevel().toInt() >= level.toInt();
	}

	/**
	 * Adds a child log to the current one
	 *
	 * @param statusLog to add
	 *
	 * @return current instance
	 */
	public StatusLog addChild(StatusLog statusLog){
		if(!hasLevel(statusLog.getLevel())){
			setLevel(statusLog.getLevel());
		}
		children.add(statusLog);
		return this;
	}

	/**
	 * Adds multiple children to the current log
	 *
	 * @param statusLogList to add
	 *
	 * @return current instance
	 */
	public StatusLog addChildren(List<StatusLog> statusLogList){
		for(var entry : statusLogList){
			addChild(entry);
		}
		return this;
	}

}
