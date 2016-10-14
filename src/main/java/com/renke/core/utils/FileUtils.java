package com.renke.core.utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileUtils {
	/**
	 * 将文件读出来转化为字符串
	 * 
	 * @param file
	 *            源文件，不能是文件夹
	 * @return
	 */
	public static String loadFileToString(File file)
			throws FileNotFoundException, IOException {
		BufferedReader br = null;
		// 字符缓冲流，是个装饰流，提高文件读取速度
		br = new BufferedReader(new FileReader(file));
		String fileToString = buffReaderToString(br);
		br.close();
		return fileToString;
	}

	/**
	 * 将文件读出来转化为字符串
	 * 
	 * @param file
	 *            源文件，不能是文件夹
	 * @return
	 * @throws IOException
	 */
	public static String buffReaderToString(BufferedReader br)
			throws IOException {
		StringBuffer sb = new StringBuffer();
		String line = br.readLine();
		while (null != line) {
			sb.append(line);
			line = br.readLine();
		}
		return sb.toString();
	}

	/**
	 * 流拷贝文件
	 * 
	 * @param tempFile
	 * @param newFile
	 * @return
	 * @throws IOException
	 */
	public static long copyFile(File tempFile, File newFile) throws IOException {

		return copyFile(new FileInputStream(tempFile), newFile);
	}

	/**
	 * 流拷贝文件
	 * 
	 * @param tempFile
	 * @param newFile
	 * @return
	 * @throws IOException
	 */
	public static long copyFile(InputStream is, File newFile)
			throws IOException {
		long s = 0;
		OutputStream os = new FileOutputStream(newFile);
		byte[] buf = new byte[4096];
		int len = 0 ;
		while((len = is.read(buf))!=-1){
			os.write(buf, 0, len);
			s += len;
		}
		os.flush();
		os.close();
		is.close();
		return s;
	}

	public static boolean deleteFile(File dir) {
		if (dir.exists()) {
			return dir.delete();
		} else {
			System.out.println("找不到要删除的文件:" + dir.getPath());
			return false;
		}
	}

	/**
	 * 删除该目录下所有文件及文件夹
	 * 
	 * @param dir
	 * @return
	 */
	public static void deleteDir(File... dir) {
		if (null != dir) {
			for (File file : dir) {
				if (null != file && file.isDirectory()) {
					String[] children = file.list();
					if(children == null) {
						continue;
					}
					// 递归删除目录中的子目录下
					for (int i = 0; i < children.length; i++) {
						deleteDir(new File(file, children[i]));
					}
				}
				// 目录此时为空，可以删除
				if(file != null)
					file.delete();
			}

		}
	}

	public static void saveToFile(String fileName, InputStream in)
			throws IOException {
		FileOutputStream fos = null;
		BufferedInputStream bis = null;
		int BUFFER_SIZE = 1024;
		byte[] buf = new byte[BUFFER_SIZE];
		int size = 0;
		bis = new BufferedInputStream(in);
		fos = new FileOutputStream(fileName);
		//
		while ((size = bis.read(buf)) != -1)
			fos.write(buf, 0, size);
		fos.close();
		bis.close();
	}
	
	public static boolean isPathFlagEnd(String path){
		return path.endsWith("/") || path.endsWith("\\");
	}
	
	public static boolean isPathFlagStart(String path){
		return path.startsWith("/") || path.startsWith("\\");
	}
	/**
	 * 
	 * 拼接路径，目的是为了保持路径的完整性, 首尾不会验证
	 * 如 splicePaths("root", "erji", "iii.jpg") == root/erji/iii.jpg
	 * splicePaths("/root/", "/erji/", "/iii.jpg") == /root/erji/iii.jpg
	 * @param path
	 * @return
	 */
	public static String splicePaths(String... path){
		if(CollectionUtils.isEmpty(path)) {
			return "";
		}
		
		String separattor = "/";
		StringBuffer buffer = new StringBuffer(path[0]);
		
		for(int i = 1; i<= path.length - 1; i++) {
			String curr = path[i], pre = path[i - 1];
			boolean slefFlag = isPathFlagStart(curr);//本身取 start
			boolean preFlag = isPathFlagEnd(pre);//前一个取 end
			
			if(slefFlag && preFlag) {//2个都有
				buffer.append(curr.substring(1, curr.length()));
			} else if(!slefFlag && !preFlag) {
				buffer.append(separattor).append(curr);
			} else {
				buffer.append(curr);
			}
		}
		return buffer.toString();
	}
	
	public static void main(String[] args) {
		System.out.println(isPathFlagEnd("llll/"));
		System.out.println(isPathFlagEnd("lll/l"));
		System.out.println(isPathFlagStart("/lll/l"));
		
//		System.out.println(splicePaths("path", "ggg", "ffff.png"));
//		System.out.println(splicePaths("/path/", "/ggg/", "/ffff.png"));
//		System.out.println(splicePaths("/path/", "/ggg/", "/ffff.png/"));
//		System.out.println(splicePaths("/path/", "ggg", "/ffff.png"));
//		System.out.println(splicePaths("/path", "/ggg", "/ffff.png"));
		
		
		System.out.println(splicePaths("\\path", "\\ggg", "ffff.png\\"));
	}
}
