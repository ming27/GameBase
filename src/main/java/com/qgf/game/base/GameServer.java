package com.qgf.game.base;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;

/**
 * 客户端采用不生成代码的方式, 服务器任然采用生成代码的方式
 * @author qinguofeng
 * @date 2015年3月9日
 */
public class GameServer {
	
	private static GameServer instance = null;

	public static GameServer createServer() {
		if (instance == null) {
			instance = new GameServer();
		}
		return instance;
	}

	public static GameServer getServer() {
		return instance;
	}

	private Map<Class<?>, IHandler> mHandlers = new HashMap<Class<?>, IHandler>();

	public void start(int port) throws InterruptedException {
		start(0, port);
	}

	public void start(int logicnum, int port) throws InterruptedException {
		start(1, 1, logicnum, port, 128);
	}

	public void start(int bossnum, int workernum, int logicnum, int port, int backlog) throws InterruptedException {
		EventLoopGroup bossGroup = new NioEventLoopGroup(bossnum);
		EventLoopGroup workerGroup = new NioEventLoopGroup(workernum);
		ServerBootstrap server = new ServerBootstrap();
		EventExecutorGroup eventExecutor = null;
		if (logicnum > 0) {
			eventExecutor = new DefaultEventExecutorGroup(logicnum);
		}
		server.group(bossGroup, workerGroup)
				.channel(NioServerSocketChannel.class)
				.option(ChannelOption.SO_BACKLOG, backlog)
				.childOption(ChannelOption.SO_KEEPALIVE, true)
				.handler(new AvroEncoder())
				.childHandler(new GameServerChannels(eventExecutor, new GameHandler()));
		server.bind(port).sync().channel().closeFuture().sync();
	}

	public void registHandler(IHandler handler) {
		// 遍历handler里面所有的 onMessage 方法, 放在map里面
		Method []methods = handler.getClass().getDeclaredMethods();
		if (methods != null) {
			for (Method m:methods) {
				String mtdName = m.getName();
				if ("onMessage".equals(mtdName) && m.getParameterCount() == 2) {
					mHandlers.put(m.getParameterTypes()[1], handler);
				}
			}
		}
	}
	
	public <T> IHandler getHandler(Class<T> t) {
		return mHandlers.get(t);
	}

	public static void main(String[] args) throws InterruptedException {
		final int PORT = 8888;
		EventLoopGroup bossGroup = new NioEventLoopGroup(1);
		EventLoopGroup workerGroup = new NioEventLoopGroup(1);
		ServerBootstrap server = new ServerBootstrap();
		server.group(bossGroup, workerGroup)
				.channel(NioServerSocketChannel.class)
				.option(ChannelOption.SO_BACKLOG, 128)
				.childOption(ChannelOption.SO_KEEPALIVE, true)
				.childHandler(new ChannelInitializer<SocketChannel>() {
					@Override
					public void initChannel(SocketChannel ch) throws Exception {
						ChannelPipeline p = ch.pipeline();
						p.addLast(new AvroDecoder());
						p.addLast(new AvroEncoder());
						p.addLast(new DefaultEventExecutorGroup(1), new GameHandler());
					}
				});
		server.bind(PORT).sync().channel().closeFuture().sync();
	}

	class GameServerChannels extends ChannelInitializer<SocketChannel> {

		private EventExecutorGroup mExecutorGroup;
		private ChannelHandler[] mChannelHandlers;

		public GameServerChannels(EventExecutorGroup executorGroup, ChannelHandler... handlers) {
			mExecutorGroup = executorGroup;
			mChannelHandlers = handlers;
		}

		@Override
		protected void initChannel(SocketChannel ch) throws Exception {
			ChannelPipeline pipeline = ch.pipeline();
			pipeline.addLast(new AvroDecoder());
			pipeline.addLast(new AvroEncoder());
			if (mExecutorGroup != null) {
				pipeline.addLast(mExecutorGroup, mChannelHandlers);
			} else {
				pipeline.addLast(mChannelHandlers);
			}
		}
	}
}
