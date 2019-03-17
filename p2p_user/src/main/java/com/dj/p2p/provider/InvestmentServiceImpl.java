package com.dj.p2p.provider;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dj.p2p.mapper.InvestmentMapper;
import com.dj.p2p.pojo.bid.Investment;
import com.dj.p2p.service.InvestmentService;
import org.springframework.stereotype.Service;

@Service
public class InvestmentServiceImpl extends ServiceImpl<InvestmentMapper, Investment> implements InvestmentService {
}
