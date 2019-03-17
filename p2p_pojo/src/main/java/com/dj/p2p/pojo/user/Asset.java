package com.dj.p2p.pojo.user;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户资产表
 */
@Data
@TableName("p2p_asset")
public class Asset implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**用户ID**/
    private Integer userId;
    /**工作年限**/
    private String workYear;
    /**房产**/
    private String house;
    /**年收入**/
    private String annualIncome;
    /**资产估值**/
    private Integer assetValuation;
    /**车产**/
    private String carProduction;

}
