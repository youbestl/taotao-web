package com.taotao.web.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.taotao.common.service.ApiService;
import com.taotao.common.service.RedisService;
import com.taotao.manage.pojo.ItemDesc;
import com.taotao.manage.pojo.ItemParamItem;
import com.taotao.web.bean.Item;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * Created by JARVIS on 2017/6/14.
 */
@Service
public class ItemService {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public static final String REDIS_KEY ="TAOTAO_WEB_ITEM_DETAIL";

    private static final int REDIS_TIME = 60 * 60 * 60 * 24 * 30 * 3;



    @Autowired
    private ApiService apiService;

    @Autowired
    private RedisService redisService;

    @Value("${TAOTAO_MANAGE_URL}")
    private String TAOTAO_MANAGE_URL;

    /**
     * 根据商品id 查询商品详情
     * 通过后台服务提供的结构提供查询
     * @param itemId
     * @return
     */
    public Item queryById(Long itemId) {

        String url = TAOTAO_MANAGE_URL+"/rest/api/item/"+itemId;
        String key = REDIS_KEY + itemId;

        try {
            String cacheData = this.redisService.get(key);
            if(StringUtils.isNotEmpty(cacheData)){
                return MAPPER.readValue(cacheData, Item.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            String jsonData = this.apiService.doGet(url);
            if(StringUtils.isEmpty(jsonData)){
                return null;
            }
            try {
                this.redisService.set(key,jsonData,REDIS_TIME);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return MAPPER.readValue(jsonData, Item.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ItemDesc queryDescByItemId(Long itemId) {
        String url = TAOTAO_MANAGE_URL+"/rest/api/item/desc/"+itemId;
        try {
            String jsonData = this.apiService.doGet(url);
            if(StringUtils.isEmpty(jsonData)){
                return null;
            }
            return MAPPER.readValue(jsonData, ItemDesc.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String queryItemParamItemByItemId(Long itemId) {
        String url = TAOTAO_MANAGE_URL+"/rest/api/item/param/item/"+itemId;
        try {
            String jsonData = this.apiService.doGet(url);
            if(StringUtils.isEmpty(jsonData)){
                return null;
            }
            ItemParamItem itemParamItem = MAPPER.readValue(jsonData, ItemParamItem.class);
            String paramData = itemParamItem.getParamData();
            ArrayNode arrayNode = (ArrayNode) MAPPER.readTree(paramData);

            StringBuilder sb = new StringBuilder();
            sb.append("<table cellpadding=\"0\" cellspacing=\"1\" width=\"100%\" border=\"0\" class=\"Ptable\"><tbody>");

            for (JsonNode param : arrayNode) {
                sb.append("<tr><th class=\"tdTitle\" colspan=\"2\">" + param.get("group").asText()
                        + "</th></tr>");
                ArrayNode params = (ArrayNode) param.get("params");
                for (JsonNode p : params) {
                    sb.append("<tr><td class=\"tdTitle\">" + p.get("k").asText() + "</td><td>"
                            + p.get("v").asText() + "</td></tr>");
                }
            }

            sb.append("</tbody></table>");
            return sb.toString();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
