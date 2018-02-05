/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.fulltl.wemall.common.utils;

import java.io.Serializable;
import java.security.SecureRandom;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import org.activiti.engine.impl.cfg.IdGenerator;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.SessionIdGenerator;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.google.common.collect.Sets;
import com.fulltl.wemall.modules.sys.service.SlSysOrderService;

/**
 * 封装各种生成唯一性ID算法的工具类.
 * @author ThinkGem
 * @version 2013-01-15
 */
@Service
@Lazy(false)
public class IdGen implements IdGenerator, SessionIdGenerator {

	private static SecureRandom random = new SecureRandom();
	
	/**
	 * 封装JDK自带的UUID, 通过Random数字生成, 中间无-分割.
	 */
	public static String uuid() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}
	
	/**
	 * 使用SecureRandom随机生成Long. 
	 */
	public static long randomLong() {
		return Math.abs(random.nextLong());
	}

	/**
	 * 基于Base62编码的SecureRandom随机生成bytes.
	 */
	public static String randomBase62(int length) {
		byte[] randomBytes = new byte[length];
		random.nextBytes(randomBytes);
		return Encodes.encodeBase62(randomBytes);
	}
	
	/**
	 * Activiti ID 生成
	 */
	@Override
	public String getNextId() {
		return IdGen.uuid();
	}

	@Override
	public Serializable generateId(Session session) {
		return IdGen.uuid();
	}
	
	private static AtomicInteger orderInt = new AtomicInteger(0);
	private static String curMillis = StringUtils.EMPTY;
	private static final String MACHINE_ID = "";
	private static final String REFUND_PREFIX = "tk";
	
	/**
	 * 生成订单号
	 * @return
	 */
	public synchronized static String generateOrderNo() {
		long millis = System.currentTimeMillis();
		int tempInt = 0;
		String intStr = StringUtils.EMPTY;
		
		if(Long.toString(millis).equals(curMillis)) {
			tempInt = orderInt.getAndIncrement();
		} else {
			curMillis = Long.toString(millis);
			orderInt.set(0);
			tempInt = orderInt.getAndIncrement();
		}
		
		if(tempInt < 10) {
			intStr = "000" + tempInt;
		} else if(tempInt < 100) {
			intStr = "00" + tempInt;
		} else if(tempInt < 1000) {
			intStr = "0" + tempInt;
		} else {
			intStr = "" + tempInt;
		}
		
		String orderNo= MACHINE_ID + millis + intStr;
		return orderNo;
	}
	
	/**
	 * 生成退款业务号
	 * @return
	 */
	public static String generateRefundId() {
		String refundId = REFUND_PREFIX + generateOrderNo();
		return refundId;
	}
	
	public static void main(String[] args) {
		Set<String> set1 = Sets.newConcurrentHashSet();
		long l1 = System.currentTimeMillis();
		Thread t1 = new Thread(new Runnable() {
			@Override
			public void run() {
				for(int i=0; i<1000; i++) {
					String orderNo = generateRefundId();
					set1.add(orderNo);
					//System.err.println("线程1：" + orderNo);
				}
				System.err.println("大小=" + set1.size());
				System.out.println(System.currentTimeMillis() - l1);
			}
		});
		Thread t2 = new Thread(new Runnable() {
			@Override
			public void run() {
				for(int i=0; i<1000; i++) {
					String orderNo = generateRefundId();
					set1.add(orderNo);
					//System.err.println("线程2：" + orderNo);
				}
				System.err.println("大小=" + set1.size());
				System.out.println(System.currentTimeMillis() - l1);
			}
		});
		t1.start();
		t2.start();
	}
	
	/*public static void main(String[] args) {
		System.out.println(IdGen.uuid());
		System.out.println(IdGen.uuid().length());
		System.out.println(new IdGen().getNextId());
		for (int i=0; i<1000; i++){
			System.out.println(IdGen.randomLong() + "  " + IdGen.randomBase62(5));
		}
	}*/

}
