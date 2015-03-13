package com.qgf.game.base;

import java.nio.ByteBuffer;

import com.qgf.game.base.protocol.Message;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 *
 * @author qinguofeng
 * @date Mar 12, 2015
 */
public class GameClient {

	public static void main(String[] args) throws InterruptedException {
		final String HOST = "127.0.0.1";
		final int PORT = 8888;
		
		Bootstrap boot = new Bootstrap();
		EventLoopGroup eventLoop = new NioEventLoopGroup(1);
		boot.group(eventLoop)
			.channel(NioSocketChannel.class)
			.option(ChannelOption.TCP_NODELAY, true)
			.handler(new ChannelInitializer<SocketChannel>(){
				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					ChannelPipeline pipeline = ch.pipeline();
					pipeline.addLast(new AvroDecoder());
					pipeline.addLast(new AvroEncoder());
					pipeline.addLast(new ChannelInboundHandlerAdapter() {

						@Override
						public void channelRegistered(ChannelHandlerContext ctx)
								throws Exception {
							super.channelRegistered(ctx);
							System.out.println("on-channelRegistered");
						}

						@Override
						public void channelUnregistered(
								ChannelHandlerContext ctx) throws Exception {
							super.channelUnregistered(ctx);
							System.out.println("on-channelUnregistered");
						}

						@Override
						public void channelActive(ChannelHandlerContext ctx)
								throws Exception {
							super.channelActive(ctx);
							System.out.println("on-channelActive");
							Message msg = new Message();
							msg.name = "TestMsg";
							byte[] bs = "TestData".getBytes();
							ByteBuffer bb = ByteBuffer.allocate(bs.length);
							bb.put(bs);
							bb.flip();
							msg.setData(bb);
							ctx.writeAndFlush(msg);
						}

						@Override
						public void channelInactive(ChannelHandlerContext ctx)
								throws Exception {
							super.channelInactive(ctx);
							System.out.println("on-channelInactive");
						}

						@Override
						public void channelRead(ChannelHandlerContext ctx,
								Object msg) throws Exception {
							super.channelRead(ctx, msg);
							System.out.println("on-channelRead");
						}

						@Override
						public void channelReadComplete(
								ChannelHandlerContext ctx) throws Exception {
							super.channelReadComplete(ctx);
							System.out.println("on-channelReadComplete");
						}

						@Override
						public void userEventTriggered(
								ChannelHandlerContext ctx, Object evt)
								throws Exception {
							super.userEventTriggered(ctx, evt);
							System.out.println("on-userEventTriggered");
						}

						@Override
						public void channelWritabilityChanged(
								ChannelHandlerContext ctx) throws Exception {
							super.channelWritabilityChanged(ctx);
							System.out.println("on-channelWritabilityChanged");
						}

						@Override
						public void exceptionCaught(ChannelHandlerContext ctx,
								Throwable cause) throws Exception {
							super.exceptionCaught(ctx, cause);
							System.out.println("on-exceptionCaught");
						}

					});
				}});
		boot.connect(HOST, PORT).sync().channel().closeFuture().sync();
	}
}
