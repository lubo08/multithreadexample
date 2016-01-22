package com.example.multithread;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import com.example.multithread.controller.ExampleController;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MultithreadExampleApplication.class)
public class MultithreadExampleApplicationTests {
	
	@Autowired
	private ExampleController exampleController;
	
	@Test
	public void contextLoads() {
		
		Assert.notNull(exampleController);
		
	}

}
