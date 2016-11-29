package com.renke.core.tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class CompressTools {
	/***
	 * 读取文件的每一行，trim()
	 * 将所有',|;|:|=|+|-|*|/|%|(|)|{|}|[|]|<|>|\|"|''之后之前的空白字符去掉
	 * 多个空白字符替换为一个
	 * 
	 * @param html
	 * @return
	 * @author renke.zuo@foxmail.com
	 * @time 2016-11-18 15:02:07
	 */
	public static String compress(String html){
		if(html.indexOf("//")>-1) return ""; 
		html = html.trim();
		html = html.replaceAll("\\s*,\\s*", ",")
				.replaceAll("\\s*;\\s*", ";").replaceAll("\\s*:\\s*", ":")
				.replaceAll("\\s*=\\s*", "=").replaceAll("\\s*\\+\\s*", "+")
				.replaceAll("\\s*-\\s*", "-").replaceAll("\\s*\\*\\s*", "*")
				.replaceAll("\\s*\\/\\s*", "/").replaceAll("\\s*\\%\\s*", "%")
				.replaceAll("\\s*\\(\\s*", "(").replaceAll("\\s*\\)\\s*", ")")
				.replaceAll("\\s*\\{\\s*", "{").replaceAll("\\s*\\}\\s*", "}")
				.replaceAll("\\s*\\[\\s*", "[").replaceAll("\\s*\\]\\s*", "]")
				.replaceAll("\\s*\\>\\s*", ">").replaceAll("\\s*\\<\\s*", "<")
				.replaceAll("\\s*\"\\s*", "\"").replaceAll("\\s*'\\s*", "'")
				.replaceAll("\\s*\\\\\\s*", "\\\\");
		html.replaceAll("\\s+", " ");
		return html;
	}
	
	public static void compressFile(File file){
		if(file == null || !file.isFile()){
			return ;
		}
		StringBuilder html = new StringBuilder();
		String filePath = file.getAbsolutePath();
		String newFilePath = filePath.substring(0, filePath.lastIndexOf("."))+".min"+filePath.substring(filePath.lastIndexOf("."));
		BufferedReader br = null;
		BufferedWriter bw = null;
		try {
			br = new BufferedReader(new FileReader(file));
			bw = new BufferedWriter(new FileWriter(new File(newFilePath)));
			String line = br.readLine();
			int lineLength = 5000;
			int lastLength = 0;
			while(line != null){
				html.append(compress(line));
				if((html.length()-lastLength) - lineLength > 0 ){
					lastLength = html.length();
					html.append("\r\n");
				}
				line = br.readLine();
			}
			bw.write(html.toString());
			bw.flush();
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			try {
				if(br!=null) br.close();
				if(bw!=null) bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) {
		File file = new File("F:\\work\\model\\简单后台\\js\\bootstrap-table.js");
		compressFile(file);
	}
}
