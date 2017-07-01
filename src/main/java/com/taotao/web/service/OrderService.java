package com.taotao.web.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.common.httpclient.HttpResult;
import com.taotao.common.service.ApiService;
import com.taotao.web.bean.Order;
import com.taotao.web.bean.User;
import com.taotao.web.threadLocal.UserTreadLocal;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;

/**
 * Created by JARVIS on 2017/7/1.
 */
@Service
public class OrderService {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Autowired
    private ApiService apiService;

    @Value("${TAOTAO_ORDER_URL}")
    private String TAOTAO_ORDER_URL;

    public String submit(Order order) {
        String url = TAOTAO_ORDER_URL + "/order/create";
        try {
            //填充用户信息
            User user = UserTreadLocal.get(); //从本地线程中拿到用户信息，解决查询token二次
            //User user = userService.queryByToken(token);
            order.setUserId(user.getId());
            order.setBuyerNick(user.getUsername());
            HttpResult httpResult = this.apiService.doPostJson(url, MAPPER.writeValueAsString(order));
            if (httpResult.getCode().intValue() == 200) {
                String body = httpResult.getBody();
                JsonNode jsonNode = MAPPER.readTree(body);
                if (jsonNode.get("status").asInt() == 200) {
                    //提交成功
                    return jsonNode.get("data").asText();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Order queryOrderById(String orderId) {
        try {
            String url = TAOTAO_ORDER_URL + "/order/query/" + orderId;
            String jsonData = this.apiService.doGet(url);
            if (StringUtils.isNotEmpty(jsonData)) {
                return MAPPER.readValue(jsonData, Order.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
