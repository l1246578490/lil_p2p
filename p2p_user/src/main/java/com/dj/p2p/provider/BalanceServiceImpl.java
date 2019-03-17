package com.dj.p2p.provider;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dj.p2p.mapper.BalanceMapper;
import com.dj.p2p.pojo.user.Balance;
import com.dj.p2p.service.BalanceService;
import org.springframework.stereotype.Service;

@Service
public class BalanceServiceImpl extends ServiceImpl<BalanceMapper, Balance> implements BalanceService {
}
