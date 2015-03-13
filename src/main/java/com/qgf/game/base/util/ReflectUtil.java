package com.qgf.game.base.util;

import io.netty.channel.ChannelHandlerContext;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.avro.specific.SpecificRecordBase;

/**
 *
 * @author qinguofeng
 * @date Mar 13, 2015
 */
public class ReflectUtil {

	public static void invokeMethod(Object obj, String name, Object... params)
			throws NoSuchMethodException, SecurityException,
			IllegalAccessException, IllegalArgumentException,
			InvocationTargetException {
		int pcount = 0;
		if (params != null) {
			pcount = params.length;
		}
		Class<?> paramTypes[] = new Class<?>[pcount];
		// TODO ... 临时方案, 待重构
		paramTypes[0] = ChannelHandlerContext.class;
		try {
			paramTypes[1] = Class.forName(((SpecificRecordBase)params[1]).getSchema().getFullName());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		Method m = obj.getClass().getDeclaredMethod(name, paramTypes);
		m.invoke(obj, params);
	}
}
