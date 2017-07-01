package com.taotao.web.threadLocal;

import com.taotao.web.bean.User;

/**
 * Created by JARVIS on 2017/7/1.
 *
 * 利用线程将用户放入线程
 */

public class UserTreadLocal {

    private static final ThreadLocal<User> LOCAL = new ThreadLocal<>();

    private UserTreadLocal() {
    }

    public static void set(User user) {
        LOCAL.set(user);
    }

    public static User get() {
        return LOCAL.get();
    }
}
