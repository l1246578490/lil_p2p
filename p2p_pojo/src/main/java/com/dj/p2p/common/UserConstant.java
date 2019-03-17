package com.dj.p2p.common;

import com.dj.p2p.pojo.user.Account;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

public interface UserConstant {
    /** 用户Token 失效时间 */
    Integer TIME_OUT = 604800;
    /** 借款人 */
    Integer USER_LEVEL_JIE = 1;
    /** 投资人 */
    Integer USER_LEVEL_TOU = 2;
    /** 风控专员 */
    Integer USER_LEVEL_FKZY = 3;
    /** 风控经理 */
    Integer USER_LEVEL_FKJL = 4;
    /** 风控总监 */
    Integer USER_LEVEL_FKZJ = 5;
    /** 银行卡号length */
    Integer BANK_CARD_LENGTH = 19;
    /** 身份号length */
    Integer ID_CARD_LENGTH = 18;
    /** 手机号length */
    Integer PHONE_LENGTH = 11;
    /** 交易密码length */
    Integer PAY_PWD_LENGTH = 6;
    /** 0 */
    Integer ZERO = 0;
    /** 满标 */
    Double MANBIAO = 100d;

    static Map<String, Object> getMap(String realName, String idCard, String phoneNum, Account account){
        Map map = new HashMap();
        if (!StringUtils.isEmpty(realName) || !StringUtils.isEmpty(idCard)) {
            map.put("realName", realName);
            map.put("idCard", idCard);
            map.put("phoneNum", phoneNum);
            map.put("approveStatus", "已认证");
            map.put("phoneStatus", "已绑定");
        } else {
            map.put("approveStatus", "未认证");
            map.put("phoneStatus", "未绑定");
        }
        if (null != account) {
            map.put("accountStatus", "已开户");
            map.put("boundStatus", "已绑定");
            map.put("sbCarNo", account.getSbCardNo());
            map.put("carNo", account.getCardNo());
        } else {
            map.put("boundStatus", "未绑定");
            map.put("accountStatus", "未开户");
        }
        return map;
    }


}
