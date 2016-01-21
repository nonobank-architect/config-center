package com.nonobank.config.spring;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.nonobank.config.IConfigManager;
import com.nonobank.config.NnZkChildListener;
import com.nonobank.config.NnZkDataChangListener;

public class ConfigDataContainer {

	private  final Map<String, Map<String, Serializable>> data = new ConcurrentHashMap<String, Map<String, Serializable>>();
	
	private static ConfigDataContainer container;
	
	private IConfigManager configManager;
	
	public Serializable getData(String group, String key){
		Map<String, Serializable>  d = data.get(group);
		if(d != null){
			return d.get(key);
		}else
			return null;
	}
	
	public Map<String, Serializable> getKVs(String group){
		return data.get(group);
	}
	
	public void setData(final String group, final String key, Serializable value) throws Exception{
		
		synchronized (data) {
			Map<String, Serializable>  d = data.get(group);
			if(d == null){
				d = new ConcurrentHashMap<String, Serializable>();
				data.put(group, d);
				configManager.addChildChangesListener(group, new NnZkChildListener() {

					@Override
					public void handleChildChange(String parentPath, List<String> currentChilds) throws Exception {
						if(currentChilds != null && currentChilds.size() > 0){
							for (String key : currentChilds) {
								Map<String, Serializable>  d = data.get(group);
								Serializable value = configManager.getValue(group, key);
								d.put(key, value);
							}
						}
					}
				});
			}
			d.put(key, value);
		}
		configManager.addDataChangesListener(group, key, new NnZkDataChangListener() {
			
			@Override
			public void handleDataDeleted(String dataPath) throws Exception {
				data.get(group).remove(key);
			}
			
			@Override
			public void handleDataChange(String dataPath, Object value) throws Exception {
				data.get(group).put(key, (Serializable)value);
			}
		});
	}
	
	public static ConfigDataContainer newInstance(){
		synchronized (ConfigDataContainer.class) {
			if(container == null){
				container = new ConfigDataContainer();
			}
		}
		return container;
	}
 
	public void setConfigManager(IConfigManager configManager) {
		this.configManager = configManager;
	}
	
	public void addDataChangeListener(String group, String key, NnZkDataChangListener nnZkDataListener){
		configManager.addDataChangesListener(group, key, nnZkDataListener);
	}

	public void addChildChangesListener(String group, NnZkChildListener zkChildListener){
		configManager.addChildChangesListener(group, zkChildListener);
	}
	
}
