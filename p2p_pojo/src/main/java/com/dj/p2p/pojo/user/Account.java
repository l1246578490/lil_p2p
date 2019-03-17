package com.dj.p2p.pojo.user;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.dj.p2p.utils.VerifyCodeUtil;
import lombok.Data;

/**
 * 用户开户信息表
 */
@Data
@TableName("p2p_account")
public class Account {

    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    /**用户ID**/
    private Integer userId;
    /**客户名称**/
    private String clientName;
    /**银行卡号**/
    private String cardNo;
    /**账户类型(投资人 借款人）**/
    private Integer accountType;
    /**银行卡预留手机**/
    private String reservedPhone;
    /**交易密码**/
    private Integer payPwd;
    /**确认交易密码**/
    @TableField(exist = false)
    private Integer okPayPwd;
    /**陕坝银行卡号**/
    private String sbCardNo;
    /**证件号码**/
    private String idCard;
}
