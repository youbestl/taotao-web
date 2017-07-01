package com.taotao.web.controller;

import com.taotao.web.bean.Item;
import com.taotao.web.bean.Order;
import com.taotao.web.bean.User;
import com.taotao.web.interceptors.UserLoginHandlerInterceptor;
import com.taotao.web.service.ItemService;
import com.taotao.web.service.OrderService;
import com.taotao.web.service.UserService;
import com.taotao.web.threadLocal.UserTreadLocal;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by JARVIS on 2017/6/23.
 */
@Controller
@RequestMapping("order")
public class OrderController {

    @Autowired
    private ItemService itemService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    /**
     * 去订单确认页
     * @param itemId
     * @return
     */
    @RequestMapping(value = "{itemId}",method = RequestMethod.GET)
    public ModelAndView toOrder(@PathVariable("itemId")Long itemId) {
        ModelAndView mv = new ModelAndView("order");
        Item item = this.itemService.queryById(itemId);
        mv.addObject("item", item);
        return mv;
    }

    @RequestMapping(value = "submit",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> submit(Order order) {
        Map<String, Object> result = new HashMap<>();
        String OrderId = this.orderService.submit(order);
        if (StringUtils.isEmpty(OrderId)) {
            result.put("status", 500);
            return result;
        } else {
            result.put("status", 200);
            result.put("data", OrderId);
            return result;
        }
    }

    /**
     * 根据订单编号查询订单信息
     * @param orderId
     * @return
     */
    @RequestMapping(value = "success",method = RequestMethod.GET)
    public ModelAndView success(@RequestParam("id")String orderId) {
        ModelAndView mv = new ModelAndView("success");
        //订单数据
        Order order = this.orderService.queryOrderById(orderId);
        mv.addObject("order", order);
        //送货时间 当前时间向后退2天
        mv.addObject("date", new DateTime().plusDays(2).toString("MM月dd日"));
        return mv;
    }

}
