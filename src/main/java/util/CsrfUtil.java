package util;

import jakarta.servlet.http.HttpSession;
import java.util.UUID;

public class CsrfUtil {

    private static final String TOKEN_KEY = "_csrf";

    /** 세션에 CSRF 토큰이 없으면 생성하고, 항상 현재 토큰을 반환 */
    public static String getOrCreateToken(HttpSession session) {
        String token = (String) session.getAttribute(TOKEN_KEY);
        if (token == null) {
            token = UUID.randomUUID().toString();
            session.setAttribute(TOKEN_KEY, token);
        }
        return token;
    }

    /** 제출된 토큰이 세션의 토큰과 일치하는지 검증 */
    public static boolean isValid(HttpSession session, String submitted) {
        if (session == null || submitted == null) return false;
        String sessionToken = (String) session.getAttribute(TOKEN_KEY);
        return submitted.equals(sessionToken);
    }
}
