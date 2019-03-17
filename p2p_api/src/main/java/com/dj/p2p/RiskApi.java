package com.dj.p2p;

import com.dj.p2p.pojo.bid.Bid;
import com.dj.p2p.pojo.bid.BidVo;
import com.dj.p2p.pojo.bid.BidVoUserVoInt;
import com.dj.p2p.pojo.bid.Query;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface RiskApi {

    /**
     *理财项目展示
     * @return
     * @throws Exception
     */
    @RequestMapping("findBidByState")
    List<BidVo> findBidByQuery(@RequestBody Query query) throws Exception;

    /**
     * 我要出借展示
     * @return
     * @throws Exception
     */
    @RequestMapping("findBidUserIntByQuery")
    List<BidVoUserVoInt> findBidUserIntByQuery() throws Exception;

    /**
     * 查标表
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping("findBidById")
    Bid findBidById(@RequestParam("id") Integer id) throws Exception;

    /**
     * 通过id该Bid
     * @param bid
     */
    @RequestMapping("updateBid")
    void updateBid(Bid bid) throws Exception;

    /**
     *我的出借展示
     * @param query
     * @return
     */
    @RequestMapping("findBidVoUserVoIntByQuery")
    List<BidVoUserVoInt> findBidVoUserVoIntByQuery(Query query) throws Exception;
}
