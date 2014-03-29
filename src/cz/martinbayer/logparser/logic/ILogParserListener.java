package cz.martinbayer.logparser.logic;

/**
 * Implementing class is used to react for notification of the object handling
 * the log file parsing files
 * 
 * @author Martin
 * 
 */
public interface ILogParserListener {

	void parsed(ILogParserEvent event);
}
