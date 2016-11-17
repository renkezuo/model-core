package com.renke.core.tools;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class RedisTool {
	private static SocketChannel socketChannel;
	private final static String REDIS_IP = "121.43.199.167";
	private final static String REDIS_PWD = "aBc@123456";
    private synchronized static void loginRedis() throws IOException{
		if(socketChannel == null || !socketChannel.isConnected()){
			ByteBuffer buf = ByteBuffer.allocate(1024);
			socketChannel = SocketChannel.open();
			socketChannel.connect(new InetSocketAddress(REDIS_IP, 6379));
			
			if(!"".equals(REDIS_PWD)){
				socketChannel.write(ByteBuffer.wrap(("auth "+REDIS_PWD+"\r\n").getBytes()));
				socketChannel.read(buf);
				if(buf.array()[0]=='-'){
					throw new IOException("redis：密码错误，登陆失败！");
				}
				buf.clear();
			}
			buf = null;
		}
	}
    
    public static String getRedisValueByKey(String redis_key){
    	String token = "";
		ByteBuffer buf = ByteBuffer.allocate(1024);
    	try {
    		loginRedis();
			socketChannel.write(ByteBuffer.wrap(("get "+redis_key+" \r\n").getBytes()));
			socketChannel.read(buf);
			byte[] bytes = buf.array();
			boolean isData  = false;
			int begin = 0;
			for(int i=0;i<bytes.length;i++){
				byte b = bytes[i];
				if(b==10){
					if(isData){
						token = new String(bytes,begin,i-begin-1);
						break;
					}else{
						isData = true;
						begin = i+1;
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
    	buf = null;
    	return token;
    }
}
