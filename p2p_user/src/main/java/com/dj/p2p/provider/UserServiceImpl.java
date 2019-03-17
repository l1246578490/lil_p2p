package com.dj.p2p.provider;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dj.p2p.client.RiskApiClient;
import com.dj.p2p.common.UserEnum;
import com.dj.p2p.pojo.bid.Bill;
import com.dj.p2p.service.BillService;
import com.dj.p2p.utils.ArithDouble;
import com.dj.p2p.common.UserConstant;
import com.dj.p2p.mapper.UserMapper;
import com.dj.p2p.pojo.bid.Bid;
import com.dj.p2p.pojo.bid.Investment;
import com.dj.p2p.pojo.user.Balance;
import com.dj.p2p.pojo.user.User;
import com.dj.p2p.service.BalanceService;
import com.dj.p2p.service.InvestmentService;
import com.dj.p2p.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private BalanceService balanceService;
    @Autowired
    private InvestmentService investmentService;
    @Autowired
    private RiskApiClient riskApiClient;
    @Autowired
    private BillService billService;
    /**
     * 立即购买
     * @param investment
     * @param balance
     * @param bid
     */
    @Override
    public void updAndSaveBidBalInt(Investment investment, Balance balance, Bid bid) throws Exception {
        Double investmentMoney = investment.getInvestmentMoney();
        //计算冻结金额
        balance.setFreezeMoney(ArithDouble.add(balance.getFreezeMoney(),investmentMoney));
        balanceService.updateById(balance);
        investment.setPercentMoney(ArithDouble.div(ArithDouble.mul(investmentMoney,100),bid.getBidMoney(),2));
        //已投资过就修改
        Investment investmentQuery = investmentService.getOne(new QueryWrapper<Investment>().eq("user_id", investment.getUserId()).eq("bid_id", investment.getBidId()));
        if (null != investmentQuery) {
            investmentQuery.setInvestmentMoney(ArithDouble.add(investmentQuery.getInvestmentMoney(),investment.getInvestmentMoney()));
            investmentQuery.setPercentMoney(ArithDouble.add(investmentQuery.getPercentMoney(),investment.getPercentMoney()));
            investmentService.updateById(investmentQuery);
        } else {
            investmentService.save(investment);//添加投资订单表
        }
        //查询标的所有订单
        Double x = 0d;
        List<Investment> list = investmentService.list(new QueryWrapper<Investment>().eq("bid_id", bid.getId()));
        for (Investment investment1 : list) {
            x =  ArithDouble.add(x, investment1.getInvestmentMoney());
        }
        //修改标进度
        bid.setMoneyPlan(ArithDouble.div(ArithDouble.mul(x,100),bid.getBidMoney()));
        if (bid.getMoneyPlan().equals(UserConstant.MANBIAO)) {
            bid.setBidState(UserEnum.BID_STATE_DFK_WQY.getCode());
        }
        riskApiClient.updateBid(bid);
    }

    /**
     * 还款
     * @param bill
     * @param user
     * @throws Exception
     */
    @Override
    public void updBillAndBal(Bill bill, User user) throws Exception {
        Double capitalInterestMoney = bill.getCapitalInterestMoney();//账单本利息
        Balance balance = balanceService.getOne(new QueryWrapper<Balance>().eq("user_id", user.getId()));
        Double totalMoney = balance.getTotalMoney();
        Assert.isTrue(totalMoney > capitalInterestMoney,"您的账户余额不足，无法还款");
        balance.setTotalMoney(ArithDouble.sub(totalMoney,capitalInterestMoney));//借款人改总金额
        balance.setWaitReplyMoney(ArithDouble.sub(balance.getWaitReplyMoney(),capitalInterestMoney));//借款人修改待还金额
        balance.setFreezeMoney(ArithDouble.add(balance.getFreezeMoney(),capitalInterestMoney));//借款人修改冻结金额
        bill.setRepayTime(new Date());
        bill.setBillState(UserConstant.USER_LEVEL_JIE);
        billService.updateById(bill);//改账单
        balanceService.updateById(balance);//改借款人账户
        String bidTerm = bill.getBidTerm();
        String[] split = bidTerm.split("/");
        if (split[0].equals(split[1])) {//满期将借款人冻结款归还投资人
            Double term = Double.valueOf(split[1]);//总期数
            //总本息合计
            Double capitals = ArithDouble.mul(bill.getCapitalInterestMoney(), term);
            //总利息
            Double interestMoney = ArithDouble.mul(bill.getInterestMoney(), term);
            //查询所有投资人订单
            List<Investment> investmentList = investmentService.list(new QueryWrapper<Investment>().eq("bid_id", bill.getBidId()));
            Integer[] userIdArr = new Integer[investmentList.size()];
            for (int i = 0; i < investmentList.size(); i++) {//组装投资人Id
                userIdArr[i] = investmentList.get(i).getUserId();
            }
            //查询投资人账户
            List<Balance> balanceList = balanceService.list(new QueryWrapper<Balance>().in("user_id", userIdArr));
            for (int i = 0; i < balanceList.size(); i++) {//组装数据
                Investment investment = investmentList.get(i);
                Balance balan = balanceList.get(i);
                //投资人占订单百分比
                Double percent = ArithDouble.div(investment.getPercentMoney(), 100);
                //投资人需减去的待收金额
                Double dueInMoney = ArithDouble.mul(capitals,percent);
                balan.setTotalMoney(ArithDouble.add(balan.getTotalMoney(),dueInMoney));//修改总金额
                balan.setWaitCollectMoney(ArithDouble.sub(balan.getWaitCollectMoney(),dueInMoney));//修改待收金额
                balan.setAllProfit(ArithDouble.add(balan.getAllProfit(),ArithDouble.mul(interestMoney,percent)));//修改总收益
            }
            balanceService.updateBatchById(balanceList);//批量改投资人账户
        }
    }
}
