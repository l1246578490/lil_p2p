package com.dj.p2p.controller;

import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.dj.p2p.pojo.bid.*;
import com.dj.p2p.pojo.user.Balance;
import com.dj.p2p.utils.ArithDouble;
import com.dj.p2p.common.ResultModel;
import com.dj.p2p.common.UserConstant;
import com.dj.p2p.client.UserApiClient;
import com.dj.p2p.common.UserEnum;
import com.dj.p2p.pojo.user.User;
import com.dj.p2p.redis.RedisService;
import com.dj.p2p.service.BidService;
import com.dj.p2p.utils.DateUtil;
import com.dj.p2p.utils.UUIDUtil;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import sun.misc.UCDecoder;

import java.util.*;

@RestController
@RequestMapping(value = "/risk/",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class RiskController {
    @Autowired
    private UserApiClient userService;
    @Autowired
    private RedisService redisService;
    @Autowired
    private BidService bidService;
    /**
     * 后台登录
     * @param phoneNum
     * @param pwd
     * @return
     */
    @ApiOperation(value="后台登录", notes="后台用户登录,返回token")
    @GetMapping("login")
    public ResultModel login(@RequestParam("phoneNum") String phoneNum, @RequestParam("pwd") String pwd){
        try {
            Assert.notEmpty(phoneNum, "请输入手机号");
            Assert.notEmpty(pwd, "请输入密码");
            User user = userService.getOne(phoneNum,pwd);
            Assert.notNull(user,"账号或密码错误");
            boolean b = UserConstant.USER_LEVEL_JIE.equals(user.getLevel()) || UserConstant.USER_LEVEL_TOU.equals(user.getLevel());
            Assert.isFalse(b,"普通用户无法登陆");
            String token = UUIDUtil.getUUID();
            redisService.set(token,user, UserConstant.TIME_OUT);
            return new ResultModel().success(token,"登陆成功");
        } catch (Exception e) {
            e.printStackTrace();
            Assert.notEmpty(e.getMessage(), "服务器异常， 请重试");
            return new ResultModel().error(null, e.getMessage());
        }
    }

    /**
     *普通用户信息展示
     * @param token
     * @return
     */
    @PostMapping("userMsgShow")
    @ApiOperation(value="普通用户信息展示")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "String",name = "token",value = "token",required = true,paramType = "header")
    })
    public ResultModel userMsgShow(@RequestHeader String token){
        try {
            return new ResultModel().success(userService.findSoUser(),"success");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultModel().error(null,"服务器异常");
        }
    }

    /**
     *修改用户锁定状态
     * @param token
     * @return
     */
    @PostMapping("updateUserStatus")
    @ApiOperation(value="修改用户锁定状态")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "String",name = "token",value = "token",required = true,paramType = "header")
    })
    public ResultModel updateUserStatus(@RequestHeader String token,@RequestParam Integer id){
        try {
            User user = userService.getUserById(id);
            if (user.getUserStatus().equals(UserConstant.ZERO)) {
                user.setUserStatus(UserConstant.USER_LEVEL_JIE);
            } else {
                user.setUserStatus(UserConstant.ZERO);
            }
            userService.updateUser(user);
            return new ResultModel().success(true,"修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultModel().error(null,"服务器异常");
        }
    }
    /**
     * 发标
     * @return
     */
    @ApiOperation(value = "发标")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "String",name = "token",value = "token",required = true,paramType = "header")
    })
    @RequestMapping(value = "addBid", method = RequestMethod.POST)
    public ResultModel addBid(@RequestBody Bid bid, @RequestHeader String token){
        try {
            Assert.notNull(bid.getProductType(),"请选择产品类型");
            Assert.notNull(bid.getIsDisplay(),"请选择是否显示标的");
            Assert.notNull(bid.getBidType(),"请选择标的类型");
            Assert.notNull(bid.getUserId(),"请关联借款人");
            Assert.notNull(bid.getBidMoney(),"请输入金额");
            Assert.notNull(bid.getIsQuota(),"请选择是否限额");
            Assert.notNull(bid.getBidTerm(),"请选择期限");
            Assert.notNull(bid.getRepaymentType(),"请选择还款方式");
            Assert.notEmpty(bid.getProjectName(),"请填写项目名称");
            Assert.notEmpty(bid.getExplains(),"请填写借款说明");
            User user = redisService.get(token);
            Assert.isTrue(UserConstant.USER_LEVEL_FKZY.equals(user.getLevel()),"只能风控专员发标");
            Double x = ArithDouble.div(ArithDouble.mul(ArithDouble.mul(bid.getBidMoney(), bid.getInterestRate()), bid.getBidTerm()), 1200);
            bid.setCommissionerId(user.getId()).setCreateTime(new Date()).setCapitalInterestMoney(ArithDouble.add(x , bid.getBidMoney())).setBidState(UserEnum.BID_STATE_CSZ.getCode()).setCapitalInterestMoney(0.00).setInterestMoney(x);
            bidService.save(bid);
            return new ResultModel().success(true,"发布成功");
        } catch (Exception e) {
            e.printStackTrace();
            Assert.notEmpty(e.getMessage(),"服务器异常");
            return new ResultModel().error(false,e.getMessage());
        }
    }

    @PostMapping("getBorrower")
    @ApiOperation(value="获取借款人信息")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "String",name = "token",value = "token",required = true,paramType = "header")
    })
    public ResultModel getBorrower(@RequestHeader String token){
        try {
            return new ResultModel().success(userService.getBorrowerAll(),"success");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultModel().error(false,"服务器异常");
        }
    }

    @GetMapping("riskManage")
    @ApiOperation("风控展示")
    @ApiImplicitParam(name = "token", dataType = "String", required = true, paramType = "header", value = "token")
    public ResultModel riskManage(@RequestHeader("token") String token){
        try {
            Assert.notEmpty(token,"token不能为空");
            User user = redisService.get(token);
            List<BidVo> bidVoList = bidService.findBidAll(user.getLevel(),user.getId());
            return new ResultModel().success(bidVoList,"success");
        } catch (Exception e) {
            e.printStackTrace();
            Assert.notEmpty(e.getMessage(), "服务器异常");
            return new ResultModel().error(false,e.getMessage());
        }
    }
    @GetMapping("proShow")
    @ApiOperation("理财项目展示")
    @ApiImplicitParam(name = "token", dataType = "String", required = true, paramType = "header", value = "token")
    public ResultModel proShow(@RequestHeader("token") String token){
        try {
            List<BidVo> bidVoList = bidService.findBidByState();
            return new ResultModel().success(bidVoList,"success");
        } catch (Exception e) {
            e.printStackTrace();
            Assert.notEmpty(e.getMessage(), "服务器异常");
            return new ResultModel().error(false,e.getMessage());
        }
    }

    @PostMapping("firstTrialShow")
    @ApiOperation(value = "初审展示")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "String", name = "token", value = "token", required = true, paramType = "header")
    })
    public ResultModel firstTrialShow(@RequestHeader("token") String token, @RequestParam("bidId") Integer bidId) {
        try {
            User user = redisService.get(token);
            Assert.isTrue(user.getLevel().equals(UserConstant.USER_LEVEL_FKJL), "只能经理初审");
            BidAndUserVo bidAndUserVo = bidService.getBidAndUserByBidId(bidId);
            return new  ResultModel().success(bidAndUserVo,"success");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultModel().error(false,e.getMessage());
        }
    }

    @PostMapping("twoCheckShow")
    @ApiOperation(value = "复审展示")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "String", name = "token", value = "token", required = true, paramType = "header")
    })
    public ResultModel twoCheckShow(@RequestHeader("token") String token, @RequestParam("bidId") Integer bidId) {
        try {
            User user = redisService.get(token);
            Bid bid = bidService.getById(bidId);
            Assert.isTrue(UserConstant.USER_LEVEL_FKZJ.equals(user.getLevel()),"只有风控总监可以复审");
            Assert.isTrue(UserEnum.BID_STATE_FSZ.getCode().equals(bid.getBidState()),"风控总监只可以审核状态为复审中订单");
            BidAndUserVo bidAndUserVo = bidService.getBidAndUserByBidId(bidId);
            return new  ResultModel().success(bidAndUserVo,"success");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultModel().error(false,e.getMessage());
        }
    }

    @PostMapping("manageCheck")
    @ApiOperation(value = "经理初审")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "String", name = "token", value = "token", required = true, paramType = "header")
    })
    public ResultModel manageCheck(@RequestHeader("token") String token, @RequestParam("bidId") Integer bidId,@RequestParam("managerIdea") Integer managerIdea) {
        try {
            Assert.notNull(bidId,"发标id不得为空");
            Bid bid = bidService.getById(bidId);
            if(managerIdea.equals(UserConstant.ZERO)){
                bid.setBidState(UserEnum.BID_STATE_FSZ.getCode());
            }else{
                bid.setBidState(UserEnum.BID_STATE_CS_NO.getCode());
            }
            User user = redisService.get(token);
            bid.setManageName(user.getRealName());
            bid.setManageTime(new Date());
            bidService.updateById(bid);
            return new  ResultModel().success(true,"经理初审成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultModel().error(false,e.getMessage());
        }
    }

    @PostMapping("directorCheck")
    @ApiOperation(value = "总监复审")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "String", name = "token", value = "token", required = true, paramType = "header")
    })
    public ResultModel directorCheck(@RequestHeader("token") String token, @RequestBody Bid bid,@RequestParam("directorIdea") Integer directorIdea) {
        try {
            Assert.notNull(bid.getId(),"发标id不得为空");
            Assert.notNull(bid.getEarlyReply(),"提前还款违约金不得为空");
            Assert.notNull(bid.getPenaltyMoney(),"违约金不得为空");
            Assert.notNull(bid.getLateCharge(),"逾期罚息不得为空");
            Assert.notNull(bid.getProceduresWays(),"借款存续期手续费方式不得为空");
            Assert.notNull(bid.getProceduresRate(),"借款存续期手续费不得为空");
            Assert.notNull(bid.getFundraisingTime(),"筹款时间不得为空");
            Assert.notEmpty(bid.getProposal(),"风控建议不得为空");
            Assert.notNull(bid.getContractType(),"请借款合同不得为空");
            if(directorIdea.equals(UserConstant.ZERO)){
                bid.setBidState(UserEnum.BID_STATE_JKZ.getCode());
            }else{
                bid.setBidState(UserEnum.BID_STATE_FS_NO.getCode());
            }
            bid.setSaleTime(new Date());
            bidService.updateById(bid);
            return new  ResultModel().success(true,"总监复审成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultModel().error(false,e.getMessage());
        }
    }

    @PostMapping("backgroundFirseShow")
    @ApiOperation(value = "后台首页")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "String", name = "token", value = "token", required = true, paramType = "header")
    })
    public ResultModel backgroundFirseShow(@RequestHeader("token") String token) {
        try {
            Map<String, Object> map = new HashMap<>();
            List<User> list = userService.findSoUser();
            List<BidVo> list2 = bidService.findBidByState();
            map.put("personNum",list.size());
            map.put("productNum",list2.size());
            return new ResultModel().success(map,"success");
        }catch (Exception e){
            e.printStackTrace();
            return new ResultModel().error(false,e.getMessage());
        }
    }

    @PostMapping("grant")
    @ApiOperation(value = "放款")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "String", name = "token", value = "token", required = true, paramType = "header")
    })
    public ResultModel grant(@RequestHeader String token, @RequestParam("bidId") Integer bidId){
        try {
            Bid bid = bidService.getById(bidId);
            Assert.isTrue(bid.getBidState().equals(UserEnum.BID_STATE_DFK_YQY.getCode()), "状态是已签约待放款，才能放款");
            List<Investment> investmentList = userService.getInvestmentByBidId(bidId);
            List<Integer> userIdList = new ArrayList<>();
            for (Investment investment : investmentList) {
                userIdList.add(investment.getUserId());
            }
            //本息合计
            Double capitalInterestMoney = bid.getCapitalInterestMoney();
            //查询购买该标的所有投资人
            List<Balance> balanceList = userService.getBalanceByUserIdList(userIdList.toArray(new Integer[]{}));
            for (Balance balance : balanceList) {
                for (Investment investment : investmentList) {
                    if (balance.getUserId().equals(investment.getUserId()) ){
                        //修改冻结金额
                        balance.setFreezeMoney(ArithDouble.sub(balance.getFreezeMoney(), investment.getInvestmentMoney()));
                        //修改代收金额
                        balance.setWaitCollectMoney(ArithDouble.add(balance.getWaitCollectMoney(),ArithDouble.div(ArithDouble.mul(capitalInterestMoney,investment.getPercentMoney()),100)));
                    }
                }
            }
            //修改投资人的金额
            userService.updateBalanceByList(new Balance().setBalanceList(balanceList));
            //通过借款人ID查询借款人账户信息
            Balance balance = userService.getBalanceByUserIdList(new Integer[]{bid.getUserId()}).get(0);
            //修改总金额
            balance.setTotalMoney(bid.getBidMoney());
            //修改待还金额
            balance.setWaitReplyMoney(ArithDouble.add(bid.getCapitalInterestMoney(), balance.getWaitReplyMoney()));
            //修改借款人账户信息
            userService.updateBalance(balance);
            //通过Id修改标
            bidService.updateById(new Bid().setId(bidId).setPayTime(new Date()).setBidState(UserEnum.BID_STATE_HKZ.getCode()));
            Integer bidTerm = bid.getBidTerm();
            //每期本金
            Double byMoney = ArithDouble.div(bid.getBidMoney(),bidTerm);
            //每期利息
            Double interestMoney = ArithDouble.div(ArithDouble.mul(bid.getBidMoney(),bid.getInterestRate()),1200);
            List<Bill> bills = new ArrayList<>();
            for (int i = 1; i <= bidTerm ; i++) {
                Bill bill = new Bill();
                bill.setBidTerm(i + "/" + bid.getBidTerm());
                bill.setBidMoney(byMoney);
                bill.setBidId(bidId);
                bill.setUserId(bid.getUserId());
                bill.setExpireTime(DateUtil.addDateMonth(bid.getPayTime(),i));
                bill.setInterestMoney(interestMoney);
                bill.setCapitalInterestMoney(ArithDouble.add(byMoney,interestMoney));
                bill.setBillState(UserConstant.ZERO);
                bills.add(bill);
            }
            userService.saveBatchBill(new Bill().setBillList(bills), bidTerm);
            return new ResultModel().success(true,"放款成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultModel().error(false, e.getMessage());
        }
    }

}
