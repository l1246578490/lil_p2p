package com.dj.p2p.provider;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dj.p2p.mapper.AssetMapper;
import com.dj.p2p.pojo.user.Asset;
import com.dj.p2p.service.AssetService;
import org.springframework.stereotype.Service;

@Service
public class AssetServiceImpl extends ServiceImpl<AssetMapper, Asset> implements AssetService {
}
