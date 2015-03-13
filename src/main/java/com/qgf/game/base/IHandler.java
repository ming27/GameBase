package com.qgf.game.base;

import io.netty.channel.ChannelHandlerContext;

import java.nio.ByteBuffer;

import com.qgf.game.base.protocol.Message;
import com.qgf.game.base.util.AvroUtil;

/**
 *
 * @author qinguofeng
 * @date Mar 13, 2015
 */
public abstract class IHandler {
//	final private <T> void onMessage(T msg) {
//		
//	}
	public <M> void sendMessage (ChannelHandlerContext ctx, M m) {
		ByteBuffer buffer = AvroUtil.encode2Byte(m);
		Message msg = new Message(m.getClass().getName(), buffer);
		ctx.writeAndFlush(msg);
	}
}
