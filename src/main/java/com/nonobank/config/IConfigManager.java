package com.nonobank.config;

import java.io.File;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URI;
import java.util.List;

public interface IConfigManager{

	String BASE_CONFIG_PATH = "/configuration";
	
	String SPLIT = "/";
	
	public void createGroup(String group);

	public boolean deleteGroup(String group);
	
	public boolean isExistGroup(String group);
	
	public void setValue(String group, String key, Object obj);
	
	public void setValue(String group, String key, File file) throws Exception;
	
	public void setValue(String group, String key, URI uri) throws Exception;
	
	public void setValue(String group, String key, InputStream in) throws Exception;
	
	public <T extends Serializable> T getValue(String group, String key);
	
	public void deleteValue(String group, String key);
	
	public void addDataChangesListener(String group, String key, NnZkDataChangListener nnZkDataListener);
	
	public void addChildChangesListener(String group,  NnZkChildListener zkChildListener);
	
	public boolean isExistValue(String group, String key);
	
	public List<String> getAllKeys(String group);
	
	boolean isConnected();

	public void close();
}
