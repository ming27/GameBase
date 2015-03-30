package com.ming.netty_action.callBacks;

interface FetcherCallback {
	void onData(Data data) throws Exception;
	void onError(Throwable cause);
}

interface Fetcher {
	void fetchData(FetcherCallback callback);
}

public class MyFetcher implements Fetcher {
	
	final Data data;
	
	public MyFetcher(Data data) {
		this.data = data;
	}
	
	@Override
	public void fetchData(FetcherCallback callback) {
		try {
			callback.onData(data);
		} catch(Exception e) {
			callback.onError(e);
		}
	}

}
