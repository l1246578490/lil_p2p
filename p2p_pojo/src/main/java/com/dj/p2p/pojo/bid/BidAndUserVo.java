package com.dj.p2p.pojo.bid;

import lombok.Data;

import java.util.Date;

@Data
public class BidAndUserVo {

    private Integer id;
    /**年利率*/
    private Integer interestRate;
    /**订单期限*/
    private Integer bidTerm;
    /**订单金额*/
    private Double bidMoney;
    /**关联借款人*/
    private String realName;
    /**还款方式 0:等额本金 1:等额本息 2:先息后本 3:到期还清*/
    private Integer repaymentType;
    /**发起时间*/
    private Date createTime;
    /**订单状态*/
    private Integer bidState;
    /**订单状态展示*/
    private String bidStateShow;
    /**用户年龄**/
    private Integer age;
    /**用户学历**/
    private String education;
    /**用户性别**/
    private Integer sex;
    /**婚姻 1：已婚 2：未婚*/
    private Integer marriage;
    /**工作年限**/
    private String workYear;
    /**房产**/
    private String house;
    /**年收入**/
    private String annualIncome;
    /**资产估值**/
    private String assetValuation;
    /**车产**/
    private String carProduction;
    /**借款说明*/
    private String explains;
    /** 初审经理名字 */
    private String manageName;
    /** 经理初审时间 */
    private Date manageTime;
    public String getBidStateShow() {
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
