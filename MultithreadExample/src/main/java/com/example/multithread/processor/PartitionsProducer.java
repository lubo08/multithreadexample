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
 * This producer will produce message partition and complete partitions add to
 * queue for search.
 * 
 * @author lubo08
 *
 */
public class PartitionsProducer implements Runnable {

	private static final Logger LOG = LoggerFactory.getLogger(PartitionsProducer.class);

	private BlockingQueue<Integer> availablePartitions;

	private AtomicInteger processedRange = new AtomicInteger(0);

	private String[] messages;

	private int rangePartition;

	private int splitToPartitions;

	public PartitionsProducer(BlockingQueue<Integer> availablePartitions, String[] messages, int rangePartition,
			int splitToPartitions) {
		this.availablePartitions = availablePartitions;
		this.messages = messages;
		this.rangePartition = rangePartition;
		this.splitToPartitions = splitToPartitions;
	}

	@Override
	public void run() {

		ExecutorService service = Executors.newFixedThreadPool(30);
		LOG.info("I'm starting generate {} arrays of rundom strings [{}]", messages.length, LocalTime.now());
		for (int i = 0; i < splitToPartitions; i++) {
			service.execute(new RandomStringGenerator(messages, processedRange, rangePartition, availablePartitions));
		}
		service.shutdown();
		boolean done = false;
		try {
			done = service.awaitTermination(3, TimeUnit.MINUTES);
		} catch (InterruptedException e) {
			LOG.error("Unsucsesfull array generate", e);
		}
		if (done) {
			LOG.info("Generate arrays of rundom strings finished [{}]", LocalTime.now());
			LOG.info("message at position 500524 is: {}", messages[500524]);
		}
	}

}
