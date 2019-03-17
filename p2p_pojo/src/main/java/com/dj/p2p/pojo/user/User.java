package com.dj.p2p.pojo.user;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

@Data
@Accessors(chain = true)
@TableName("p2p_user")
public class User implements Serializable {
    /**用户ID**/
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**用户手机号**/
    private String phoneNum;
    /**用户密码**/
    private String pwd;
    /**用户性别**/
    private Integer sex;
    /**用户年龄**/
    private Integer age;
    /**用户学历**/
    private String education;
    /**用户身份证号**/
    private String idCard;
    /**用户真实姓名**/
    private String realName;
    /**用户级别**/
    private Integer level;
    /**确认密码**/
    @TableField(exist = false)
    private String okPwd;
    /** 登陆时间 */
    private Date loginTime;
    /** 登陆次数 */
    private Integer loginSum;
    /** 用户锁定状态 0 未锁定，锁定 */
    private Integer userStatus;
    /** 开户状态 */
    private Integer accountStatus;
    /** 婚姻状态  1已婚,0未婚 */
    private Integer marriage;

}
