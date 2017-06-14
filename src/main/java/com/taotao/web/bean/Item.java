package com.taotao.web.bean;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by JARVIS on 2017/6/14.
 */
public class Item extends com.taotao.manage.pojo.Item {

    public String[] getImages() {
        return StringUtils.split(super.getImage(), ",");
    }

}
