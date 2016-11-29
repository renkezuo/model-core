package com.renke.core.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.renke.core.entity.ByteArray;
import com.renke.core.entity.Param;
import com.renke.core.entity.PostDataTmp;
import com.renke.core.enums.Type;
import com.renke.core.tools.CollectionTools;

@Controller
@RequestMapping("/user")
public class UserController{
	@RequestMapping(value="/insert")
	public @ResponseBody JsonElement insert(HttpServletRequest request){
		try {
			InputStream is = request.getInputStream();
			String contentType = request.getContentType();
			System.out.println(contentType);
			byte[] segmentFlag = null;
			int boundaryIndex = contentType.indexOf("boundary");
			if(boundaryIndex>-1){
				segmentFlag = ("--"+contentType.substring(boundaryIndex+1)).getBytes();
			}
			byte[] buf = new byte[512];
			int len = 0;
			List<Param> result = new ArrayList<Param>();
			//临时数据
			PostDataTmp pdt = new PostDataTmp();
			
			
			//每读一次，保存上一次和本次的内容，和段标记做比较，有则分析，没则继续
			while( (len=is.read(buf))!=-1){
				//组装数据
				
				
				
				System.out.println(new String(buf,0,len));
//				if(segmentFlag == null){
//					//第一个\r\n前面的内容为段标记，取出段标记
//					for(int i=0;i<len;i++){
//						byte b = buf[i];
//						if(b=='\r'){
//							if(buf[i+1] == '\n'){
//								segmentFlag = new byte[i];
//								System.arraycopy(buf,0, segmentFlag,0, i);
//								break;
//							}
//						}
//					}
//				}
//				System.arraycopy(buf, 0, data, dataIndex, len);
				//解析数据方法
				//解析数据
				//返回
				
				//用1248表示是否会更好
				//标记是否解析完毕,boolean
				//头部是否解析完毕,boolean
				//空行是否解析完毕,boolean
				//剩余数据,byte[]
				
				
				
				//4块，分别为，1标记，2头部，3空行，4数据
				//1、完整标记，不完整头部
				//2、完整标记，完整头部，不完整数据
				//3、不完整标记
				for(int i=0;i<len;i++){
					//标记头部
//					if(pd.segflag){
//						pd.segflag = false;
//					}
//					for(int s=0;s<segmentFlag.length;s++){
//						if(data[i+s] != segmentFlag[s]){
//							pd.segflag = false;
//							break;
//						}
//						if(s==segmentFlag.length-1){
//							pd.segflag = true;
//						}
//					}
//					
//					if(pd.segflag){
//						i = i+segmentFlag.length+2;
//						for(int k=i;k<len;k++){
//							if(data[k]=='\r'){
//								if(data[k+1]=='\n'){
//									byte[] key = new byte[k-i];
//									System.arraycopy(data, i,key,0,k-i);
//									result.add(key);
//									pd.headerFlag = true;
//									i = k+2;
//								}
//							}
//						}
//						if(pd.headerFlag){
//							if(data[i]=='\r'){
//								if(data[i+1]=='\n'){
//									pd.spaceFlag = true;
//									i = i+1;
//								}
//							}
//						}
//					}
					
					
					
				}
				
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		JsonObject jo = new JsonObject();
		jo.addProperty("msg", "user insert");
		return jo;
	}
	
	
	/***
	 * 逻辑参考uml图
	 * @param pdt
	 * @param array
	 * @param segFlag
	 * @param result
	 * @return
	 * @author renke.zuo@foxmail.com
	 * @time 2016-11-29 15:02:43
	 */
	public static PostDataTmp parseData(PostDataTmp pdt,ByteArray array,byte[] segFlag,List<Param> result){
		int mark = array.mark();
		while(array.position() < array.limit()){
			int pos = array.position();
			boolean move = false;
			//处理分段标记内容和正文内容
			if(pdt.flag == 0 || pdt.flag == 15){
				//如果长度不够，则直接返回
				if(pos+segFlag.length+2 > array.limit()){
					pdt.data = new byte[array.limit()-mark];
					//1k数据
					
					if(pdt.data.length > PostDataTmp.MIN_FILE_SIZE){
						//append To File
						
					}
					System.arraycopy(array.getArray(), pos, pdt.data, 0, array.limit()-mark);
					array.clear();
					break;
				}
				for(int s=0;s<segFlag.length;s++){
					if(array.get(pos+s) != segFlag[s]){
						pdt.flag = 0;
						break;
					}
					if(s==segFlag.length-1){
						if(array.position() != mark && pdt.flag == 15){
							//此处需要调整
							Param param = CollectionTools.getLastE(result);
							if(param.type == Type.BINARY){
								param.value = new String(array.getArray(),mark,array.position()-mark);
							}else{
								param.bytes = new byte[array.position()-mark];
								System.arraycopy(array.getArray(), mark, param.bytes, 0, array.position()-mark);
							}
							pdt.data = null;
						}
						pdt.flag = 1;
						pos = array.moveByN(segFlag.length+2);
						mark = array.mark();
						move = true;
					}
				}
			}
			//分段内容处理完毕，开始处理key信息
			if(pdt.flag == 1){
				if(array.get(pos) == '\r'){
					if(pos+1 >= array.limit()){
						pdt.data = new byte[array.limit()-mark];
						System.arraycopy(array.getArray(), pos, pdt.data, 0, array.limit()-mark);
						array.clear();
						break;
					}
					if(array.get(pos+1) == '\n'){
						String[] params = new String(array.getArray(),mark,pos-mark).split(";");
						if(params.length>=2){
							Param param = new Param();
							param.key = params[1].split("=")[1];
							param.type = Type.BINARY;
							if(params.length==3){
								param.fileName = params[2].split("=")[1];
								param.type = Type.FILE;
							}
							pdt.flag += 2;
							result.add(param);
							pos = array.moveByN(2);
							mark = array.mark();
							move = true;
						}
					}
				}
			}
			//key信息处理完毕，开始处理内容类型信息，或者直接处理为空行
			if(pdt.flag == 3){
				if(array.get(pos) == '\r'){
					if(pos+1 >= array.limit()){
						pdt.data = new byte[array.limit()-mark];
						System.arraycopy(array.getArray(), pos, pdt.data, 0, array.limit()-mark);
						array.clear();
						break;
					}
					if(array.get(pos+1) == '\n'){
						if(mark != pos){
							//param可能为空
							Param param = CollectionTools.getLastE(result);
							String[] params = new String(array.getArray(),mark,pos-mark).split(":");
							//可能出现异常
							param.contentType = params[1].trim();
							pdt.flag += 4;
							pos = array.moveByN(2);
							mark = array.mark();
							move = true;
						}else{
							pdt.flag += 12;
							pos = array.moveByN(2);
							mark = array.mark();
							move = true;
						}
					}
				}
			}
			
			//开始处理空行信息
			if(pdt.flag == 7){
				if(array.get(pos) == '\r'){
					if(pos+1 >= array.limit()){
						pdt.data = new byte[array.limit()-mark];
						System.arraycopy(array.getArray(), pos, pdt.data, 0, array.limit()-mark);
						array.clear();
						break;
					}
					if(array.get(pos+1) == '\n'){
						pdt.flag += 8;
						pos = array.moveByN(2);
						mark = array.mark();
						move = true;
					}
				}
			}
			//开始处理key对应的数据信息
			if(pdt.flag == 15){
				continue;
			}
			if(!move) array.next();
		}
		return null;
	}
	
}
