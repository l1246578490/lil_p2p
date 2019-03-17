package com.dj.p2p.common;


public enum UserEnum {

    /** 初审中 */
    BID_STATE_CSZ("初审中",1),
    /** 复审中 */
    BID_STATE_FSZ("复审中",2),
    /** 初审未通过 */
    BID_STATE_CS_NO("初审未通过",3),
    /** 复审未通过 */
    BID_STATE_FS_NO("复审未通过",4),
    /** 借款中 */
    BID_STATE_JKZ("借款中",5),
    /** 待放款_已签约 */
    BID_STATE_DFK_YQY("待放款_已签约",7),
    /** 待放款_未签约 */
    BID_STATE_DFK_WQY("待放款_未签约",6),
    /** 还款中 */
    BID_STATE_HKZ("还款中",8),
    /** 已完成 */
    BID_STATE_YWC("已完成",9),
    /** 流标 */
    BID_STATE_LB("流标",10);
    private String str;
    private Integer code;
    UserEnum(String str,Integer code){
        this.str = str;
        this.code = code;
    }

    public String getStr() {
        return str;
    }

    public Integer getCode() {
        return code;
    }
}
