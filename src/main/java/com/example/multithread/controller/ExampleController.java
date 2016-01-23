package com.example.multithread.controller;

import java.time.LocalTime;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.example.multithread.processor.PartitionsConsumer;
import com.example.multithread.processor.PartitionsProducer;

/**
 * Controller service to provide example tasks. 
 * @author lubo08
 *
 */
@Service
public class ExampleController {

	private static final Logger LOG = LoggerFactory.getLogger(ExampleController.class);

	private String[] messages = new String[100000000];

	private int splitToPartitions = 100;

	private int rangePartition = messages.length / splitToPartitions;
	
	private BlockingQueue<Integer> availablePartitions = new ArrayBlockingQueue<Integer>(splitToPartitions);
	
	private ExecutorService queueService = Executors.newCachedThreadPool();
	

	/**
	 * Process example tasks
	 */
	public void buildExample() {
		
		LOG.info("Example started [{}]", LocalTime.now());
		try {
			queueService.execute(new PartitionsProducer(availablePartitions,messages, rangePartition, splitToPartitions));
			queueService.execute(new PartitionsConsumer(availablePartitions,messages, rangePartition, splitToPartitions));
		} catch (Exception e) {
			LOG.error("Example error", e);
		}
		
		queueService.shutdown();
		boolean done = false;
		try {
			done = queueService.awaitTermination(5, TimeUnit.MINUTES);
		} catch (InterruptedException e) {
			LOG.error("Unsucsesfull searched messages", e);
		}
		if (done) {
			LOG.info("Example finished [{}]", LocalTime.now());
		}
		
		
	}

}
