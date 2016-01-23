package com.example.multithread.processor;

import java.time.LocalTime;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This partition consumer will take message from queue if available and search
 * ready partition
 * 
 * @author lubo08
 *
 */
public class PartitionsConsumer implements Runnable {

	private static final Logger LOG = LoggerFactory.getLogger(PartitionsConsumer.class);

	private BlockingQueue<Integer> availablePartitions;

	private Integer processedPartitions = 0;

	private AtomicInteger resultCounter = new AtomicInteger(0);

	private String[] messages;

	private int rangePartition;

	private int splitToPartitions;

	public PartitionsConsumer(BlockingQueue<Integer> availablePartitions, String[] messages, int rangePartition,
			int splitToPartitions) {
		this.availablePartitions = availablePartitions;
		this.messages = messages;
		this.rangePartition = rangePartition;
		this.splitToPartitions = splitToPartitions;
	}

	@Override
	public void run() {
		ExecutorService service = Executors.newFixedThreadPool(30);
		LOG.info("I'm starting search {} arrays of car matches in message [{}]", messages.length, LocalTime.now());

		while (processedPartitions != splitToPartitions) {
			Integer processedPosition = null;
			try {
				processedPosition = availablePartitions.take();
			} catch (InterruptedException e) {
				LOG.error("Waiting for message was interrupted", e);
			}
			service.execute(new TaskProcessor(messages, processedPosition, rangePartition, resultCounter));
			processedPartitions++;
			LOG.info("will search partition {}", processedPosition);
		}

		service.shutdown();
		boolean done = false;
		try {
			done = service.awaitTermination(3, TimeUnit.MINUTES);
		} catch (InterruptedException e) {
			LOG.error("Unsucsesfull array generate", e);
		}
		if (done) {
			LOG.info("search arrays finished [{}]", LocalTime.now());
			LOG.info("matches found: {}", resultCounter.get());
		}
	}

}
