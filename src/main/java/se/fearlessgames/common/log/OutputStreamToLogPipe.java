package se.fearlessgames.common.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class OutputStreamToLogPipe extends OutputStream {
	private static final char DELIMITER = '\n';

	private final Logger logger;
	private final ByteArrayOutputStream contentStream;

	public OutputStreamToLogPipe(String name) {
		logger = LoggerFactory.getLogger(name);
		logger.info("Created new OutputStreamToLogPipe()");
		contentStream = new ByteArrayOutputStream();
	}

	@Override
	public void write(int b) throws IOException {
		if (b == DELIMITER) {
			flush();
		} else {
			contentStream.write(b);
		}
	}

	@Override
	public void flush() throws IOException {
		String content = new String(contentStream.toByteArray());
		if (content.endsWith("\r")) {
			content = content.substring(0, content.length() - 1);
		}
		logger.info(content);
		contentStream.reset();
	}
}
