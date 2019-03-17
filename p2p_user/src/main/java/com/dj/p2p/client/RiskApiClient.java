package com.dj.p2p.client;

import com.dj.p2p.RiskApi;
import com.dj.p2p.UserApi;
import org.springframework.cloud.netflix.feign.FeignClient;

/**
 * FeignClient中value的值为调用服务的配置文件中的spring.application.name的值
 */
@FeignClient(value = "risk-service",path = "/riskApi/")
public interface RiskApiClient extends RiskApi {

}
