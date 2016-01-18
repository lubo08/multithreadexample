package com.example.multithread.processor;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Thread, we will search partitions of arrays for a match string "car"
 * @author lubo08
 *
 */
public class TaskProcessor implements Runnable {

	private String[] messages;
	private AtomicInteger processedRange;
	private int rangePartition;
	private AtomicInteger resultCounter;

	public TaskProcessor(String[] messages, AtomicInteger processedRange, int rangePartition,
			AtomicInteger resultCounter) {
		this.messages = messages;
		this.processedRange = processedRange;
		this.rangePartition = rangePartition;
		this.resultCounter = resultCounter;
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		int partitionCounter = processedRange.getAndIncrement() * rangePartition;
		// logger.info("processed partition {}",partitionCounter);
		int positiveMatches = 0;
		for (int i = 0; i < rangePartition; i++) {
			if (messages[partitionCounter++].contains("car")) {
				positiveMatches++;
			}
		}
		if (positiveMatches > 0) {
			resultCounter.addAndGet(positiveMatches);
		}
	}

}
