package com.dj.p2p.pojo.bid;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@TableName("p2p_bid")
@Accessors(chain = true)
public class Bid {
    /**ID*/
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    /**产品类型 0:房产抵押 1:车辆抵押 2:信用借款 3:企业借款*/
    private Integer productType;
    /**项目名称*/
    private String projectName;
    /**是否显示 0:显示  1:不显示*/
    private Integer isDisplay;
    /**订单类型  0:个人标  1:企业标*/
    private Integer bidType;
    /**关联借款人*/
    private Integer userId;
    /**订单金额*/
    private Double bidMoney;
    /**是否限额 0:限额  1:不限额*/
    private Integer isQuota;
    /**订单期限*/
    private Integer bidTerm;
    /**还款方式 0:等额本金 1:等额本息 2:先息后本 3:到期还清*/
    private Integer repaymentType;
    /**借款说明*/
    private String explains;
    /**订单状态*/
    private Integer bidState;
    /**筹款时间  X天*/
    private Integer fundraisingTime;
    /**风控建议*/
    private String proposal;
    /**发售时间*/
    private Date saleTime;
    /**合同类型 0:图通合同 1:四方合同*/
    private Integer contractType;
    /**年利率*/
    private Integer interestRate;
    /**发起时间*/
    private Date createTime;
    /**发表专员*/
    private Integer commissionerId;
    /**利息金额*/
    private Double interestMoney;
    /**本息合计*/
    private Double capitalInterestMoney;
    /**逾期罚息*/
    private Double lateCharge;
    /**提前还钱利率*/
    private Double earlyReply;
    /**违约金*/
    private Double penaltyMoney;
    /**存续期手续费方式  */
    private Integer proceduresWays;
    /**手续费利率*/
    private Double proceduresRate;
    /** 初审经理 */
    private String manageName;
    /** 经理初审时间 */
    private Date manageTime;
    /** 筹款进度 */
    private Double moneyPlan;
    /**放款时间*/
    private Date payTime;


}
