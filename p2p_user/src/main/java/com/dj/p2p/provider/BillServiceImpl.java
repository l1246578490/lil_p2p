package com.dj.p2p.provider;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dj.p2p.mapper.BillMapper;
import com.dj.p2p.pojo.bid.Bill;
import com.dj.p2p.service.BillService;
import org.springframework.stereotype.Service;

@Service
public class BillServiceImpl extends ServiceImpl<BillMapper, Bill> implements BillService {
}
