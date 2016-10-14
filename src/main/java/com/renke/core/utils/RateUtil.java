package com.renke.core.utils;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * title: RateUtil.java 
 * 概率帮助类
 *
 * @author rplees
 * @email rplees.i.ly@gmail.com
 * @version 1.0  
 * @created 2015年6月5日 上午9:57:26
 */
public class RateUtil {
	
	final Logger logger = LoggerFactory.getLogger(getClass());
	static final int DEFAULT_MAX_SIZE = 8;
	
	int maxSize = 0;
	int dataSize = 0;	//实际数量
	
	Object[] 	keys;
	int[] 		r;
	
	public RateUtil() {
		this(DEFAULT_MAX_SIZE);
	}

	public RateUtil(int maxSize) {
		this.maxSize = maxSize;
		keys = new Object[maxSize];
		r = new int[maxSize];
	}
	
	/**
	 * @param key 命中之后的返回值
	 * @param rate 产生的概率
	 */
	public void add(Object key, int rate) {
		if(dataSize >= maxSize) 
			throw new IllegalStateException("the dataSize must be less than maxSize.");
		
		keys[dataSize] = key;
		r[dataSize] = rate;
		
		dataSize ++;
	}
	
	public void removeKey(Object k) {
		for (int i = 0; i < dataSize; i++) {
			if((k instanceof String) ? k.equals(keys[i]) : keys[i] == k) {
				removeIndex(i);
				break;
			}
		}
	}
	
	public void removeIndex(int index) {
		
		if(index < 0 || index + 1 > dataSize) {
			throw new IllegalStateException("the index is rang of dataSize:" + dataSize + ";index:" + index);
		}
		
		Object[] newKeys = new Object[dataSize - 1];
		int[] newR = new int[dataSize - 1];
		
		int sub = 0;
		for (int i = 0; i < dataSize; i++) {
			if(i == index) {
				continue;
			}
			newKeys[sub] = keys[i];
			newR[sub] = r[i];
			
			sub ++;
		}
		
		dataSize --;
		keys = newKeys;
		r = newR;
	}
	/**
	 *  一定找出一个不与k相等的数据
	 * @param k
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> T mustHitOnceWithOutKey(Object k) {
		if(dataSize == 1 && keys[0] == k) {
			throw new IllegalStateException("mustHitOnceWithOutKey only data equals k.");
		}
		T t = mustHitOnce(dataSize, keys, r);
		
		boolean isEquals = (k instanceof String) ? k.equals(t) : k == t;
		return isEquals ? (T) mustHitOnceWithOutKey(k) : t;
	}
	
	/**
	 *  一定会命中一个
	 * @return key
	 */
	public <T> T mustHitOnce() {
		return mustHitOnce(dataSize, keys, r);
	}
	
	/**
	 *  相对与基数随机命中，有可能不命中
	 *  
	 *  先从列表中平均随机到一组数据，在以基准值为base的随机看看中不中
	 * @param base 基数
	 * @return null-没命中 or T
	 */
	@SuppressWarnings("unchecked")
	public <T> T hitWithBaseNum(int base) {
		if (dataSize <= 0) {
			throw new IllegalStateException("mustHitOnce dataSize was zero.");
		}
		
		int randomIndex = new Random().nextInt(dataSize); //随机到的下标
		int rate = r[randomIndex];
		int baseRandom = new Random().nextInt(base + 1);
		
		return rate >= baseRandom ? /**命中*/ (T)keys[randomIndex] : null;
/*		
 * 	以下实现不适用场合
 * int random = new Random().nextInt(base + 1);
		int cloneDataSize = dataSize;
		
		while (cloneDataSize >= 1) {
			int _i = dataSize - cloneDataSize;
			if(r[_i] >= random) {
				Logger.logInfo("hitWithBaseNum>>基数:" + base + ";random:" + random + ";命中:" + keys[_i]);
				return (T)keys[_i];
			}
//			random -= r[_i];
			cloneDataSize --;
		}
		
		Logger.logInfo("hitWithBaseNum>>基数:" + base + ";random:" + random + ";没命中!");
		return null;*/
	}

	@SuppressWarnings("unchecked")
	public <T> T hitWithTotalBaseNum(int base) {
		if (dataSize <= 0) {
			return null;
		}
		
		int nextInt = new Random().nextInt(base + 1);
		
		int nextIntClone = nextInt;
		for (int i = 0; i < dataSize; i++) {
			int rValue = r[i];
			if (nextIntClone <= rValue) {
				logger.debug("mustHitOnce>>基数:" + base + ";nextInt:" + nextInt + ";命中:" + keys[i]);
				return (T) keys[i];
			}

			nextIntClone -= rValue;
		}
		logger.debug("hitWithBaseNum>>基数:" + base + ";random:" + nextInt + ";没命中!");
		return null;
	}
	
	@SuppressWarnings("unchecked")
	private static <T> T mustHitOnce(int dataSize, Object[]  keys,int[] r) {
		if (dataSize <= 0) {
			throw new IllegalStateException("mustHitOnce dataSize was zero.");
		}
		
		if(dataSize == 1) {
			return (T)keys[0];
		}
		
		int base = getBaseRate(dataSize, r);
		Random random = new Random();
		int nextInt = random.nextInt(base + 1);

		int nextIntClone = nextInt;
		for (int i = 0; i < dataSize; i++) {
			int rValue = r[i];
			if (nextIntClone <= rValue) {
//				Logger.logInfo("mustHitOnce>>基数:" + base + ";nextInt:" + nextInt + ";命中:" + keys[i]);
				return (T) keys[i];
			}

			nextIntClone -= rValue;
		}
		throw new IllegalStateException("I am Sorry! mustHitOnce can't found one.");
	}
	
	
	public int getDataSize() {
		return dataSize;
	}

	/**
	 * 获取随机数的基准值
	 * @return
	 */
	private static int getBaseRate(int dataSize, int[] r) {
		int base = 0;
		for (int i = 0; i < dataSize; i++) {
			base += r[i];
		}
		return base;
	}
	
	public static void main(String[] args) {
	/*	List<Integer> l = new ArrayList<Integer>();
		
		for (int i = 0; i < 10; i++) {
			l.add(i);
		}
		
		RateUtil ru = new RateUtil(l.size());
		for (Integer i : l) {
			ru.add("key_" + i, i);
		}
		
		String key = ru.mustHitOnce();
		System.out.println("mustHitOnce 命中了 " + key);
		
		key = ru.mustHitOnceWithOutKey("key_5");
		System.out.println("mustHitOnceWithOutKey 命中了 " + key + ";但绝不是:key_5");
		
		key = ru.hitWithBaseNum(20); //基础值为100 去随机命中,有可能不会命中
		System.out.println("hitWithBaseNum 命中了 " + key );*/
		RateUtil ru = new RateUtil();
		ru.add(1, 2);
		ru.add(2, 2);
		ru.add(3, 2);
		ru.add(4, 4);
		ru.add(5, 1);
		ru.add(6, 1);
		Object mustHitOnce = ru.mustHitOnce();
		
		System.out.println(mustHitOnce);
		
		Object hitWithBaseNum = ru.hitWithBaseNum(10);
		System.out.println(hitWithBaseNum);
		
		Object hitWithTotalBaseNum = ru.hitWithTotalBaseNum(100);
		
		System.out.println(hitWithTotalBaseNum);
	}
}
