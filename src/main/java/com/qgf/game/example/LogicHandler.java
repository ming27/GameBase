package com.qgf.game.example;

import io.netty.channel.ChannelHandlerContext;

import com.qgf.game.base.IHandler;

/**
 *
 * @author qinguofeng
 * @date Mar 13, 2015
 */
public class LogicHandler extends IHandler {

	public void onMessage(ChannelHandlerContext ctx, Example msg) {
		System.out.println(msg.getName() + ":" + msg.getValue());
	}

}
