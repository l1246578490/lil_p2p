package com.dj.p2p.pojo.bid;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("p2p_bid_investment")
public class Investment {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**标ID*/
    private Integer bidId;
    /**用户ID*/
    private Integer userId;
    /**t投资金额*/
    private Double investmentMoney;
    /** 投资人投钱百分比 */
    private Double percentMoney;

}
