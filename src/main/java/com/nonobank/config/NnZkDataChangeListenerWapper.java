package com.nonobank.config;

public class  NnZkDataChangeListenerWapper implements NnZkDataChangListener{

	private NnZkDataChangListener nnchangListener;
	
	public NnZkDataChangeListenerWapper(NnZkDataChangListener changListener){
		nnchangListener = changListener;
	}
	
	public void handleDataChange(String dataPath, Object data) throws Exception {
		String key = dataPath.substring(dataPath.lastIndexOf("/") + 1, dataPath.length());
		this.nnchangListener.handleDataChange(key, data);
	}
	
	public void handleDataDeleted(String dataPath) throws Exception {
		String key = dataPath.substring(dataPath.lastIndexOf("/") + 1, dataPath.length());
		this.nnchangListener.handleDataDeleted(key);
	}
}
