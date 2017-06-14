package com.taotao.web.controller;

import com.taotao.manage.pojo.ItemDesc;
import com.taotao.manage.pojo.ItemParamItem;
import com.taotao.web.bean.Item;
import com.taotao.web.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by JARVIS on 2017/6/14.
 */
@Controller
@RequestMapping("item")
public class ItemController {
    @Autowired
    private ItemService itemService;

    /**
     * 根据itemId查询商品详情
     * @param itemId
     * @return
     */
    @RequestMapping(value = "{itemId}",method = RequestMethod.GET)
    public ModelAndView queryListByItemId(@PathVariable("itemId")Long itemId){
        ModelAndView mv = new ModelAndView("item");
        //商品详情
        Item item = this.itemService.queryById(itemId);

        //商品描述
        ItemDesc itemDesc = this.itemService.queryDescByItemId(itemId);

        //商品规格参数
        String html = this.itemService.queryItemParamItemByItemId(itemId);

        mv.addObject("item", item);
        mv.addObject("itemDesc", itemDesc);
        mv.addObject("itemParam", html);
        return mv;
    }
}
