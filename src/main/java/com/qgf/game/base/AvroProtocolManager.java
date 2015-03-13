package com.qgf.game.base;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author qinguofeng
 * @date Mar 11, 2015
 */
public class AvroProtocolManager {

	private static final Map<Integer, String> mN2SMap = new HashMap<Integer, String>();
	private static final Map<String, Integer> mS2NMap = new HashMap<String, Integer>();

	public static void addProtocol(String protocol) {
		
	}

	public static String getProtocol(Integer num) {
		return mN2SMap.get(num);
	}

	public static Integer getProtocol(String name) {
		return mS2NMap.get(name);
	}
}
