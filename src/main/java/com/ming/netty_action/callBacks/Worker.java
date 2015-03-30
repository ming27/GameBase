package com.ming.netty_action.callBacks;

public class Worker {
	public void doWork() {
		Fetcher  fetcher = new MyFetcher(new Data(1,1));
		fetcher.fetchData(new FetcherCallback() {
			
			@Override
			public void onError(Throwable cause) {
				System.out.println("An erroe accour: " + cause.getMessage());
			}
			
			@Override
			public void onData(Data data) throws Exception {
				System.out.println("Data received: " + data);
			}
		});
	}
	
	public static void main(String[] args) {
		Worker w = new Worker();
		w.doWork();
	}
}
