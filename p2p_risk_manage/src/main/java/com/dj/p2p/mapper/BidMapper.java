package com.dj.p2p.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dj.p2p.pojo.bid.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.dao.DataAccessException;

import java.util.List;

public interface BidMapper extends BaseMapper<Bid> {
    /**
     * 查询风控展示
     * @param level
     * @param id
     * @return
     * @throws DataAccessException
     */
    List<BidVo> findBidAll(@Param("level") Integer level,@Param("id") Integer id) throws DataAccessException;

    /**
     *初审/复审展示
     * @param bidId
     * @return
     * @throws DataAccessException
     */
    BidAndUserVo getBidAndUserByBidId(@Param("bidId") Integer bidId)throws DataAccessException;

    /**
     *理财项目展示
     * @return
     * @throws DataAccessException
     */
    List<BidVo> findBidByState(Query query) throws DataAccessException;

    /**
     * 我要出借展示
     * @return
     * @throws DataAccessException
     */
    List<BidVoUserVoInt> findBidUserIntByQuery()throws DataAccessException;

    /**
     * 我的出借展示
     * @param query
     * @return
     * @throws DataAccessException
     */
    List<BidVoUserVoInt> findBidVoUserVoIntByQuery(Query query)throws DataAccessException;

}
