package io.sld.riskcomplianceloginservice.gateway.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient("teste-eureke-client")
public interface LoginClient {

    @RequestMapping("/greeting")
    String login();
}
