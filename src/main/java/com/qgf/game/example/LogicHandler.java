package com.qgf.game.example;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import io.netty.channel.ChannelHandlerContext;

import com.qgf.game.base.IHandler;

/**
 *
 * @author qinguofeng
 * @date Mar 13, 2015
 */
public class LogicHandler extends IHandler {

	public void handle(ChannelHandlerContext ctx, Example msg) {
		System.out.println(msg.getName() + ":" + msg.getValue());
	}

	@Override
	public <T> void onMessage(ChannelHandlerContext ctx, T msg) {
		// dispatch request here
		try {
			Method mtd = getClass().getDeclaredMethod("handle", ChannelHandlerContext.class, msg.getClass());
			mtd.invoke(this, ctx, msg);
		} catch (NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
			System.out.println("no such message");
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}

}
