package com.dj.p2p.provider;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dj.p2p.RiskApi;
import com.dj.p2p.common.UserConstant;
import com.dj.p2p.mapper.BidMapper;
import com.dj.p2p.pojo.bid.Bid;
import com.dj.p2p.pojo.bid.BidVo;
import com.dj.p2p.pojo.bid.BidVoUserVoInt;
import com.dj.p2p.pojo.bid.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping(value = "/riskApi/",produces = {"application/json;charset=UTF-8"})
@RestController
public class RiskApiImpl extends ServiceImpl<BidMapper, Bid> implements RiskApi {
    @Autowired
    private BidMapper bidMapper;

    /**
     * 理财项目展示
     * @return
     * @throws Exception
     */
    @Override
    public List<BidVo> findBidByQuery(@RequestBody Query query) throws Exception {
        return bidMapper.findBidByState(query);
    }

    /**
     * 我要出借展示
     * @param query
     * @return
     * @throws Exception
     */
    @Override
    public List<BidVoUserVoInt> findBidUserIntByQuery() throws Exception {
        return bidMapper.findBidUserIntByQuery();
    }

    /**
     * 查标表
     *
     * @param id
     * @return
     * @throws Exception
     */
    @Override
    public Bid findBidById(Integer id) throws Exception {
        return this.getById(id);
    }

    /**
     * 通过id该Bid
     *
     * @param bid
     */
    @Override
    public void updateBid(@RequestBody Bid bid)throws Exception {
        this.updateById(bid);
    }

    /**
     * 我的出借展示
     *
     * @param query
     * @return
     */
    @Override
    public List<BidVoUserVoInt> findBidVoUserVoIntByQuery(@RequestBody Query query)throws Exception {
        return bidMapper.findBidVoUserVoIntByQuery(query);
    }

}
