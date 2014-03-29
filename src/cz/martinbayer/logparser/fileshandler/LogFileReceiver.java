package cz.martinbayer.logparser.fileshandler;

public abstract class LogFileReceiver implements Runnable {

	private LogFileSemaphoreWatchedStore semaphore;
	private StringBuffer storedBuffer;

	public LogFileReceiver(LogFileSemaphoreWatchedStore semaphore) {
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

	protected abstract void releaseSources();

	public abstract int handleStoredBuffer(StringBuffer sb, int actualLength);
}
