package com.moggi.quizmini.framework.config;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

public class MyLocaleResolver implements LocaleResolver {

    /**
     * i18n配置
     * https://www.cnblogs.com/moluu/articles/14187084.html
     * <p>
     * cookie 用户选项持久化
     * https://blog.csdn.net/yeyinglingfeng/article/details/82256176
     *
     * @param request
     * @return
     */
    @Override
    public Locale resolveLocale(HttpServletRequest request) {
        // 获取 请求（a标签）中携带的地区参数。
        String langByRequest = request.getParameter("lang");

        // 如果不为空地区参数为获取的地区参数
        if (StringUtils.isNotBlank(langByRequest)) {
            return getLocale(langByRequest);
        }

        // 如果请求中携带的地区参数为空
        Cookie[] cookies = request.getCookies(); // 获取cookie数组
        if (cookies != null && cookies.length > 0) {
            String langByCookie = null;
            for (Cookie cookie : cookies) { // 遍历cookie数组
                if ("ClientLanguage".equals(cookie.getName())) {
                    langByCookie = cookie.getValue();
                    break;
                }
            }

            if (StringUtils.isNotBlank(langByCookie)) {
                return getLocale(langByCookie);
            }
        }

        // 获取默认的地区参数
        return Locale.ITALY;
    }

    /**
     * @param language eg: zh_CN
     * @return
     */
    private Locale getLocale(String language) {
        String[] split = language.split("_");
        Locale locale = new Locale(split[0], split[1]);
        return locale;
    }

    // 这个不写
    @Override
    public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale) {
    }
}
