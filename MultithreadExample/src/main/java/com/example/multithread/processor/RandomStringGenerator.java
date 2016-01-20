package com.example.multithread.processor;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Thread in which will be generated random message added to array
 * @author lubo08
 *
 */
public class RandomStringGenerator implements Runnable {
	
	private static final Logger LOG = LoggerFactory.getLogger(RandomStringGenerator.class);
	
	private String[] messageDict = new String[] { "Hello here", "take care", "car wash", "nobody here" };

	private String[] messages;
	private AtomicInteger processedRange;
	private int rangePartition;
	private BlockingQueue<Integer> availablePartitions;

	public RandomStringGenerator(String[] messages, AtomicInteger processedRange, int rangePartition, BlockingQueue<Integer> availablePartitions) {
		this.messages = messages;
		this.processedRange = processedRange;
		this.rangePartition = rangePartition;
		this.availablePartitions = availablePartitions;
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		int currentPartition = processedRange.getAndIncrement();
		int partitionCounter = currentPartition * rangePartition;
		for (int i = 0; i < rangePartition; i++) {
			messages[partitionCounter++] = messageDict[ThreadLocalRandom.current().nextInt(0, 4)];
		}
		try {
			availablePartitions.put(currentPartition);
		} catch (InterruptedException e) {
			LOG.error("Partition {} will not be searched",currentPartition,e);
		}
	}

}
