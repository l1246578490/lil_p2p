package com.dj.p2p;

import com.dj.p2p.pojo.bid.Bill;
import com.dj.p2p.pojo.bid.Investment;
import com.dj.p2p.pojo.user.Balance;
import com.dj.p2p.pojo.user.User;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface UserApi {

    /**
     * 登录验证
     * @param phoneNum
     * @param pwd
     * @return
     */
    @RequestMapping("getOne")
    User getOne(@RequestParam("phoneNum")String phoneNum,@RequestParam("pwd") String pwd)throws Exception;

    /**
     * 查找所有普通用户
     * @return
     */
    @RequestMapping("findSoUser")
    List<User> findSoUser()throws Exception;

    /**
     * 修改用户
     * @param user
     */
    @RequestMapping("updateUser")
    void updateUser(User user);

    /**
     * 通过id查找用户
     * @param id
     * @return
     */
    @RequestMapping("getUserById")
    User getUserById(@RequestParam("id") Integer id);

    /**
     * 查找所有借款人
     * @return
     */
    @RequestMapping("getBorrowerAll")
    List<User> getBorrowerAll();

    /**
     * 查询所有投资人订单
     * @param bidId
     * @return
     */
    @RequestMapping("getInvestmentByBidId")
    List<Investment> getInvestmentByBidId(@RequestParam("bidId") Integer bidId)throws Exception;

    /**
     * 查询对应userId借款人账户
     * @param userIdArr
     * @return
     * @throws Exception
     */
    @RequestMapping("getBalanceByUserIdList")
    List<Balance> getBalanceByUserIdList(@RequestParam("userIdArr") Integer[] userIdArr) throws Exception;

    /**
     * 修改对应借款人账户金额
     * @param balance
     */
    @RequestMapping("updateBalanceByList")
    void updateBalanceByList(Balance balance) throws Exception;

    /**
     * 修改账户
     * @param balance
     */
    @RequestMapping("updateBalance")
    void updateBalance(Balance balance) throws Exception;

    /**
     *批量保存账单
     * @param bill
     * @param bidTerm
     */
    @RequestMapping("saveBatchBill")
    void saveBatchBill(@RequestBody Bill bill,@RequestParam("bidTerm") Integer bidTerm) throws Exception;

    /**
     *根据BidId数组查找投资人
     * @param bidIdArr
     * @return
     * @throws Exception
     */
    @RequestMapping("getInvestmentByBidIdArr")
    List<Investment> getInvestmentByBidIdArr(@RequestParam("bidIdArr") Integer[] bidIdArr) throws Exception;

    /**
     * 根据investmentId数组删除
     * @param investmentIdArr
     * @throws Exception
     */
    @RequestMapping("deleteInvestmentIdArr")
    void deleteInvestmentIdArr(@RequestParam("investmentIdArr") Integer[] investmentIdArr)throws Exception;
}
