package com.renke.core.utils;

public class SysConstant {
	public static final int MAX_SELECT_COUNT = 5000;
	
	public static int STATUS_NORMAL = 1;//正常
	public static int STATUS_DISABLE = -1;//禁用
	
	/**
	 * title: GlobalConstant.java 
	 * 系统事件
	 *
	 * @author rplees
	 * @email rplees.i.ly@gmail.com
	 * @version 1.0  
	 * @created 2015年11月6日 下午1:32:35
	 */
	public final static class SysCmd {
		public static final String PRE_INIT = "preInit";
		public static final String INIT = "init";
		public static final String FISRT_REQUSET = "fisrtRequset";
		public static final String APPLY_SITEMESH_CUSTOM_CONFIGURATION = "applySiteMeshCustomConfiguration";
		
		public static final String CMD_DIC_SYNC = "_cmd_dic_sync_";//字典同步
		public static final String NOTIFY_CACHE = "_notify_cache_";
		
		public static final String CMD_JOB_STOPFORCED = "_cmd_job_stopForced_";
		public static final String CMD_JOB_RESTART = "_cmd_job_reStart_";
	}
}
