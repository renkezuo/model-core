package com.renke.core.tools;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileTool {
	public static void appendBytes(String path,byte[] bytes){
		appendBytes(path,bytes,0,bytes.length);
	}
	
	public static void appendBytes(String path,byte[] bytes,int off,int len){
		FileOutputStream fos = null;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		File file = new File(path);
		try {
			//追加
			fos = new FileOutputStream(file,true);
			baos.write(bytes,off,len);
			baos.writeTo(fos);
			baos.flush();
		}catch (IOException e) {
			e.printStackTrace();
		} finally{
			try {
				if(fos!=null){
					fos.close();
					fos = null;
				}
				baos.close();
				baos = null;
				file = null;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
