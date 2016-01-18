package com.example.multithread.processor;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Thread in which will be generated random message added to array
 * @author lubo08
 *
 */
public class RandomStringGenerator implements Runnable {

	private String[] messageDict = new String[] { "Hello here", "take care", "car wash", "nobody here" };

	private String[] messages;
	private AtomicInteger processedRange;
	private int rangePartition;

	public RandomStringGenerator(String[] messages, AtomicInteger processedRange, int rangePartition) {
		this.messages = messages;
		this.processedRange = processedRange;
		this.rangePartition = rangePartition;
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {

		int partitionCounter = processedRange.getAndIncrement() * rangePartition;
		// logger.info("processed partition {}",partitionCounter);
		for (int i = 0; i < rangePartition; i++) {
			messages[partitionCounter++] = messageDict[ThreadLocalRandom.current().nextInt(0, 4)];
		}

	}

}
