package cz.martinbayer.logparser.logic;

import java.util.regex.Matcher;

public enum LogParserPhase {
	/**
	 * Marks the start of the one found record (returned by
	 * {@link Matcher#find()})
	 */
	START,
	/**
	 * Marks that one property of found record was reached (returned by
	 * {@link Matcher#group()})
	 */
	PROPERTY,
	/**
	 * Marks that there are no other groups in the record
	 */
	FINISH
}