package com.nonobank.config;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MainClient {

	private static ApplicationContext context ;
	
	@Before
	public void before(){
		context = new ClassPathXmlApplicationContext("classpath:app-context.xml");
	}
	
	@Test
	public void test(){
		GroupData groupData = (GroupData)context.getBean("groupData");
		System.out.println(groupData.getValue("A1"));
	}
}
