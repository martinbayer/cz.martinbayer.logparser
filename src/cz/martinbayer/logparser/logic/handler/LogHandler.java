package cz.martinbayer.logparser.logic.handler;

import java.io.File;

import cz.martinbayer.logparser.fileshandler.LogFileReader;
import cz.martinbayer.logparser.fileshandler.LogFileReceiver;
import cz.martinbayer.logparser.fileshandler.LogFileSemaphoreWatchedStore;
import cz.martinbayer.logparser.logic.ILogParserListener;

/**
 * 
 * @author Martin
 * 
 */
public class LogHandler {

	private static LogHandler instance;
	private File[] filesToParse;
	private String encoding = "UTF-8";
	private LogFileReader logFileReader;
	private LogFileReceiver receiver;
	private LogFileSemaphoreWatchedStore semaphore;

	private LogHandler(File[] filesToParse, String encoding,
			LogFileReceiver fileReceiver) {
		this.filesToParse = filesToParse;
		if (encoding != null) {
			this.encoding = encoding;
		}
		semaphore = new LogFileSemaphoreWatchedStore(5);
		logFileReader = new LogFileReader(semaphore, this.filesToParse,
				this.encoding);
		this.receiver = fileReceiver;
		this.receiver.setSemaphore(semaphore);
	}

	public static synchronized LogHandler getInstance(File[] filesToParse,
			String encoding, LogFileReceiver receiver) {
		if (instance == null) {
			instance = new LogHandler(filesToParse, encoding, receiver);
		}
		return instance;
	}

	/**
	 * Only one listener can be used at once due to performance reason. Result
	 * can be used for other listeners in upper layer if needed.
	 * 
	 * @param listener
	 */
	public synchronized void doParse(ILogParserListener listener) {
		semaphore.reset();
		receiver.setListener(listener);
		Thread fileReadThread = new Thread(logFileReader);
		Thread logbackReceiverThread = new Thread(receiver);
		fileReadThread.start();
		logbackReceiverThread.start();

		try {
			logbackReceiverThread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
