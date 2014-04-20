package cz.martinbayer.logparser.fileshandler;

import cz.martinbayer.logparser.logic.ILogParserListener;

public abstract class LogFileReceiver implements Runnable {

	private LogFileSemaphoreWatchedStore semaphore;
	private StringBuffer storedBuffer;

	protected ILogParserListener listener;

	public void setSemaphore(LogFileSemaphoreWatchedStore semaphore) {
		this.semaphore = semaphore;
	}

	@Override
	public void run() {
		try {
			this.storedBuffer = semaphore.getStoredBuffer();
			int actualBufferSize = 0;
			int unprocessedDataSize = 0;
			while (!semaphore.isStopped()) {
				semaphore.release();
				actualBufferSize = this.storedBuffer.length();
				unprocessedDataSize = handleStoredBuffer(storedBuffer,
						actualBufferSize);
				/*
				 * buffer can be filled with more data actually but work with
				 * the previous size only
				 */
				if (unprocessedDataSize >= 0) {
					this.storedBuffer.delete(0, unprocessedDataSize);
				}
			}
			releaseSources();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Listener must be set before the processing is started otherwise exception
	 * is thrown
	 * 
	 * @param listener
	 */
	public void setListener(ILogParserListener listener) {
		this.listener = listener;
	}

	protected abstract void releaseSources();

	public abstract int handleStoredBuffer(StringBuffer sb, int actualLength);
}
