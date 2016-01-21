package com.nonobank.config.spring;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.nonobank.config.IConfigManager;
import com.nonobank.config.NnZkChildListenerWapper;
import com.nonobank.config.NnZkDataChangeListenerWapper;
import com.nonobank.config.annotion.DataChangeListener;
import com.nonobank.config.annotion.GroupConfigData;

public class ConfigDataBeanProcessor extends InstantiationAwareBeanPostProcessorAdapter implements InitializingBean{

	private String groups;
	
	private IConfigManager configManager;

	private static final String SPLIT = ",";
	

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		dataChangeListenerProcess(bean);
//		childChangeListenerProcess(bean);
		return super.postProcessAfterInitialization(bean, beanName);
	}

	@Override
	public boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException {
		Class<?> clz = bean.getClass();
		Field[] fields = clz.getDeclaredFields();
		if(fields != null){
			for (Field field : fields) {
				GroupConfigData configData = field.getAnnotation(GroupConfigData.class);
				if(configData != null){
					String group = configData.group();
					Map<String, Serializable> value = ConfigDataContainer.newInstance().getKVs(group);
					field.setAccessible(true);
					try {
						field.set(bean, value);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		return true;
	}

	/**
	 * 处理childchangeslistener注解
	 * @param bean
	 */
	/*private void childChangeListenerProcess(Object bean) {
		Class<?> clz = bean.getClass();
		ChildChangeListener ccl = clz.getAnnotation(ChildChangeListener.class);
		if(ccl != null){
			if(NnZkChildListener.class.isAssignableFrom(clz)){
				NnZkChildListener listener = (NnZkChildListener)bean;
				String group = ccl.group();
				configManager.addChildChangesListener(group, new NnZkChildListenerWapper(listener));
			}
		}
	}*/

	/**
	 * 处理datachangelistener注解
	 * @param bean
	 */
	private void dataChangeListenerProcess(Object bean) {
		Class<?> clz = bean.getClass();
		DataChangeListener changeListener = clz.getAnnotation(DataChangeListener.class);
		if(changeListener != null){
			if(DataChangeListenerFacade.class.isAssignableFrom(clz)){
				DataChangeListenerFacade listener = (DataChangeListenerFacade)bean;
				listener.setConfigManager(configManager);
				String group = changeListener.group();
				String key = changeListener.key();
				if(!StringUtils.isEmpty(key)){
					configManager.addDataChangesListener(group, key, new NnZkDataChangeListenerWapper(listener));
				}else{
					List<String> keys = configManager.getAllKeys(group);
					configManager.addChildChangesListener(group, new NnZkChildListenerWapper(listener));
					if(!CollectionUtils.isEmpty(keys)){
						for (int i = 0; i < keys.size(); i++) {
							String key2 = keys.get(i);
							configManager.addDataChangesListener(group, key2, new NnZkDataChangeListenerWapper(listener));
						}
					}
				}
			}
		}
	}



	@Override
	public void afterPropertiesSet() throws Exception {
		
		ConfigDataContainer.newInstance().setConfigManager(configManager);
		String[] groupArray = groups.split(SPLIT);
		if(groupArray != null && groupArray.length > 0 ){
			for (String group : groupArray) {
				List<String> keys = configManager.getAllKeys(group);
				if(keys != null && keys.size() > 0){
					for (String key : keys) {
						Serializable value = configManager.getValue(group, key);
						ConfigDataContainer.newInstance().setData(group, key, value);
					}
				}
			}
		}
	}


	public String getGroups() {
		return groups;
	}

	public void setGroups(String groups) {
		this.groups = groups;
	}

	public IConfigManager getConfigManager() {
		return configManager;
	}

	public void setConfigManager(IConfigManager configManager) {
		this.configManager = configManager;
	}
	
}
