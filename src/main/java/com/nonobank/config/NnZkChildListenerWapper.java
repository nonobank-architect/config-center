package com.nonobank.config;

import java.util.List;

public class NnZkChildListenerWapper implements NnZkChildListener{

	private NnZkChildListener nnZkChildListener;
	
	public NnZkChildListenerWapper(NnZkChildListener nnZkChildListener){
		this.nnZkChildListener = nnZkChildListener;
	}
	
	public void handleChildChange(String parentPath, List<String> currentChilds) throws Exception {
		String key = parentPath.substring(parentPath.lastIndexOf("/") + 1, parentPath.length());
		nnZkChildListener.handleChildChange(key, currentChilds);
	}

}
