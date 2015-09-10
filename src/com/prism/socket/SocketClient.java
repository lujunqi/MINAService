package com.prism.socket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class SocketClient {
	private Socket s;  
    private DataOutputStream out;  
    private DataInputStream in;  
    public SocketClient() throws IOException {  
    }  
  
  
    public static void main(String[] args) throws Exception {  
        SocketClient c = new SocketClient();  
        c.talk();  
    }  
//  发送对象  
//  ObjectOutputStream oos;  
//  TransferObj obj;  
    public void sendMessage(Socket s) {  
        try {  
              
            //socket传字符串  
            out = new DataOutputStream(s.getOutputStream());  
            byte[] bt="中文\n".getBytes();  
            out.write(bt);  
            out.writeBytes("nafio_date\n");  
            //out.writeUTF("中文\n");//by nafio这么写不行  
              
            //socket传对象  
//          oos = new ObjectOutputStream(s.getOutputStream());  
//          obj=new TransferObj();  
//          obj.setDate("socketDateToMina");  
//          oos.writeObject(obj);  
//          oos.flush();  
              
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
  
    }  
  
    public void receiveMessage(Socket s) {  
        try {  
            in = new DataInputStream(s.getInputStream());  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }  
  
    public void talk() throws Exception {  
        /*while (true)*/ {  
            try {  
                //发送对象  
                //oos.close();  
                s = new Socket("127.0.0.1", 9000);  
                System.out.println("客户端:发送信息");  
                sendMessage(s);  
                System.out.println("发送信息完毕!");  
                //发字符串  
                //receiveMessage(s);  
                out.close();  
                //in.close();  
            }  
            catch(Exception e){  
                e.printStackTrace();  
            }  
            finally {  
                try{  
                    if(s!=null)s.close();  //断开连接  
                }catch (IOException e) {e.printStackTrace();}  
            }  
        }  
    } 
}
