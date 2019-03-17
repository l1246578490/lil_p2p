package com.dj.p2p.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dj.p2p.pojo.bid.Bid;
import com.dj.p2p.pojo.bid.BidAndUserVo;
import com.dj.p2p.pojo.bid.BidVo;
import com.dj.p2p.pojo.bid.Query;

import java.util.List;

public interface BidService extends IService<Bid> {

    /**
     *查询风控展示
     * @param level
     * @param id
     * @return
     * @throws Exception
     */
    List<BidVo> findBidAll(Integer level, Integer id)throws Exception;

    /**
     *初审/复审展示
     * @param bidId
     * @return
     * @throws Exception
     */
    BidAndUserVo getBidAndUserByBidId(Integer bidId)throws Exception;

    /**
     *理财项目展示
     * @return
     * @throws Exception
     */
    List<BidVo> findBidByState() throws Exception;

    /**
     * 修改标的保存账单
     * @param bidId
     * @throws Exception
     */
    void updateBidAndSaveBill(Integer bidId) throws Exception;
}
