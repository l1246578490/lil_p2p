package com.dj.p2p.pojo.user;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@TableName("p2p_balance")
@Accessors(chain = true)
public class Balance {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**用户ID*/
    private Integer userId;
    /**总金额*/
    private Double totalMoney;
    /**冻结金额*/
    private Double  freezeMoney;
    /**待还金额*/
    private Double waitReplyMoney;
    /**待收金额*/
    private Double waitCollectMoney;
    /**总收益*/
    private Double allProfit;
    /**可用金额*/
    @TableField(exist = false)
    private Double avail;
    /** 账户集合 */
    @TableField(exist = false)
    private List<Balance> balanceList;
}
