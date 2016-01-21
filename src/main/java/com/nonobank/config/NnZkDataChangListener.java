package com.nonobank.config;

import org.I0Itec.zkclient.IZkDataListener;

public interface NnZkDataChangListener extends IZkDataListener{
	
	 public void handleDataChange(String key, Object data) throws Exception;

	 public void handleDataDeleted(String key) throws Exception;
}