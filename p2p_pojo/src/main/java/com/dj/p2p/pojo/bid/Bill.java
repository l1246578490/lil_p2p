package com.dj.p2p.pojo.bid;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@TableName("p2p_bill")
@Accessors(chain = true)
public class Bill implements Serializable {
    /**ID*/
    @TableId(value = "id" ,type = IdType.AUTO)
    private Integer id;
    /** 标的Id */
    private Integer bidId;
    /** 还款期限 */
    private String bidTerm;
    /** 利息金额 */
    private Double interestMoney;
    /** 本息合计 */
    private Double capitalInterestMoney;
    /** 借款本金 */
    private Double bidMoney;
    /** 到期时间 */
    private Date expireTime;
    /** 还款之后时间 */
    private Date repayTime;
    /** 还款状态 0:未还  1:已还 */
    private Integer billState;
    /** 借款人id */
    private Integer userId;
    /** 订单集合 */
    @TableField(exist = false)
    private List<Bill> billList;

}
