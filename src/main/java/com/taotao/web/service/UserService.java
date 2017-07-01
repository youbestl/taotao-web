package com.taotao.web.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.common.service.ApiService;
import com.taotao.web.bean.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * Created by JARVIS on 2017/6/30.
 */
@Service
public class UserService {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Autowired
    private ApiService apiService;

    @Value("${TAOTAO_SSO_URL}")
    public String TAOTAO_SSO_URL;

    /**
     * 根据token 查询用户信息
     *
     * @param token
     * @return
     */
    public User queryByToken(String token) {
        try {
            String url = TAOTAO_SSO_URL + "/service/user/" + token;
            String jsonData = this.apiService.doGet(url);
            if (StringUtils.isNotEmpty(jsonData)) {
                return MAPPER.readValue(jsonData, User.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
