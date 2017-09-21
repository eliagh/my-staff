package com.mycompany.mystaff.service.util;

import org.springframework.util.StringUtils;

public class ResolveTokenUtil {

    private ResolveTokenUtil() {
    }

    public static String resolveToken(String bearerToken) {
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7, bearerToken.length());
        }

        return null;
    }

}
