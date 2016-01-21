package com.nonobank.config;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.nonobank.config.annotion.GroupConfigData;

@Component
public class GroupData {

	@GroupConfigData(group="finance")
	private static Map<String, Serializable> map = new HashMap<String, Serializable>();
	
	public Serializable getValue(String key){
		return map.get(key);
	}

}
