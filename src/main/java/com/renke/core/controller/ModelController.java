package com.renke.core.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.renke.core.dao.BaseDao;
import com.renke.core.db.JdbcExecutor;
import com.renke.core.entity.Model;
import com.renke.core.service.ModelService;

@Controller
@RequestMapping("/model")
public class ModelController {
	private final static Logger logger = LoggerFactory.getLogger(ModelController.class);
	
	@Resource
	private ModelService modelService;
	@Resource
	private BaseDao<Model> dao;
	
	@RequestMapping(value = "/hello")
	public String helloWorld(){
//		Test test = new Test();
//		Model log = new Model();
		long start = System.currentTimeMillis();
		int length = 100000;
		int threadCount = 20;
		for(int i=1;i<=threadCount;i++){
			Thread thread = new Thread(new InsertThread(length / threadCount, i));
			thread.start();
		}
		
		logger.info(" {} times insert need :{} ms." ,length,(System.currentTimeMillis() - start));
		
//		File head = new File("F:\\resource\\1.jpg");
//		File full = new File("F:\\resource\\2.png");
//		ByteArrayOutputStream baos = new ByteArrayOutputStream();
//		try {
//			FileInputStream fis = new FileInputStream(head);
//			byte[] buf = new byte[8196];
//			int len = 0;
//			while( (len = fis.read(buf)) != -1){
//				baos.write(buf, 0, len);
//			}
//			test.headpic = new Blob(baos.toByteArray(),null);
//			baos.close();
//			fis.close();
//			
//			baos = new ByteArrayOutputStream();
//			fis = new FileInputStream(full);
//			while( (len = fis.read(buf)) != -1){
//				baos.write(buf, 0, len);
//			}
//			test.fullpic = baos.toByteArray();
//			fis.close();
//			baos.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		
//		test.birthday = new Timestamp(System.currentTimeMillis());
//		test.name = "renke";
//		test.sex = 0;
//		
//		dao.insert(test);
		//测试，各种参数情况是否能够正常插入，修改，查询
		//结果。   可以使用，但是前提得处理好文件大小关系
		//建议  还是不要使用clob.blob.binary等
		//Clob,Blob,Binary
		
//		log.setId(109);
//		log.setIp("1231231");
//		log.setPort(1080);
//		
//		modelService.insertModel(log);
//		System.out.println(ToStringTool.entityToString(log));
//		log.setIp("33333");
//		log.setPort(1080);
//		modelService.updateModel(log);

//		log = new Model();
//		log.setIp("33333");
//		List<Model> list = modelService.selectList(log);
//		List<Map<String,Object>> list2 = modelService.selectList2(log);
//		System.out.println(ToStringTool.listToString(list));
//		System.out.println(list.size());
//		System.out.println(ToStringTool.listToString(list2));
//		System.out.println(list2.size());
//		log = modelService.selectModel(log);
//		System.out.println(ToStringTool.entityToString(log));
//		log = modelService.selectModelById(100);
//		System.out.println(ToStringTool.entityToString(log));
		return "index";
	}
	
	@RequestMapping(value="/json")
	public @ResponseBody JsonElement json(){
		JsonArray json = new JsonArray();
		for(int i=0;i<20;i++){
			JsonObject jo = new JsonObject();
			jo.addProperty("id", ""+(i+1));
			jo.addProperty("name", "name_"+(i+1));
			jo.addProperty("price", "$"+i);
			json.add(jo);
		}
		logger.info("I'm json");
		return json;
	}
	
	@RequestMapping(value = "/query")
	public @ResponseBody Model query(){
		Model log = new Model();
		log = modelService.selectModel(log);
		return log;
	}
	
	@RequestMapping("/cache")
	public String myCache(HttpServletRequest request,HttpServletResponse response){
		response.setHeader("Cache-Control", "max-age=3600");
		request.setAttribute("cacheTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis()));
		logger.info("cache");
		return "test/cache";
	}
	
	@RequestMapping("/sqlite")
	public String sqlite(HttpServletRequest request,HttpServletResponse response){
		String hello = request.getParameter("hello");
		Connection c = null;
		PreparedStatement stmt = null;
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:F:/tools/sqlite/springdb.db");
			String sql = "insert into t_menu(id,menu_name) values(?,?)";
			stmt = c.prepareStatement(sql);
			stmt.setInt(1, 1);
			stmt.setString(2, hello);
			stmt.execute();
			stmt.close();
			c.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		request.setAttribute("emoji", hello);
		return "test/emoji";
	}
	
	@RequestMapping("/emoji")
	public String emoji(HttpServletRequest request,HttpServletResponse response){
		Connection c = null;
		Statement stmt = null;
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:F:/tools/sqlite/springdb.db");
			String sql = "select menu_name from t_menu";
			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			rs.next();
//			String id = rs.getString(1);
			String hello = rs.getString(1);
//			rs.next();
//			System.out.println("id-->"+id);
			System.out.println(hello);
			request.setAttribute("emoji", hello);
//			stmt.execute();
			rs.close();
			stmt.close();
			c.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		modelService.printDBProduct();
		return "test/emoji";
	}
	
	
	
	class InsertThread implements Runnable {
		private int times = 0;
		private int length = 1000;
		public InsertThread(int length,int times){
			this.length = length;
			this.times = times;
		}
		public void run() {
			long begin = System.currentTimeMillis();
			JdbcExecutor<Model> je = dao.getMysql();
			String[] sqls = new String[1000];
			for(int i=0;i<length;i++){
				int k = i%1000;
				String ip = length + "." + i%25555;
				sqls[k]= "insert into t_log(ip,port) values('"+ip+"',"+i+")";
				if(i%1000 == 0){
					je.batchUpdate(sqls);
				}
			}
			je.batchUpdate(sqls);
			logger.info(" {} times insert need :{} ms." ,times,(System.currentTimeMillis() - begin));
		}
	}
}
