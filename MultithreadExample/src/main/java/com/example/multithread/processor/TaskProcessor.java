package com.example.multithread.processor;

import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.util.Assert;

/**
 * Thread, we will search partitions of arrays for a match string "car"
 * 
 * @author lubo08
 *
 */
public class TaskProcessor implements Runnable {

	private String[] messages;
	
	private Integer processedRange;
	
	private int rangePartition;
	
	private AtomicInteger resultCounter;

	public TaskProcessor(String[] messages, Integer processedRange, int rangePartition, AtomicInteger resultCounter) {
		this.messages = messages;
		this.processedRange = processedRange;
		this.rangePartition = rangePartition;
		this.resultCounter = resultCounter;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		Assert.notNull(processedRange, "Partition indentifier cannot be null");
		int partitionCounter = processedRange * rangePartition;
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
