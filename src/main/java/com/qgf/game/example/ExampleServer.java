package com.qgf.game.example;

import com.qgf.game.base.GameServer;

/**
 *
 * @author qinguofeng
 * @date Mar 13, 2015
 */
public class ExampleServer {

	public static void main(String[] args) {
		GameServer server = GameServer.createServer();
		server.registHandler(new LogicHandler());
		try {
			server.start(8888);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
