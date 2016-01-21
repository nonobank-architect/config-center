package com.nonobank.config.spring;

import com.nonobank.config.IConfigManager;
import com.nonobank.config.NnZkChildListener;
import com.nonobank.config.NnZkDataChangListener;

public interface DataChangeListenerFacade extends NnZkChildListener, NnZkDataChangListener {

	public void setConfigManager(IConfigManager configManager);
}
