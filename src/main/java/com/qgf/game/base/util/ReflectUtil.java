package com.qgf.game.base.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 *
 * @author qinguofeng
 * @date Mar 13, 2015
 */
public class ReflectUtil {

	public static void invokeMethod(Object obj, String name, Class<?>[] paramTypes, Object... params)
			throws NoSuchMethodException, SecurityException,
			IllegalAccessException, IllegalArgumentException,
			InvocationTargetException {
		Method m = obj.getClass().getDeclaredMethod(name, paramTypes);
		m.invoke(obj, params);
	}
}
