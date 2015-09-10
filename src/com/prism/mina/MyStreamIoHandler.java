package com.prism.mina;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.handler.stream.StreamIoHandler;

public class MyStreamIoHandler extends StreamIoHandler {

	@Override
	public void sessionOpened(IoSession session) {
		System.out.println("客户端连接了:" + session.getRemoteAddress());
		super.sessionOpened(session);

	}

	@Override
	protected void processStreamIo(IoSession session, InputStream in,
			OutputStream out) {

		// 设定一个线程池
		// 参数说明：最少数量3，最大数量6 空闲时间 3秒
		ThreadPoolExecutor threadPool = new ThreadPoolExecutor(3, 6, 3,
				TimeUnit.SECONDS,
				// 缓冲队列为3
				new ArrayBlockingQueue<Runnable>(3),
				// 抛弃旧的任务
				new ThreadPoolExecutor.DiscardOldestPolicy());
		FileOutputStream fos = null;
		File receiveFile = new File("e:\\hello.txt");
		try {
			fos = new FileOutputStream(receiveFile);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		// 将线程放入线程池 当连接很多时候可以通过线程池处理
		threadPool.execute(new IoStreamThreadWork(in, fos));

	}
}
