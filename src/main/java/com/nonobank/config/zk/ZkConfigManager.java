package com.nonobank.config.zk;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URI;
import java.util.List;

import org.I0Itec.zkclient.IZkStateListener;
import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.Watcher.Event.KeeperState;

import com.nonobank.config.IConfigManager;
import com.nonobank.config.NnZkChildListener;
import com.nonobank.config.NnZkDataChangListener;

public class ZkConfigManager implements IConfigManager{

	protected ZkClient zkClient;
	
	private volatile KeeperState state = KeeperState.SyncConnected;
	
	private String host;
	
	public ZkConfigManager(){
		
	}
	
	public void init() throws Exception {
		zkClient = new ZkClient(host);
		zkClient.subscribeStateChanges(new IZkStateListener() {
			
			public void handleStateChanged(KeeperState state) throws Exception {
				ZkConfigManager.this.state = state;
			}
			
			public void handleNewSession() throws Exception {
				
			}
		});
	}
	
	public void createGroup(String path) {
		if(!zkClient.exists(fPath(path))){
			zkClient.createPersistent(fPath(path));
		}
	}

	public boolean deleteGroup(String path) {
		return zkClient.delete(fPath(path));
	}

	public boolean isExistGroup(String path) {
		return zkClient.exists(fPath(path));
	}
	
	public void addDataChangesListener(String path, String key, NnZkDataChangListener nnZkDataListener) {
		String fPath = fPath(path) + SPLIT + key;
		zkClient.subscribeDataChanges(fPath, nnZkDataListener);
	}

	public void addChildChangesListener(String path, NnZkChildListener zkChildListener) {
		String fPath = fPath(path) ;
		zkClient.subscribeChildChanges(fPath, zkChildListener);
	}

	public boolean isConnected() {
		return state == KeeperState.SyncConnected;
	}

	public void close() {
		
	}

	public void setValue(String path, String key, Object obj) {
		String fPath = fPath(path) + SPLIT + key;
		if(zkClient.exists(fPath)){
			zkClient.writeData(fPath, obj);
		}else{
			zkClient.createPersistent(fPath);
			zkClient.writeData(fPath, obj);
		}
	}

	public void setValue(String path, String key, File file) throws Exception {
		String fPath = fPath(path) + SPLIT + key;
		zkClient.createPersistent(fPath, true);
		zkClient.writeData(fPath, getFileContent(new FileInputStream(file)));
	}

	public void setValue(String path, String key, InputStream in) throws Exception {
		String fPath = fPath(path) + SPLIT + key;
		zkClient.createPersistent(fPath, true);
		zkClient.writeData(path, getFileContent(in));
	}


	public <T extends Serializable> T getValue(String path, String key) {
		String fPath = fPath(path) + SPLIT + key;
		if(zkClient.exists(fPath)){
			return zkClient.readData(fPath);
		}else
			return null;
	}

	public boolean isExistValue(String path, String key) {
		Object value = getValue(path, key);
		return value == null ? false : true;
	}
	
	public void setValue(String path, String key, URI uri) throws Exception {
		setValue(path, key, new File(uri));
	}

	public void deleteValue(String group, String key) {
		String fPath = fPath(group) + SPLIT + key;
		if(zkClient.exists(fPath)){
			zkClient.delete(fPath);
		}
	}
	
	protected String fPath(String path){
		String fPath = null;
		if(path.contains(SPLIT)){
			fPath = BASE_CONFIG_PATH + path.substring(path.indexOf(SPLIT));
		}else{
			fPath = BASE_CONFIG_PATH + SPLIT + path;
		}
		return fPath;
	}
	
	private String getFileContent(InputStream in) throws Exception {
		byte[] b = new byte[8 * 1024];
		int hasRead = 0;
		StringBuilder sBuilder = new StringBuilder();
		while((hasRead = in.read(b)) != -1){
			sBuilder.append(new String(b, 0, hasRead));
		}
		in.close();
		return sBuilder.toString();
	}

	public List<String> getAllKeys(String group) {
		return zkClient.getChildren(fPath(group));
	}

	public void setHost(String host) {
		this.host = host;
	}

}
