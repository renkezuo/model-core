package com.renke.core.test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class GBK2UTF8 {
	public static void convert(File file) {
		try {
			BufferedReader br = new BufferedReader(
					new InputStreamReader(new FileInputStream(file), Charset.forName("GBK")));
			StringBuilder sb = new StringBuilder();
			String line = "";
			while ((line = br.readLine()) != null) {
				sb.append(line);
				sb.append("\n");
			}

			BufferedWriter bw = new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream(file), Charset.forName("UTF-8")));
			bw.write(sb.toString());
			bw.flush();
			br.close();
			bw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void searchFilesByDir(File dir ,FileFilter ff,List<File> result){
		if(dir.exists() && dir.isDirectory()){
			File[] files = dir.listFiles();
			for(File file : files){
				if(file.isDirectory()){
					searchFilesByDir(file,ff,result);
				}else{
					if(ff.accept(file)){
						result.add(file);
					}
				}
			}
		}
	}
	
	public static void main(String[] args) {
		File dir = new File ("E:/gitsource/util/src/main/java");
		List<File> files = new ArrayList<>();
		searchFilesByDir(dir,(f) -> {return (f.isFile() && f.getPath().lastIndexOf(".java") > -1);},files);
		for(File file : files){
			convert(file);
		}
	}
}
