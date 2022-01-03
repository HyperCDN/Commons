package de.hypercdn.commons.imp.statuslog;

import org.slf4j.event.Level;

import java.util.ArrayList;
import java.util.List;

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

	public String getMessage(){
		return getMessage(0);
	}

	public String getMessage(int shift){
		return "\t".repeat(shift) + message;
	}

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

	public Level getLevel(){
		return level;
	}

	public StatusLog setLevel(Level level){
		this.level = level;
		return this;
	}

	public boolean hasLevel(Level level){
		return getLevel().toInt() >= level.toInt();
	}

	public StatusLog addChild(StatusLog statusLog){
		if(hasLevel(statusLog.getLevel())){
			setLevel(statusLog.getLevel());
		}
		children.add(statusLog);
		return this;
	}

	public StatusLog addChildren(List<StatusLog> statusLogList){
		for(var entry : statusLogList){
			addChild(entry);
		}
		return this;
	}

}
