package cz.martinbayer.logparser.fileshandler;

public class LogFileSemaphoreWatchedStore {

	private StringBuffer storedBuffer;
	private boolean stopped = false;;
	private int signalsCount = 0;
	private int maxSignals = 0;
	private boolean stoppedImmediatelly;

	public LogFileSemaphoreWatchedStore(int maxSignals) {
		if (maxSignals > 0) {
			this.maxSignals = maxSignals;
		} else {
			this.maxSignals = 1;
		}
	}

	public synchronized void take() throws InterruptedException {
		/* wait until receiver will take more data */
		while (this.signalsCount == this.maxSignals) {
			wait();
		}
		this.signalsCount++;
		notify();
	}

	public synchronized void release() throws InterruptedException {
		while (this.signalsCount == 0) {
			wait();
		}
		this.signalsCount--;
		notify();
	}

	public void store(StringBuffer storedBuffer) {
		this.storedBuffer = storedBuffer;
	}

	public synchronized StringBuffer getStoredBuffer() {
		return this.storedBuffer;
	}

	public boolean isStopped() {
		synchronized (this) {
			return stopped && signalsCount == 0;
		}
	}

	public void reset() {
		synchronized (this) {
			this.signalsCount = 0;
			this.stopped = false;
			this.storedBuffer.delete(0, this.storedBuffer.length());
		}
	}

	public void stop() {
		synchronized (this) {
			this.stopped = true;
		}
	}

	public void stopImmediatelly() {
		synchronized (this) {
			this.stoppedImmediatelly = true;
		}
	}

	public final boolean isStoppedImmediatelly() {
		return stoppedImmediatelly;
	}
}
