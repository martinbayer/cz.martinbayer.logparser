package cz.martinbayer.logparser.fileshandler;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

public class LogFileReader implements Runnable {

	private static final int BUFFER_SIZE = 8192;
	private LogFileSemaphoreWatchedStore semaphore;
	private File[] filesToRead;
	private StringBuffer sb;
	private String encoding = "UTF-8";

	public LogFileReader(LogFileSemaphoreWatchedStore semaphore,
			File[] filesToRead, String encoding) {
		this.semaphore = semaphore;
		sb = new StringBuffer();
		this.semaphore.store(sb);
		this.filesToRead = filesToRead;
		this.encoding = encoding;
	}

	@Override
	public void run() {
		reset();
		for (File logFile : this.filesToRead) {
			System.out.println("File analysis started:" + logFile.getName());
			try (FileChannel fileChannel = new RandomAccessFile(logFile, "r")
					.getChannel()) {

				ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
				while (fileChannel.read(buffer) != -1) {
					buffer.flip();
					CharBuffer s = Charset.forName(encoding).decode(buffer);
					sb.append(s);
					// semaphore receiver is noticed that new data were read to
					// stringbuffer
					this.semaphore.take();
					buffer.clear();
				}
			} catch (IOException | InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		this.semaphore.stop();
	}

	private void reset() {
		this.sb.delete(0, this.sb.length());
	}
}
