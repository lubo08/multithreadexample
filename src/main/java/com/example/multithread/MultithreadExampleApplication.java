package com.example.multithread;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import com.example.multithread.controller.ExampleController;

/**
 * This is an example application. Example show how to find in very big array of Strings concrete word and count them. We have String[10000000] 
 * with random generated strings of words. 
 * @author lubo08
 *
 */
@SpringBootApplication
public class MultithreadExampleApplication {
	
	
	
	/**
	 * Will run this application
	 * @param args
	 */
	public static void main(String[] args) {
		ApplicationContext context =  SpringApplication.run(MultithreadExampleApplication.class, args);
		ExampleController controller = (ExampleController)context.getBean("exampleController");
		controller.buildExample();		
	}
	

	
}
