package com.dj.p2p.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dj.p2p.pojo.bid.Bid;
import com.dj.p2p.pojo.bid.Bill;
import com.dj.p2p.pojo.bid.Investment;
import com.dj.p2p.pojo.user.Balance;
import com.dj.p2p.pojo.user.User;

import java.util.List;

public interface UserService extends IService<User> {



    /**
     *修改标的订单
     * @param investment
     * @param balance
     * @param bid
     */
    void updAndSaveBidBalInt(Investment investment, Balance balance, Bid bid)throws Exception;

    /**
     * 还款
     * @param bill
     * @param user
     * @throws Exception
     */
    void updBillAndBal(Bill bill, User user)throws Exception;
}
