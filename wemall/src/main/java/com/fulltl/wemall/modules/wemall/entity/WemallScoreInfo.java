/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.fulltl.wemall.modules.wemall.entity;

import com.fulltl.wemall.modules.sys.entity.User;
import com.fulltl.wemall.modules.sys.utils.DictUtils;
import com.google.common.collect.Maps;

import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import java.util.Date;
import java.util.Map;

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
	
	public enum ScoreFromType {
		/**
		 * 签到
		 */
		signIn("1"),
		/**
		 * 抽奖
		 */
		lottery("2"),
		/**
		 * 评论
		 */
		comment("3"),
		/**
		 * 购买商品获取
		 */
		buyItems("4"),
		/**
		 * 积分撤回
		 */
		rollback("5"),
		/**
		 * 商品积分抵扣
		 */
		itemScoreDeduction("6"),
		/**
		 * 商品积分兑换
		 */
		itemScoreExchange("7"),
		/**
		 * 新用户获取积分
		 */
		initGetScore("8");
		
		private String value;
		private ScoreFromType(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return value;
		}
		public void setValue(String value) {
			this.value = value;
		}
	}
	
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

	/**
	 * 获取列表接口展示使用的小型数据map
	 * @return
	 */
	public Map<String, Object> getSmallEntityMap() {
		Map<String, Object> map = Maps.newHashMap();
		map.put("id", this.getId());
		map.put("userId", this.getUser().getId());
		map.put("fromType", DictUtils.getDictLabel(this.getFromType(), "score_fromType", ""));
		map.put("type", this.getType());
		map.put("score", this.getScore());
		map.put("createDate", this.getCreateDate());
		super.formatEmptyString(map);
		return map;
	}
		
}