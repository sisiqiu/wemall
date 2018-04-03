/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.fulltl.wemall.modules.wemall.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fulltl.wemall.common.persistence.Page;
import com.fulltl.wemall.common.service.CrudService;
import com.fulltl.wemall.modules.sys.entity.User;
import com.fulltl.wemall.modules.sys.service.SystemService;
import com.fulltl.wemall.modules.sys.utils.OrderDictUtils;
import com.fulltl.wemall.modules.sys.utils.UserUtils;
import com.fulltl.wemall.modules.wemall.dao.WemallScoreInfoDao;
import com.fulltl.wemall.modules.wemall.entity.WemallOrderItem;
import com.fulltl.wemall.modules.wemall.entity.WemallScoreInfo;
import com.fulltl.wemall.modules.wemall.entity.WemallScoreInfo.ScoreFromType;

/**
 * 积分明细管理Service
 * @author ldk
 * @version 2018-01-05
 */
@Service
@Transactional(readOnly = true)
public class WemallScoreInfoService extends CrudService<WemallScoreInfoDao, WemallScoreInfo> {

	@Autowired
	private SystemService systemService;
	
	public WemallScoreInfo get(String id) {
		return super.get(id);
	}
	
	public List<WemallScoreInfo> findList(WemallScoreInfo wemallScoreInfo) {
		return super.findList(wemallScoreInfo);
	}
	
	public Page<WemallScoreInfo> findPage(Page<WemallScoreInfo> page, WemallScoreInfo wemallScoreInfo) {
		return super.findPage(page, wemallScoreInfo);
	}
	
	@Transactional(readOnly = false)
	public void save(WemallScoreInfo wemallScoreInfo) {
		super.save(wemallScoreInfo);
	}
	
	@Transactional(readOnly = false)
	public void delete(WemallScoreInfo wemallScoreInfo) {
		super.delete(wemallScoreInfo);
	}
	
	/**
	 * 校验所用积分数是否超过当前用户剩余积分数
	 * @param score
	 * @return
	 */
	public boolean checkUserScore(Integer score) {
		User user = UserUtils.getUser();
		if(score == null || score.compareTo(user.getCurScoreNum()) < 0) return true;
		else return false;
	}
	
	/**
	 * 对用户的积分执行扣除或增加操作
	 * @param score	根据正负判断是增加，还是消耗
	 * @param fromType
	 */
	@Transactional(readOnly = false)
	public void updateUserScore(String userId, Integer score, ScoreFromType scoreFromType) {
		//更新用户的当前积分数及累计积分数
		User user = UserUtils.get(userId);
		user.setCurScoreNum(user.getCurScoreNum()+score);
		if(!scoreFromType.equals(ScoreFromType.rollback) && score > 0) {
			user.setTotalScoreNum(user.getTotalScoreNum() + score);
		}
		systemService.updateUserInfo(user);
		
		WemallScoreInfo wemallScoreInfo = new WemallScoreInfo();
		wemallScoreInfo.setUser(user);
		wemallScoreInfo.setFromType(scoreFromType.getValue());
		wemallScoreInfo.setScore(score);
		wemallScoreInfo.setType(score > 0 ? "1": "0");
		this.save(wemallScoreInfo);
	}

	/**
	 * 获取最大可抵扣金额（此项需补全实现）
	 * @return
	 */
	public int getTotalDeductPrice(List<WemallOrderItem> wemallOrderItemList) {
		return 100000;
	}
	
	/**
	 * 获取最大可使用的积分总额
	 * @return
	 */
	public int getCanUseTotalScore(List<WemallOrderItem> wemallOrderItemList) {
		return OrderDictUtils.priceToScore(getTotalDeductPrice(wemallOrderItemList));
	}
	
	/**
	 * 校验当前使用积分额对应的抵扣金额  是否超过  商品列表的积分最大抵扣金额
	 * @param wemallOrderItemList
	 * @param deductPrice
	 * @return false--超过，true--没有超过
	 */
	public boolean checkItemsScoreDeductPrice(List<WemallOrderItem> wemallOrderItemList, Integer deductPrice) {
		if(deductPrice <= getTotalDeductPrice(wemallOrderItemList)) {
			return true;
		} else {
			return false;
		}
	}
	
}