/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.fulltl.wemall.modules.wemall.entity;

import com.fulltl.wemall.modules.sys.entity.User;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import java.util.Date;

import com.fulltl.wemall.common.persistence.DataEntity;

/**
 * 积分明细管理Entity
 * @author ldk
 * @version 2018-01-05
 */
public class WemallScoreInfo extends DataEntity<WemallScoreInfo> {
	
	private static final long serialVersionUID = 1L;
	private User user;		// 用户id
	private String fromType;		// 获取途径
	private String type;		// 类型（0--消耗；1--增加）
	private Integer score;		// 分值
	private Integer beginScore;		// 开始 分值
	private Integer endScore;		// 结束 分值
	private Date beginCreateDate;		// 开始 创建时间
	private Date endCreateDate;		// 结束 创建时间
	
	public WemallScoreInfo() {
		super();
	}

	public WemallScoreInfo(String id){
		super(id);
	}

	@NotNull(message="用户id不能为空")
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	@Length(min=1, max=10, message="获取途径长度必须介于 1 和 10 之间")
	public String getFromType() {
		return fromType;
	}

	public void setFromType(String fromType) {
		this.fromType = fromType;
	}
	
	@Length(min=1, max=1, message="类型（0--消耗；1--增加）长度必须介于 1 和 1 之间")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	@NotNull(message="分值不能为空")
	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}
	
	public Integer getBeginScore() {
		return beginScore;
	}

	public void setBeginScore(Integer beginScore) {
		this.beginScore = beginScore;
	}
	
	public Integer getEndScore() {
		return endScore;
	}

	public void setEndScore(Integer endScore) {
		this.endScore = endScore;
	}
		
	public Date getBeginCreateDate() {
		return beginCreateDate;
	}

	public void setBeginCreateDate(Date beginCreateDate) {
		this.beginCreateDate = beginCreateDate;
	}
	
	public Date getEndCreateDate() {
		return endCreateDate;
	}

	public void setEndCreateDate(Date endCreateDate) {
		this.endCreateDate = endCreateDate;
	}
		
}