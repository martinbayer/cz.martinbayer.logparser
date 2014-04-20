package cz.martinbayer.logparser.logic.regex;

import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * Holds the information about used pattern and its groups Also split pattern is
 * stored - it is the information for splitting input files to more smaller
 * pieces to speed up the parsing
 * 
 * @author Martin
 * 
 */
public class ConfigPatternInfo {

	private ArrayList<String> usedGroups = new ArrayList<>();
	private Pattern parsingPattern;
	private Pattern splitPattern;

	public void addGroups(String[] strings) {
		for (int i = 0; strings != null && i < strings.length; i++) {
			if (strings[i] != null) {
				usedGroups.add(strings[i]);
			}
		}
	}

	public ArrayList<String> getGroups() {
		return usedGroups;
	}

	public final Pattern getParsingPattern() {
		return parsingPattern;
	}

	public final void setParsingPattern(Pattern parsingPattern) {
		this.parsingPattern = parsingPattern;
	}

	public final Pattern getSplitPattern() {
		return splitPattern;
	}

	public final void setSplitPattern(Pattern splitPattern) {
		this.splitPattern = splitPattern;
	}
}
