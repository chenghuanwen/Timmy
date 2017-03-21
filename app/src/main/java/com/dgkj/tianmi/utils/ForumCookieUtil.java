package com.dgkj.tianmi.utils;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

/**
 * Created by Android004 on 2017/2/16.
 */

public class ForumCookieUtil implements CookieJar {
    private final List<Cookie> allCookies = new ArrayList<Cookie>();



    private static List<Cookie> cookies;

    public static List<Cookie> getCookies() {
        return cookies != null ? cookies : new ArrayList<Cookie>();
    }

    public static void setCookies(List<Cookie> cookies) {
        ForumCookieUtil.cookies = cookies;
    }

    public synchronized void saveFromResponse(HttpUrl url, List<Cookie> cookies)
    {
        allCookies.addAll(cookies);
        LogUtil.i("TAG","URL==="+url+"论坛cookies==="+cookies.get(0).toString()+"cookie个数=="+cookies.size()+"所有cookie=="+cookies.toString());
        setCookies(cookies);
    }


    @Override
    public synchronized List<Cookie> loadForRequest(HttpUrl url)
    {
        List<Cookie> result = new ArrayList<Cookie>();
        for (Cookie cookie : allCookies)
        {
            if (cookie.matches(url))
            {
                result.add(cookie);
            }
        }
        return result;
    }
}
