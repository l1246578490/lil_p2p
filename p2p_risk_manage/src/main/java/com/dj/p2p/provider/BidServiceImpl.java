package com.dj.p2p.provider;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dj.p2p.UserApi;
import com.dj.p2p.common.UserConstant;
import com.dj.p2p.common.UserEnum;
import com.dj.p2p.mapper.BidMapper;
import com.dj.p2p.pojo.bid.*;
import com.dj.p2p.pojo.user.Balance;
import com.dj.p2p.service.BidService;
import com.dj.p2p.utils.ArithDouble;
import com.dj.p2p.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class BidServiceImpl extends ServiceImpl<BidMapper, Bid> implements BidService {

    @Autowired
    private BidMapper bidMapper;
    @Autowired
    private UserApi userApi;

    @Override
    public List<BidVo> findBidAll(Integer level, Integer id) throws Exception {
        return bidMapper.findBidAll(level,id);
    }

    @Override
    public BidAndUserVo getBidAndUserByBidId(Integer bidId) throws Exception {
        return bidMapper.getBidAndUserByBidId(bidId);
    }

    /**
     * 理财项目展示
     * @return
     * @throws Exception
     */
    @Override
    public List<BidVo> findBidByState() throws Exception {
        Query query = new Query();
        query.setBidState(5);
        return bidMapper.findBidByState(query);
    }

    @Override
    public void updateBidAndSaveBill(Integer bidId) throws Exception {
        Bid bid = this.getById(bidId);
        Assert.isTrue(bid.getBidState().equals(UserEnum.BID_STATE_DFK_YQY), "状态是已签约待放款，才能放款");
        List<Investment> investmentList = userApi.getInvestmentByBidId(bid.getId());
        List<Integer> userIdList = new ArrayList<>();
        for (Investment investment : investmentList) {
            userIdList.add(investment.getUserId());
        }
        //查询购买该标的所有投资人
        List<Balance> balanceList = userApi.getBalanceByUserIdList(userIdList.toArray(new Integer[]{}));
        for (Balance balance : balanceList) {
            for (Investment investment : investmentList) {
                if (balance.getUserId().equals(investment.getUserId()) ){
                    //修改冻金额
                    balance.setFreezeMoney(ArithDouble.sub(balance.getFreezeMoney(), investment.getInvestmentMoney()));
                    //修改代收金额
                    balance.setWaitCollectMoney(ArithDouble.add(balance.getWaitCollectMoney(),investment.getInvestmentMoney()));
                }
            }
        }
        //修改投资人的金额
        userApi.updateBalanceByList(new Balance().setBalanceList(balanceList));
        //通过借款人ID查询借款人账户信息
        Balance balance = userApi.getBalanceByUserIdList(new Integer[]{bid.getUserId()}).get(0);
        //修改总金额
        balance.setTotalMoney(bid.getBidMoney());
        //修改待还金额
        balance.setWaitReplyMoney(ArithDouble.add(bid.getCapitalInterestMoney(), balance.getWaitReplyMoney()));
        //修改借款人账户信息
        userApi.updateBalance(balance);
        //通过Id修改标
        Date date = new Date();
        this.updateById(new Bid().setId(bid.getId()).setPayTime(date).setBidState(UserEnum.BID_STATE_HKZ.getCode()));
        Integer bidTerm = bid.getBidTerm();
        Double byMoney = ArithDouble.div(bid.getBidMoney(), bidTerm);
        Double interestMoney = ArithDouble.div(ArithDouble.mul(bid.getBidMoney(), bid.getInterestRate()),1200,2);
        List<Bill> bills = new ArrayList<>();
        for (int i = 1; i <= bidTerm ; i++) {
            Bill bill = new Bill();
            bill.setBidTerm(i + "/" + bid.getBidTerm());
            bill.setBidMoney(byMoney);
            bill.setBidId(bid.getId());
            bill.setUserId(bid.getUserId());
            bill.setExpireTime(DateUtil.addDateMINUTE(date, i));
            bill.setInterestMoney(interestMoney);
            bill.setCapitalInterestMoney(ArithDouble.add(byMoney, interestMoney));
            bills.add(bill);
        }
        userApi.saveBatchBill(new Bill().setBillList(bills), bidTerm);
    }



    /**  @Scheduled(cron = "0 0 2 1/1 * ?")
     */
    /*@Scheduled(cron = "0 0/1 * * * ?")*/

    /**
     * 定时流标
     */
    @Scheduled(cron = "0 0 2 1/1 * ?")
    public void updateBidCron() {
        try {
            // 获取所有借款中的标
            List<Bid> bidList = this.list(new QueryWrapper<Bid>().eq("bid_state", UserEnum.BID_STATE_JKZ.getCode()));
            Integer[] bidIdArr = new Integer[bidList.size()];
            for (int i = 0; i < bidList.size(); i++) {
                Bid bid = bidList.get(i);
                Date expireTime = DateUtil.addDateDay(bid.getSaleTime(), bid.getFundraisingTime());
                if (DateUtil.compare(expireTime, new Date())) {
                    //修改借款中标状态为 流标
                    bid.setBidState(UserEnum.BID_STATE_LB.getCode());
                    bidIdArr[i] = bid.getId();
                }
            }
            this.updateBatchById(bidList);//批量修改
            //获取所有借款中标 的投资信息
            List<Investment> investmentList = userApi.getInvestmentByBidIdArr(bidIdArr);
            Integer[] userIdArr = new Integer[investmentList.size()];
            for (int i = 0; i < investmentList.size(); i++) {//组装购买标的所有用户ID
                userIdArr[i] = investmentList.get(i).getUserId();
            }
            //获取id集合 用户资产信息
            List<Balance> balanceList = userApi.getBalanceByUserIdList(userIdArr);
            Integer[] investmentIdArr = new Integer[investmentList.size()];
            for (int i = 0; i < balanceList.size(); i++) {
                Balance balance = balanceList.get(i);
                Investment investment = investmentList.get(i);
                investmentIdArr[i] = investment.getId();//组装investment id
                //修改冻结金额
                balance.setFreezeMoney(ArithDouble.sub(balance.getFreezeMoney(), investment.getInvestmentMoney()));
            }
            userApi.updateBalanceByList(new Balance().setBalanceList(balanceList));
            //批量删除购买信息
            userApi.deleteInvestmentIdArr(investmentIdArr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
