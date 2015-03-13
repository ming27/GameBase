package com.qgf.game.base;

import org.apache.avro.specific.SpecificRecordBase;

import com.qgf.game.base.protocol.Message;
import com.qgf.game.base.util.AvroUtil;
import com.qgf.game.base.util.ReflectUtil;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 *
 * @author qinguofeng
 * @date 2015年3月9日
 */
public class GameHandler extends ChannelInboundHandlerAdapter {

	@Override
	public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
		super.channelRegistered(ctx);
	}

	@Override
	public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
		super.channelUnregistered(ctx);
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		super.channelActive(ctx);
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		super.channelInactive(ctx);
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		if (msg instanceof Message) {
			GameServer server = GameServer.getServer();
			Message msgObj = (Message) msg;
			Object obj = AvroUtil.decodeFrom(msgObj.getData(), msgObj.getName().toString());
			IHandler handler = server.getHandler(obj.getClass());
			ReflectUtil.invokeMethod(handler, "onMessage", ctx, obj);
		}
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		super.channelReadComplete(ctx);
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt)
			throws Exception {
		super.userEventTriggered(ctx, evt);
	}

	@Override
	public void channelWritabilityChanged(ChannelHandlerContext ctx)
			throws Exception {
		super.channelWritabilityChanged(ctx);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		super.exceptionCaught(ctx, cause);
	}

}
