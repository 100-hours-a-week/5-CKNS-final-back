package com.example.travelday.global.utils;

import jakarta.servlet.http.Cookie;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CookieUtils {

    /**
     * 쿠키 생성
     *
     * @param cookieName 쿠키명
     * @param value      쿠키값
     * @param maxAge     쿠키 만료 시간
     * @return 생성된 쿠키
     */
    public static Cookie createCookie(final String cookieName, final String value, final int maxAge) {
        Cookie cookie = new Cookie(cookieName, value);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setMaxAge(maxAge);
        cookie.setPath("/");

        return cookie;
    }

    /**
     * 쿠키의 값 조회
     *
     * @param cookies 쿠키 배열
     * @param name    쿠키명
     * @return 쿠키의 값
     */
    public static String getCookieValue(Cookie[] cookies, final String name) {
        if (cookies == null) {
            return null;
        }

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(name)) {
                return cookie.getValue();
            }
        }

        return null;
    }

    /**
     * 쿠키 삭제
     *
     * @param cookieName 쿠키명
     */
    public static Cookie getCookieForRemove(final String cookieName) {
        Cookie cookie = new Cookie(cookieName, null);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setMaxAge(0);
        cookie.setPath("/");

        return cookie;
    }
}
