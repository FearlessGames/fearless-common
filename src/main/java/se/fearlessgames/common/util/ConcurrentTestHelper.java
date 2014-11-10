package se.fearlessgames.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;

public class ConcurrentTestHelper {
	private final Logger log = LoggerFactory.getLogger(getClass());
	private final int numberOfThreads;
	private final CountDownLatch start;
	private final CountDownLatch done;
	private final CountDownLatch goSignal;

	public ConcurrentTestHelper(int numberOfThreads) {
		this.numberOfThreads = numberOfThreads;
		start = new CountDownLatch(numberOfThreads);
		done = new CountDownLatch(numberOfThreads);
		goSignal = new CountDownLatch(1);
	}

	public void awaitGoSignal() {
		try {
			goSignal.await();
		} catch (InterruptedException e) {
			log.warn("Wait for go signal interrupted", e);
		}
	}

	public void reportReadyToStart() {
		start.countDown();
	}

	public void awaitReadyForStart() {
		try {
			start.await();
		} catch (InterruptedException e) {
			log.warn("Wait for ready for start interrupted", e);
		}
	}

	public void reportFinished() {
		done.countDown();
	}

	public void awaitFinish() {
		try {
			done.await();
		} catch (InterruptedException e) {
			log.warn("Wait for finish interrupted", e);
		}
	}

	public void giveGoSignal() {
		goSignal.countDown();
	}

	public int getNumberOfThreads() {
		return numberOfThreads;
	}
}
