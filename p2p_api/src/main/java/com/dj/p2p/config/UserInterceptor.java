package com.dj.p2p.config;

import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.dj.p2p.common.UserConstant;
import com.dj.p2p.pojo.user.User;
import com.dj.p2p.redis.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
public class UserInterceptor implements HandlerInterceptor {
    @Autowired
    private RedisService redisService;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        String token = request.getHeader("token");
        if(StringUtils.isEmpty(token)){
            return false;
        }
        User user = redisService.get(token);
        if(null == user){
            return false;
        }
        Assert.isTrue(user.getLevel().equals(UserConstant.USER_LEVEL_JIE) || user.getLevel().equals(UserConstant.USER_LEVEL_TOU),"非普通用户无法登陆");
        redisService.set(token,user, UserConstant.TIME_OUT);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
