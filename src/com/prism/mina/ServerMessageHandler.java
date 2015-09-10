package com.prism.mina;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

import org.apache.mina.core.future.CloseFuture;
import org.apache.mina.core.future.IoFuture;
import org.apache.mina.core.future.IoFutureListener;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

public class ServerMessageHandler implements IoHandler {

	@Override
	public void exceptionCaught(IoSession session, Throwable cause)
			throws Exception {
		System.out.println("服务器发生异常： {}" + cause.getMessage());
	}

	@Override
	public void messageReceived(IoSession session, Object message)
			throws Exception {
		byte[] b = (byte[])message;
		System.out.println("服务器接收到数据： {}" + b.length + "=="
				+ message.toString());

		// String content = message.toString();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String datetime = sdf.format(new Date());

//		System.out.println("转发 messageReceived: " + datetime + "\t" + message);

		// 拿到所有的客户端Session
		Collection<IoSession> sessions = session.getService()
				.getManagedSessions().values();
		
		// 向所有客户端发送数据
		for (IoSession sess : sessions) {
//			sess.write(message);
		}
	}

	@Override
	public void messageSent(IoSession session, Object message) throws Exception {
//		System.out.println("服务器发送消息： {}" + message);
	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {
		System.out.println("关闭当前session：{}#{}" + session.getId()
				+ session.getRemoteAddress());

		CloseFuture closeFuture = session.close(true);
		closeFuture.addListener(new IoFutureListener<IoFuture>() {
			public void operationComplete(IoFuture future) {
				if (future instanceof CloseFuture) {
					((CloseFuture) future).setClosed();
					System.out
							.println("sessionClosed CloseFuture setClosed-->{},"
									+ future.getSession().getId());
				}
			}
		});
	}

	@Override
	public void sessionCreated(IoSession session) throws Exception {
		System.out.println("创建一个新连接：{}" + session.getRemoteAddress());
		// session.write("");
	}

	@Override
	public void sessionIdle(IoSession session, IdleStatus status)
			throws Exception {
		// System.out.println("当前连接{}处于空闲状态：{}" + session.getRemoteAddress()
		// + status);
	}

	@Override
	public void sessionOpened(IoSession session) throws Exception {
		System.out.println("打开一个session：{}#{}" + session.getId()+"===="
				+ session.getBothIdleCount());
	}

	@Override
	public void inputClosed(IoSession session) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("inputClosed");
	}

}
