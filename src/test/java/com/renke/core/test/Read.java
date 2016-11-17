package com.renke.core.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.mysql.cj.core.MysqlType;
import com.renke.core.tools.ResultSetTool;

public class Read {
	@Test
	public void getFile() throws IOException{
		File file = new File("MysqlType.txt");
		System.out.println(file.getAbsolutePath());;
		BufferedReader fr =  new BufferedReader(new FileReader(file));
		String line = null;
		List<Type> list = new ArrayList<Type>();
		Type type = new Type();
		while( (line = fr.readLine())!=null){
			if(line.indexOf("indexOfIgnoreCase")!=-1){
				String[] args = line.split("\"");
				if(args.length >=1){
					for(int i=1;i<args.length;i++,i++){
						type.clazz.add(args[i]);
					}
				}
			}else if(line.indexOf("return") != -1){
				String result = line.split("return ")[1];
				type.type = result.substring(0, result.lastIndexOf(";"));
				list.add(type);
				type = new Type();
			}else{
				continue;
			}
		}
		fr.close();
		for(Type t : list){
			for(String str : t.clazz){
				System.out.println("simpleNameToMysqlType.put(\""+str+"\",MysqlType."+t.type+");");
			}
		}
		for(Type t : list){
			for(String str : t.clazz){
//				System.out.println("simpleNameToMysqlType.put(\""+str+"\",MysqlType."+t.type+");");
				System.out.println("System.out.println(\"simpleNameToTypes.put(\\\""+str+"\\\",\"+"+"MysqlType."+t.type+".getJdbcType()+\");\" );");
			}
		}
		
	}
	public static void main(String[] args) throws InterruptedException {
		long begin = System.currentTimeMillis();
		
		for(int i = 0;i<1000000; i++){
			ResultSetTool.getMysqlType("Integer");
		}
		System.out.println(
				ResultSetTool.getMysqlType("Integer"));
		System.out.println("map:"+(System.currentTimeMillis() - begin) + "ms");

		
		begin = System.currentTimeMillis();
		for(int i = 0;i<1000000; i++){
			MysqlType.getByName("Integer");
		}
		System.out.println(
				MysqlType.getByName("Integer"));
		System.out.println("mysqlType:"+(System.currentTimeMillis() - begin) + "ms");
		
		Byte[] bytes = new Byte[1024];
		System.out.println(bytes.getClass().getSimpleName());
	}
}

class Type{
	List<String> clazz = new ArrayList<String>();
	String type = "";
}