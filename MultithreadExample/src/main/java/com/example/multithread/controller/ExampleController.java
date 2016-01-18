package com.example.multithread.controller;

import java.time.LocalTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.example.multithread.processor.RandomStringGenerator;
import com.example.multithread.processor.TaskProcessor;

/**
 * Controller service to provide example tasks. 
 * @author lubo08
 *
 */
@Service
public class ExampleController {

	private static final Logger logger = LoggerFactory.getLogger(ExampleController.class);

	private String[] messages = new String[100000000];

	private ExecutorService service;

	private AtomicInteger processedPosition = new AtomicInteger(0);

	private int splitToPartitions = 1000;

	private int rangePartition = messages.length / splitToPartitions;

	private AtomicInteger resultCounter = new AtomicInteger(0);
	
	private String[] messageDict = new String[] { "Hello here", "take care", "car wash", "nobody here" };

	/**
	 * Process example tasks
	 */
	public void buildExample() {
		// first we run simple array generate;
		logger.info("I'm starting generate {} arrays of rundom strings without multithreading [{}]", messages.length, LocalTime.now());
		for(int i = 0; i < messages.length; i++) {
				messages[i] = messageDict[ThreadLocalRandom.current().nextInt(0, 4)];
		}
		logger.info("Generate rundom strings finished [{}]", LocalTime.now());
		
		logger.info("I'm starting search {} arrays of car matches in message without multithreading [{}]", messages.length,LocalTime.now());
		int matchesCounter = 0;
		for(int i = 0; i < messages.length; i++) {
			if (messages[i].contains("car")) {
				matchesCounter++;
			}
		}
		logger.info("search single arrays finished [{}]", LocalTime.now());
		logger.info("matches found: {}", matchesCounter);
		
		//now we run thread solution.
		service = Executors.newFixedThreadPool(30);
		logger.info("I'm starting generate {} arrays of rundom strings [{}]", messages.length, LocalTime.now());
		for (int i = 0; i < splitToPartitions; i++) {
			service.execute(new RandomStringGenerator(messages, processedPosition, rangePartition));
		}
		service.shutdown();
		boolean done = false;
		try {
			done = service.awaitTermination(3, TimeUnit.MINUTES);
		} catch (InterruptedException e) {
			logger.error("Unsucsesfull array generate", e);
		}
		if (done) {
			logger.info("Generate arrays of rundom strings finished [{}]", LocalTime.now());
			logger.info("message at position 500524 is: {}", messages[500524]);
		}
		
		service = Executors.newFixedThreadPool(30);
		logger.info("I'm starting search {} arrays of car matches in message [{}]", messages.length, LocalTime.now());
		processedPosition.set(0);
		for (int i = 0; i < splitToPartitions; i++) {
			service.execute(new TaskProcessor(messages, processedPosition, rangePartition, resultCounter));
		}
		service.shutdown();
		done = false;
		try {
			done = service.awaitTermination(3, TimeUnit.MINUTES);
		} catch (InterruptedException e) {
			logger.error("Unsucsesfull array generate", e);
		}
		if (done) {
			logger.info("search arrays finished [{}]", LocalTime.now());
			logger.info("matches found: {}", resultCounter.get());
		}
		
	}

}
