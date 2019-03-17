package com.dj.p2p.pojo.bid;

import lombok.Data;

import java.util.Date;

@Data
public class BidVoUserVoInt {
    private Integer id;
    /**项目名称*/
    private String projectName;
    /**关联借款人*/
    private String realName;
    /**订单金额*/
    private Double bidMoney;
    /**年利率*/
    private Integer interestRate;
    /**本息合计*/
    private Double capitalInterestMoney;
    /**利息金额*/
    private Double interestMoney;
    /**订单期限*/
    private Integer bidTerm;
    /**订单状态*/
    private Integer bidState;
    /** 筹款进度 */
    private Double moneyPlan;
    /** 发售时间 */
    private Date saleTime;
    /** 加入人次 */
    private Integer joinSum;
}
