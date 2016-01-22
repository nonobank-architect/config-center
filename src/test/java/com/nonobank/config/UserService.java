package com.nonobank.config;

import com.nonobank.config.annotion.DataChangeListener;
import com.nonobank.config.spring.AbstractDataChangeListener;

@DataChangeListener(group="finance")
public class UserService extends AbstractDataChangeListener{

	
	@Override
	public void handleDataChange(String key, Object data) throws Exception {
		System.out.println("Data Change key: " + key + "data :" + data);
	}

	@Override
	public void handleDataDeleted(String key) throws Exception {
		System.out.println("Data Deleted key : " + key);
	}

	
}
