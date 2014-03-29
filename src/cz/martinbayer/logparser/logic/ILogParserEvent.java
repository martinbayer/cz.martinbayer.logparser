package cz.martinbayer.logparser.logic;

import java.util.EventObject;

/**
 * Used to hold the information about log event
 * 
 * @author Martin
 * 
 */
public class ILogParserEvent extends EventObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5683623798593347346L;
	private LogParserPhase phase;
	private String groupName;
	private String groupValue;

	public ILogParserEvent(Object source, LogParserPhase phase,
			String groupName, String groupValue) {
		super(source);
		this.phase = phase;
		this.groupName = groupName;
		this.groupValue = groupValue;
	}

	public final LogParserPhase getPhase() {
		return phase;
	}

	public final String getGroupName() {
		return groupName;
	}

	public final String getGroupValue() {
		return groupValue;
	}
}
