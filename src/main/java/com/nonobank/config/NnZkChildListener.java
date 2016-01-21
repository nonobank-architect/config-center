package com.nonobank.config;

import java.util.List;

import org.I0Itec.zkclient.IZkChildListener;

public interface NnZkChildListener extends IZkChildListener {

	public void handleChildChange(String group, List<String> currentChilds) throws Exception;
}
