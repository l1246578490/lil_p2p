package com.dj.p2p.pojo.bid;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Query {

    private Integer bidId;
    private Integer userId;
    private Integer bidState;
    private Integer userLevel;

}
