package com.dj.p2p.provider;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dj.p2p.UserApi;
import com.dj.p2p.common.UserConstant;
import com.dj.p2p.mapper.UserMapper;
import com.dj.p2p.pojo.bid.Bill;
import com.dj.p2p.pojo.bid.Investment;
import com.dj.p2p.pojo.user.Balance;
import com.dj.p2p.pojo.user.User;
import com.dj.p2p.service.BalanceService;
import com.dj.p2p.service.BillService;
import com.dj.p2p.service.InvestmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RequestMapping(value = "/userApi/",produces = {"application/json;charset=UTF-8"})
@RestController
public class UserApiImpl extends ServiceImpl<UserMapper, User> implements UserApi {

    @Autowired
    private InvestmentService investmentService;
    @Autowired
    private BalanceService balanceService;
    @Autowired
    private BillService billService;

    @Override
    public User getOne(@RequestParam("phoneNum") String phoneNum,@RequestParam("pwd") String pwd) {
        return this.getOne(new QueryWrapper<User>().eq("phone_num", phoneNum).eq("pwd", pwd));
    }

    /**
     * 查找所有普通用户
     * @return
     */
    @Override
    public List<User> findSoUser() {
        return this.list(new QueryWrapper<User>().in("level", UserConstant.USER_LEVEL_JIE,UserConstant.USER_LEVEL_TOU));
    }

    /**
     * 修改用户锁定状态
     *
     * @param user
     */
    @Override
    public void updateUser(@RequestBody User user) {
        this.updateById(user);
    }

    /**
     * 通过id查找用户
     *
     * @param id
     * @return
     */
    @Override
    public User getUserById(Integer id) {
        return this.getById(id);
    }

    /**
     * 查找所有借款人
     *
     * @return
     */
    @Override
    public List<User> getBorrowerAll() {
        return this.list(new QueryWrapper<User>().eq("level", UserConstant.USER_LEVEL_JIE).eq("user_starus",UserConstant.ZERO));
    }

    /**
     * 查询所有投资人订单
     *
     * @param bidId
     * @return
     */
    @Override
    public List<Investment> getInvestmentByBidId(Integer bidId) {
        return investmentService.list(new QueryWrapper<Investment>().eq("bid_id",bidId));
    }


    /**
     * 查询对应userId借款人账户
     * @param userIdArr
     * @return
     * @throws Exception
     */
    @Override
    public List<Balance> getBalanceByUserIdList(Integer [] userIdArr) {
        return balanceService.list(new QueryWrapper<Balance>().in("user_id", userIdArr));
    }

    /**
     * 修改对应借款人账户金额
     * @param balance
     */
    @Override
    public void updateBalanceByList(@RequestBody Balance balance) throws Exception {
        balanceService.updateBatchById(balance.getBalanceList());
    }

    /**
     * 修改账户
     * @param balance
     */
    @Override
    public void updateBalance(@RequestBody Balance balance) {
        balanceService.updateById(balance);
    }

    /**
     *批量保存账单
     * @param bill
     * @param bidTerm
     */
    @Override
    public void saveBatchBill(@RequestBody Bill bill, Integer bidTerm) {
        billService.saveBatch(bill.getBillList(), bidTerm);
    }

    /**
     * 根据BidId数组查找投资人
     *
     * @param bidIdArr
     * @return
     * @throws Exception
     */
    @Override
    public List<Investment> getInvestmentByBidIdArr(Integer[] bidIdArr) throws Exception {
        return investmentService.list(new QueryWrapper<Investment>().in("bid_id",bidIdArr));
    }

    /**
     * 根据investmentId数组删除
     *
     * @param investmentIdArr
     * @throws Exception
     */
    @Override
    public void deleteInvestmentIdArr(Integer[] investmentIdArr) throws Exception {
        investmentService.removeByIds(Arrays.asList(investmentIdArr));
    }
}
