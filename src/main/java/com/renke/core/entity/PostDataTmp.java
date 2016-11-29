package com.renke.core.entity;

/**
 * POST请求，解析数据时的四个标记位
 * 第四个标记不会为true
 * @author renke.zuo@foxmail.com
 * @time 2016-11-28 14:15:11
 */
public class PostDataTmp {
	public static final int MIN_FILE_SIZE = 1024 * 5;
	public short flag = 0;
	public byte[] data = null;
}
