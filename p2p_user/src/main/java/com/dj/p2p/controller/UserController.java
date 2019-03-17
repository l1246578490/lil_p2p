package com.dj.p2p.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.dj.p2p.client.RiskApiClient;
import com.dj.p2p.utils.ArithDouble;
import com.dj.p2p.common.ResultModel;
import com.dj.p2p.common.UserConstant;
import com.dj.p2p.common.UserEnum;
import com.dj.p2p.pojo.bid.*;
import com.dj.p2p.pojo.user.*;
import com.dj.p2p.redis.RedisService;
import com.dj.p2p.service.*;
import com.dj.p2p.utils.DateUtil;
import com.dj.p2p.utils.UUIDUtil;
import com.dj.p2p.utils.VerifyCodeUtil;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

@RestController
@RequestMapping(value = "/user/",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private AssetService assetService;
    @Autowired
    private RedisService redisService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private BalanceService balanceService;
    @Autowired
    private RiskApiClient riskApiClient;
    @Autowired
    private InvestmentService investmentService;
    @Autowired
    private BillService billService;

    @ApiOperation(value="登录", notes="用户登录,返回token")
    @GetMapping("login")
    public ResultModel login(@RequestParam("phoneNum") String phoneNum, @RequestParam("pwd") String pwd){
        try {
            Assert.notEmpty(phoneNum, "请输入手机号");
            Assert.notEmpty(pwd, "请输入密码");
            User user = userService.getOne(new QueryWrapper<User>().eq("phone_num", phoneNum).eq("pwd", pwd));
            Assert.notNull(user,"账号或密码错误");
            Assert.isTrue(user.getLevel().equals(UserConstant.USER_LEVEL_JIE) || user.getLevel().equals(UserConstant.USER_LEVEL_TOU),"风控人员无法登录");
            Assert.isTrue(user.getUserStatus().equals(UserConstant.ZERO),"账号已被锁定");
            String token = UUIDUtil.getUUID();
            redisService.set(token,user, UserConstant.TIME_OUT);
            userService.updateById(new User().setId(user.getId()).setLoginSum(user.getLoginSum()+1).setLoginTime(new Date()));
            return new ResultModel().success(token,"登陆成功");
        } catch (Exception e) {
            e.printStackTrace();
            Assert.notEmpty(e.getMessage(), "服务器异常， 请重试");
            return new ResultModel().error(null, e.getMessage());
        }
    }
    @ApiOperation(value="注册", notes="用户注册")
    @PostMapping("add")      //@RequestBody只能有一个，所以合并user,asset
    public ResultModel addUser(@RequestBody UserVoAsset userVoAsset){
        try {
            User user = userVoAsset.getUser();
            Assert.notEmpty(user.getPhoneNum(), "请输入手机号");
            Assert.notEmpty(user.getPwd(), "请输入密码");
            Assert.notEmpty(user.getOkPwd(), "请确认密码");
            Assert.notNull(user.getSex(), "请选择性别");
            Assert.notNull(user.getAge(), "请输入年龄");
            Assert.notEmpty(user.getEducation(), "请选择学历");
            Assert.notEmpty(user.getIdCard(), "请填写身份证号码");
            Assert.notEmpty(user.getRealName(), "请填写真实姓名");
            Assert.notNull(user.getLevel(), "请选择级别");
            Assert.isNull(userService.getOne(new QueryWrapper<User>().eq("phone_num", user.getPhoneNum())), "该手机号已注册");
            userService.save(user);
            if (user.getLevel().equals(UserConstant.USER_LEVEL_JIE) || user.getLevel().equals(UserConstant.USER_LEVEL_TOU)) {
                Asset asset = userVoAsset.getAsset();
                Assert.notEmpty(asset.getWorkYear(), "请选择工作权限");
                Assert.notEmpty(asset.getHouse(), "请选择房产");
                Assert.notEmpty(asset.getAnnualIncome(), "请选择年收入");
                Assert.notNull(asset.getAssetValuation(), "请输入资产估值");
                Assert.notEmpty(asset.getCarProduction(), "请选择车产信息");
                asset.setUserId(user.getId());
                assetService.save(asset);
            }
            return new ResultModel().success(true,"注册成功");
        } catch (Exception e) {
            e.printStackTrace();
            Assert.notEmpty(e.getMessage(), "服务器异常， 请重试");
            return new ResultModel().error(null, e.getMessage());
        }
    }
    @PostMapping("openAnaccount")
    @ApiOperation(value="开户", notes="用户开户")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "String",name = "token",value = "token",required = true,paramType = "header")
    })
    public ResultModel openAnaccount(@RequestHeader String token, @RequestBody Account account){
        try {
            Assert.notEmpty(token, "未传入token");
            User user = redisService.get(token);
            Assert.isNull(accountService.getOne(new QueryWrapper<Account>().eq("user_id", user.getId())), "该用户已开户");
            Assert.notEmpty(account.getClientName(), "请输入客户名称");
            Assert.notEmpty(account.getIdCard(), "请输入证件号码");
            Assert.isTrue(UserConstant.ID_CARD_LENGTH.equals(account.getIdCard().length()),"银行卡号必须为19位");
            Assert.notNull(account.getCardNo(), "请输入银行卡号");
            Assert.isTrue(UserConstant.BANK_CARD_LENGTH.equals(account.getCardNo().length()),"银行卡号必须为19位");
            Assert.notNull(account.getAccountType(), "请输入账户账户类型");
            Assert.notEmpty(account.getReservedPhone(), "请填写银行卡预留手机");
            Assert.isTrue(UserConstant.PHONE_LENGTH.equals(account.getReservedPhone().length()),"手机号必须为11位");
            Assert.notNull(account.getPayPwd(), "请填写交易密码");
            Assert.isTrue(UserConstant.PAY_PWD_LENGTH.equals((""+account.getPayPwd()).length()),"交易密码必须为6位");
            Assert.isTrue(account.getPayPwd().equals(account.getOkPayPwd()),"两次交易密码输入不一致");
            account.setUserId(user.getId());
            user.setAccountStatus(UserConstant.USER_LEVEL_JIE);
            account.setSbCardNo(VerifyCodeUtil.generateTextCode(0,19,null));
            accountService.save(account);
            userService.updateById(user);
            balanceService.save(new Balance().setUserId(user.getId()));
            return new ResultModel().success(true,"开户成功");
        } catch (Exception e) {
            e.printStackTrace();
            Assert.notEmpty(e.getMessage(), "服务器异常");
            return new ResultModel().error(false, e.getMessage());
        }
    }
    @PostMapping("searchRealName")
    @ApiOperation(value="安全信息", notes="安全信息")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "String",name = "token",value = "token",required = true,paramType = "header")
    })
    public ResultModel searchRealName(@RequestHeader String token){
        try {
            Assert.notEmpty(token, "未传入token");
            User user = redisService.get(token);
            Account account = accountService.getOne(new QueryWrapper<Account>().eq("user_id", user.getId()));
            return new ResultModel().success(UserConstant.getMap(user.getRealName(),user.getIdCard(),user.getPhoneNum(),account),"success");
        } catch (Exception e) {
            e.printStackTrace();
            Assert.notEmpty(e.getMessage(), "服务器异常， 请重试");
            return new ResultModel().error(false, e.getMessage());
        }
    }

    /**
     * 账户管理
     * @param token
     * @return
     */
    @ApiOperation(value = "账户管理")
    @GetMapping("accountsManage")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "String",name = "token",value = "token",required = true,paramType = "header")
    })
    public ResultModel accountsManagement(@RequestHeader String token) {
        try {
            Assert.notEmpty(token, "未传入token");
            User user = redisService.get(token);
            Balance balance = balanceService.getOne(new QueryWrapper<Balance>().eq("user_id", user.getId()));
            Assert.notNull(balance,"请先开户");
            /**借款人可用余额*/
            if (user.getLevel().equals(UserConstant.USER_LEVEL_JIE)) {
                balance.setAvail(balance.getTotalMoney() - balance.getWaitReplyMoney());;
            }
            /**投资人可用余额*/
            if (user.getLevel().equals(UserConstant.USER_LEVEL_TOU)) {
                balance.setAvail(balance.getTotalMoney() - balance.getFreezeMoney());;
            }
            return new ResultModel().success(balance,"success");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultModel().error(false, e.getMessage());
        }
    }
    @PostMapping("withdrawMoney")
    @ApiOperation(value = "提现")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "String",name = "token",value = "token",required = true,paramType = "header"),
            @ApiImplicitParam(dataType = "Double",name = "txMoney",value = "提现金额",required = true,paramType = "query"),
            @ApiImplicitParam(dataType = "Integer",name = "payPwd",value = "支付密码",required = true,paramType = "query")
    })
    public ResultModel withdrawMoney(@RequestHeader String token,@RequestParam("txMoney") Double txMoney,@RequestParam("payPwd") String payPwd){
        try {
            Assert.notEmpty(token, "未传入token");
            Assert.notNull(txMoney,"提现金额不得为空");
            Assert.notNull(payPwd,"支付密码不得为空");
            User user = redisService.get(token);
            //查余额
            Balance balance = balanceService.getOne(new QueryWrapper<Balance>().eq("user_id", user.getId()));
            Assert.isFalse(balance.getTotalMoney()<=0,"总金额不足");
            //校验密码
            Account result = accountService.getOne(new QueryWrapper<Account>().eq("user_id", user.getId()).eq("pay_pwd",payPwd));
            Assert.notNull(result,"支付密码错误");
            Assert.isFalse(txMoney < 100,"提现金额不得小于100");
            Assert.isFalse(ArithDouble.sub(balance.getTotalMoney(),balance.getFreezeMoney()) < txMoney,"余额不足无法提现");
            Balance updBalance = new Balance();
            updBalance.setId(balance.getId());
            updBalance.setTotalMoney(ArithDouble.sub(balance.getTotalMoney(),txMoney));
            boolean b = balanceService.updateById(updBalance);
            Assert.isTrue(b,"提现失败,请联系管理员");
            return new ResultModel().success(null,"提现成功");
        } catch (Exception e){
            e.printStackTrace();
            Assert.notEmpty(e.getMessage(), "服务器异常， 请重试");
            return new ResultModel().error(null,e.getMessage());
        }
    }

    /**
     * 充值
     * @param token
     * @return
     */
    @ApiOperation(value = "测试充值")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "String",name = "token",value = "token",required = true,paramType = "header"),
            @ApiImplicitParam(dataType = "Double",name = "payMoney",value = "充值金额",required = true,paramType = "query"),
            @ApiImplicitParam(dataType = "Integer",name = "payPwd",value = "支付密码",required = true,paramType = "query"),
    })
    @PostMapping(value = "pay")
    public ResultModel pay(@RequestHeader String token, @RequestParam("payMoney") Double payMoney, @RequestParam("payPwd") Integer payPwd){
        try {
            if(payMoney < 10){
                return new ResultModel().error(false,"充值金额不得小于10");
            }
            Assert.notNull(payPwd,"请输入支付密码");
            User user = redisService.get(token);
            Account account = accountService.getOne(new QueryWrapper<Account>().eq("user_id", user.getId()));
            if(!payPwd.equals(account.getPayPwd())){
                return new ResultModel().error(false,"支付密码不正确");
            }
            Balance balance = balanceService.getOne(new QueryWrapper<Balance>().eq("user_id", user.getId()));
            balance.setTotalMoney(balance.getTotalMoney()+payMoney);
            balanceService.updateById(balance);
            return new ResultModel().success(true,"充值成功");
        } catch (Exception e) {
            e.printStackTrace();
            Assert.notEmpty(e.getMessage(),"服务器异常");
            return new ResultModel().error(false,e.getMessage());
        }
    }

    /**
     * 充值提现返回页面数据
     * @param token
     * @return
     */
    @ApiOperation(value = "充值,提现,返回页面数据")
    @PostMapping("rechargeReturn")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "String",name = "token",value = "token",required = true,paramType = "header")
    })
    public ResultModel rechargeReturn(@RequestHeader String token){
        try {
            User user = redisService.get(token);
            QueryWrapper<Balance> wrapperBalance = new QueryWrapper<>();
            wrapperBalance.eq("user_id",user.getId());
            Balance balance = balanceService.getOne(wrapperBalance);
            QueryWrapper<Account> wrapperAccount = new QueryWrapper<>();
            wrapperAccount.eq("user_id",user.getId());
            Account account = accountService.getOne(wrapperAccount);
            Double available = 0.0;
            Map<String,Object> map = new HashMap<>();
            if (user.getLevel().equals(UserConstant.USER_LEVEL_TOU)){
                available = balance.getTotalMoney() - balance.getFreezeMoney();
            }
            if (user.getLevel().equals(UserConstant.USER_LEVEL_JIE)){
                available = balance.getTotalMoney()  - balance.getWaitReplyMoney();
            }
            map.put("available",available);
            map.put("idCard",account.getIdCard());
            map.put("bankPhone",account.getReservedPhone());
            map.put("realName",account.getClientName());
            return new ResultModel().success(map,"success");
        }catch (Exception e){
            e.printStackTrace();
            return new ResultModel().error(false,"服务器异常");
        }
    }

    @GetMapping("myBorrow")
    @ApiOperation("我的借款展示")
    @ApiImplicitParam(name = "token", dataType = "String", required = true, paramType = "header", value = "token")
    public ResultModel proShow(@RequestHeader("token") String token){
        try {
            User user = redisService.get(token);
            Assert.isTrue(user.getLevel().equals(UserConstant.USER_LEVEL_JIE),"非借款人无法查看");
            Query query = new Query();
            query.setUserId(user.getId());
            query.setBidState(UserConstant.ZERO);
            List<BidVo> bidVoList = riskApiClient.findBidByQuery(query);
            return new ResultModel().success(bidVoList,"success");
        } catch (Exception e) {
            e.printStackTrace();
            Assert.notEmpty(e.getMessage(), "服务器异常");
            return new ResultModel().error(false,e.getMessage());
        }
    }
    @GetMapping("ICanBeLendShow")
    @ApiOperation("我要出借展示")
    @ApiImplicitParam(name = "token", dataType = "String", required = true, paramType = "header", value = "token")
    public ResultModel ICanBeLendShow(@RequestHeader("token") String token){
        try {
            User user = redisService.get(token);
            Assert.isTrue(user.getLevel().equals(UserConstant.USER_LEVEL_TOU),"非投资人无法查看");
            List<BidVoUserVoInt> bidVoList = riskApiClient.findBidUserIntByQuery();
            return new ResultModel().success(bidVoList,"success");
        } catch (Exception e) {
            e.printStackTrace();
            Assert.notEmpty(e.getMessage(), "服务器异常");
            return new ResultModel().error(false,e.getMessage());
        }
    }

    @GetMapping("buyNowShow")
    @ApiOperation("立即购买展示")
    @ApiImplicitParam(name = "token", dataType = "String", required = true, paramType = "header", value = "token")
    public ResultModel buyNowShow(@RequestHeader("token") String token,@RequestParam("id")Integer id){
        try {
            Map<String,Object> map = new HashMap();
            User user = redisService.get(token);
            Assert.isTrue(user.getLevel().equals(UserConstant.USER_LEVEL_TOU),"非投资人无法查看");
            //查余额
            Balance balance = balanceService.getOne(new QueryWrapper<Balance>().eq("user_id", user.getId()));
            map.put("avail",ArithDouble.sub(balance.getTotalMoney(),balance.getFreezeMoney()));
            //查满标进度
            Bid bidById = riskApiClient.findBidById(id);
            map.put("moneyPlan",bidById.getMoneyPlan());
            //算最多可投
            List<Investment> list = investmentService.list(new QueryWrapper<Investment>().eq("bid_id", id));
            Double bidMoney = bidById.getBidMoney();
            for (Investment investment : list) {
                bidMoney = ArithDouble.sub(bidMoney,investment.getInvestmentMoney());
            }
            map.put("atMostMoney",bidMoney);
            return new ResultModel().success(map,"success");
        } catch (Exception e) {
            e.printStackTrace();
            Assert.notEmpty(e.getMessage(), "服务器异常");
            return new ResultModel().error(false,e.getMessage());
        }
    }

    @GetMapping("MyLendShow")
    @ApiOperation("我的出借展示")
    @ApiImplicitParam(name = "token", dataType = "String", required = true, paramType = "header", value = "token")
    public ResultModel MyLendShow(@RequestHeader("token") String token){
        try {
            User user = redisService.get(token);
            Assert.isTrue(user.getLevel().equals(UserConstant.USER_LEVEL_TOU),"非投资人无法查看");
            List<BidVoUserVoInt> bidByQuery = riskApiClient.findBidVoUserVoIntByQuery(new Query().setUserId(user.getId()).setBidState(UserEnum.BID_STATE_HKZ.getCode()));
            return new ResultModel().success(bidByQuery,"success");
        } catch (Exception e) {
            e.printStackTrace();
            Assert.notEmpty(e.getMessage(), "服务器异常");
            return new ResultModel().error(false,e.getMessage());
        }
    }

    @ApiOperation(value = "立即购买")
    @PostMapping("buyNow")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "String",name = "token",value = "token",required = true,paramType = "header")
    })
    public ResultModel buyNow(@RequestHeader("token") String token, @RequestBody Investment investment,
                              @RequestParam("okPayPwd")Integer okPayPwd){
        try {
            Assert.notNull(investment.getInvestmentMoney(), "请输入购买金额");
            Assert.isTrue(investment.getInvestmentMoney() > 0, "购买金额必须大于0");
            Assert.notNull(okPayPwd, "请输入交易密码");
            User user = redisService.get(token);
            Account account = accountService.getOne(new QueryWrapper<Account>().eq("user_id", user.getId()));
            Assert.notNull(account, "你还没有开户");
            Assert.isTrue(okPayPwd.equals(account.getPayPwd()), "交易密码不正确");
            Balance balance = balanceService.getOne(new QueryWrapper<Balance>().eq("user_id", user.getId()));
            Assert.isTrue(ArithDouble.sub(balance.getTotalMoney(),balance.getFreezeMoney()) >= investment.getInvestmentMoney(), "可用余额不足");
            Bid bid = riskApiClient.findBidById(investment.getBidId());
            Assert.isFalse(bid.getMoneyPlan().equals(UserConstant.MANBIAO), "此标已满");
            //订单最多投入金额
            Double mostInput =ArithDouble.sub(bid.getBidMoney(),ArithDouble.mul(bid.getBidMoney(),ArithDouble.div(bid.getMoneyPlan(),100)));
            if (bid.getMoneyPlan() < 40 && bid.getIsQuota().equals(UserConstant.USER_LEVEL_JIE)){
                mostInput = ArithDouble.mul(bid.getBidMoney(),0.6);
            }
            Assert.isTrue(investment.getInvestmentMoney() <= mostInput,"超过最大限额，无法投资");
            Assert.isTrue( DateUtil.addDateDay(bid.getSaleTime(), bid.getFundraisingTime()).getTime() >= new Date().getTime(), "已流标");
            investment.setUserId(user.getId());
            userService.updAndSaveBidBalInt(investment,balance,bid);
            return new ResultModel().success(true, "购买成功");
        } catch (Exception e) {
            e.printStackTrace();
            Assert.notEmpty(e.getMessage(), "服务器异常，请重试");
            return new ResultModel().error(false, e.getMessage());
        }
    }
    @ApiOperation(value = "签约")
    @PostMapping("signed")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "String",name = "token",value = "token",required = true,paramType = "header")
    })
    public ResultModel signed(@RequestHeader String token, @RequestParam("bidId") Integer bidId){
        try {
            Bid bid = riskApiClient.findBidById(bidId);
            if (bid.getBidState().equals(UserEnum.BID_STATE_DFK_WQY.getCode())) {
                riskApiClient.updateBid(new Bid().setBidState(UserEnum.BID_STATE_DFK_YQY.getCode()).setId(bid.getId()));
                return new ResultModel().success(true, "签约成功");
            }
            return new ResultModel().error(false, "只有待放款才能签约");
        } catch (Exception e) {
            e.printStackTrace();
            Assert.notEmpty(e.getMessage(), "服务器异常");
            return new ResultModel().error(false,e.getMessage());
        }
    }

    @PostMapping(value = "borrowerBill")
    @ApiOperation("借款人账单展示")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", dataType = "String", required = true, paramType = "header", value = "token"),
    })
    public ResultModel borrowerBill(@RequestHeader("token") String token,@RequestParam("id") Integer id){
        try{
            QueryWrapper<Bill> wrapper = new QueryWrapper<>();
            wrapper.eq("bid_id",id);
            List<Bill> list = billService.list(wrapper);
            return new ResultModel().success(list,"success");
        }catch (Exception e){
            e.printStackTrace();
            Assert.notEmpty(e.getMessage(),"服务器异常");
            return new ResultModel().error(false,e.getMessage());
        }
    }

    @PostMapping(value = "investmentBill")
    @ApiOperation("投资人账单展示")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", dataType = "String", required = true, paramType = "header", value = "token"),
    })
    public ResultModel investmentBill(@RequestHeader("token") String token,@RequestParam("id") Integer id){
        try{
            QueryWrapper<Bill> wrapper = new QueryWrapper<>();
            wrapper.eq("bid_id",id);
            List<Bill> list = billService.list(wrapper);
            User user = redisService.get(token);
            QueryWrapper<Investment> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("user_id",user.getId());
            Investment investment = investmentService.getOne(queryWrapper);
            for (Bill bill : list) {
                bill.setBidMoney(bill.getBidMoney()*investment.getPercentMoney());
                bill.setCapitalInterestMoney(bill.getCapitalInterestMoney()*investment.getPercentMoney()/100);
                bill.setInterestMoney(bill.getInterestMoney()*investment.getPercentMoney()/100);
            }
            return new ResultModel().success(list,"success");
        }catch (Exception e){
            e.printStackTrace();
            Assert.notEmpty(e.getMessage(),"服务器异常");
            return new ResultModel().error(false,e.getMessage());
        }
    }
    @PostMapping(value = "refund")
    @ApiOperation("还款")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", dataType = "String", required = true, paramType = "header", value = "token"),
            @ApiImplicitParam(name = "billId", dataType = "Integer", required = true, paramType = "query")
    })
    public ResultModel refund(@RequestHeader String token,@RequestParam("billId")Integer billId){
        try {
            User user = redisService.get(token);
            Bill bill = billService.getById(billId);
            userService.updBillAndBal(bill,user);
            return new ResultModel().success(true,"还款成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultModel().error(false,e.getMessage());
        }
    }

}
