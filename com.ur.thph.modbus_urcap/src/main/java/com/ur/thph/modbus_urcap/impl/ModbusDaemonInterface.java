package com.ur.thph.modbus_urcap.impl;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class ModbusDaemonInterface {

	private static final int PORT = 40408;
	private static final String HOST_IP = "127.0.0.1";
	private static final XmlRpcClient XML_RPC_CLIENT = new XmlRpcClient();
	private static final XmlRpcClientConfigImpl XML_RPC_CLIENT_CONFIG = new XmlRpcClientConfigImpl();
	
	private final AtomicBoolean isDaemonReachable = new AtomicBoolean(false);
	private final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
	private ScheduledFuture<?> scheduleAtFixedRate;

	public ModbusDaemonInterface() {
		setupXmlRpcClient();
//		startMonitorThread();
	}

	public static String getDaemonUrl() {
		return "http://" + HOST_IP + ":" + PORT + "/RPC2";
		
	}
	
	private static void setupXmlRpcClient() {
		try {
			XML_RPC_CLIENT_CONFIG.setEnabledForExtensions(true);
			XML_RPC_CLIENT_CONFIG.setServerURL(new URL("http://" + HOST_IP + ":" + PORT + "/RPC2"));
			XML_RPC_CLIENT_CONFIG.setConnectionTimeout(1000); //1s
			XML_RPC_CLIENT.setConfig(XML_RPC_CLIENT_CONFIG);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
	
//	public void startMonitorThread() {
//		Runnable containerMonitorRunnable = new Runnable() {
//			@Override
//			public void run() {
//				isDaemonReachable.set(tryExecuteIsReachable());
//			}
//		};
//
//		stopMonitorThread();
//		scheduleAtFixedRate = executorService.scheduleWithFixedDelay(containerMonitorRunnable, 0, 1, TimeUnit.SECONDS);
//	}
	
	public boolean tryExecuteIsReachable() {
		try {
			return (Boolean) XML_RPC_CLIENT.execute("isReachable", new ArrayList<String>());
		} catch (XmlRpcException ignored) {
			return false;
		}
	}

	public void stopMonitorThread() {
		if (scheduleAtFixedRate != null) {
			scheduleAtFixedRate.cancel(true);
		}
	}
	public boolean isDaemonReachable() {
		try {
			XML_RPC_CLIENT.execute("isReachable", new ArrayList<String>());
			return true;
		} catch (XmlRpcException ignored) {
			return false;
		}
	}
	
	private boolean processBoolean(Object response) throws UnknownResponseException {
		if (response instanceof Boolean) {
			Boolean val = (Boolean) response;
			return val.booleanValue();
		} else {
			throw new UnknownResponseException();
		}
	}

	private String processString(Object response) throws UnknownResponseException {
		if (response instanceof String) {
			return (String) response;
		} else {
			throw new UnknownResponseException();
		}
	}

}
