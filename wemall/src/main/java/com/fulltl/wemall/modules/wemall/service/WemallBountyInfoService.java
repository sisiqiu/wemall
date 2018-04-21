/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.fulltl.wemall.modules.wemall.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fulltl.wemall.common.persistence.Page;
import com.fulltl.wemall.common.service.CrudService;
import com.fulltl.wemall.modules.sys.entity.User;
import com.fulltl.wemall.modules.sys.service.SystemService;
import com.fulltl.wemall.modules.sys.utils.UserUtils;
import com.fulltl.wemall.modules.wemall.dao.WemallBountyInfoDao;
import com.fulltl.wemall.modules.wemall.entity.WemallBountyInfo;
import com.fulltl.wemall.modules.wemall.entity.WemallBountyInfo.BountyFromType;
import com.fulltl.wemall.modules.wemall.entity.WemallScoreInfo;

/**
 * 奖励金管理Service
 * @author ldk
 * @version 2018-01-05
 */
@Service
@Transactional(readOnly = true)
public class WemallBountyInfoService extends CrudService<WemallBountyInfoDao, WemallBountyInfo> {

	@Autowired
	private SystemService systemService;
	
	public WemallBountyInfo get(String id) {
		return super.get(id);
	}
	
	public List<WemallBountyInfo> findList(WemallBountyInfo wemallBountyInfo) {
		return super.findList(wemallBountyInfo);
	}
	
	public Page<WemallBountyInfo> findPage(Page<WemallBountyInfo> page, WemallBountyInfo wemallBountyInfo) {
		return super.findPage(page, wemallBountyInfo);
	}
	
	@Transactional(readOnly = false)
	public void save(WemallBountyInfo wemallBountyInfo) {
		super.save(wemallBountyInfo);
	}
	
	@Transactional(readOnly = false)
	public void delete(WemallBountyInfo wemallBountyInfo) {
		super.delete(wemallBountyInfo);
	}
	
	/**
	 * 对用户的积分执行扣除或增加操作
	 * @param score	根据正负判断是增加，还是消耗
	 * @param fromType
	 */
	@Transactional(readOnly = false)
	public void updateUserBounty(String userId, Integer score, BountyFromType bountyFromType) {
		//更新用户的当前奖励金数及累计奖励金数
		User user = UserUtils.get(userId);
		user.setCurBountyNum(user.getCurBountyNum()+score);
		logger.info("updateUserBounty: " + score + "----------" + user.getCurBountyNum());
		if(!bountyFromType.equals(BountyFromType.rollback) && score > 0) {
			user.setTotalBountyNum(user.getTotalBountyNum() + score);
		}
		systemService.updateUserInfo(user);
		
		WemallBountyInfo wemallBountyInfo = new WemallBountyInfo();
		wemallBountyInfo.setUser(user);
		wemallBountyInfo.setFromType(bountyFromType.getValue());
		wemallBountyInfo.setPrice(score);
		wemallBountyInfo.setType(score > 0 ? "1": "0");
		this.save(wemallBountyInfo);
	}

	/**
	 * 校验当前用户奖励金数是否足够
	 * @param bountyUsageNum
	 * @return
	 */
	public boolean checkUserBounty(Integer bountyUsageNum) {
		User user = UserUtils.getUser();
		if(bountyUsageNum == null || bountyUsageNum.compareTo(user.getCurBountyNum()) <= 0) return true;
		else return false;
	}
}