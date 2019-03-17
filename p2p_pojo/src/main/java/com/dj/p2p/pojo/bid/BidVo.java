package com.dj.p2p.pojo.bid;

import lombok.Data;

import java.util.Date;

@Data
public class BidVo {
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
    public String getBidStateShow() {
        String bidStateShow = "";
        if (bidState.equals(1)) {
            bidStateShow = "初审中";
        }
        if (bidState.equals(2)) {
            bidStateShow = "复审中";
        }
        if (bidState.equals(3)) {
            bidStateShow = "初审未通过";
        }
        if (bidState.equals(4)) {
            bidStateShow = "复审未通过";
        }
        if (bidState.equals(5)) {
            bidStateShow = "借款中";
        }
        if (bidState.equals(6)) {
            bidStateShow = "待放款";
        }
        if (bidState.equals(7)) {
            bidStateShow = "还款中";
        }
        if (bidState.equals(8)) {
            bidStateShow = "已完成";
        }
        if (bidState.equals(9)) {
            bidStateShow = "流标";
        }
        return bidStateShow;
    }
}
