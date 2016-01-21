package com.nonobank.config.spring;

import java.util.List;

import org.springframework.util.CollectionUtils;

import com.nonobank.config.IConfigManager;
import com.nonobank.config.NnZkDataChangeListenerWapper;

public abstract class AbstractDataChangeListener implements DataChangeListenerFacade{

	private IConfigManager configManager;
	
	
	@Override
	public void handleChildChange(String group, List<String> currentChilds) throws Exception {
		if(!CollectionUtils.isEmpty(currentChilds)){
			for (String key : currentChilds) {
				configManager.addDataChangesListener(group, key, new NnZkDataChangeListenerWapper(this));
			}
		}
		handleLaterChildChange(group, currentChilds);
	}


	protected void handleLaterChildChange(String group, List<String> currentChilds) {
		
	}


	public void setConfigManager(IConfigManager configManager) {
		this.configManager = configManager;
	}
	
}
