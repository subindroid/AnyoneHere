package controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import util.CsrfUtil;

import java.io.IOException;

/**
 * 로그인이 필요한 POST 요청에 대해 CSRF 토큰을 검증하는 필터 (web.xml에서 등록).
 * 회원가입/로그인은 세션이 없으므로 화이트리스트로 제외.
 */
public class CsrfFilter implements Filter {

    private static final String[] WHITELIST = {
            "/processAddMember",
            "/processLoginMember"
    };

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest  req = (HttpServletRequest)  request;
        HttpServletResponse res = (HttpServletResponse) response;

        if ("POST".equalsIgnoreCase(req.getMethod())) {
            String path = req.getServletPath();

            // 화이트리스트는 검증 생략
            for (String w : WHITELIST) {
                if (path.equals(w)) {
                    chain.doFilter(request, response);
                    return;
                }
            }

            // 비로그인 상태도 검증 생략 (세션 없음)
            HttpSession session = req.getSession(false);
            if (session == null || session.getAttribute("userId") == null) {
                chain.doFilter(request, response);
                return;
            }

            String token = req.getParameter("_csrf");
            if (!CsrfUtil.isValid(session, token)) {
                res.sendError(HttpServletResponse.SC_FORBIDDEN, "잘못된 요청입니다. 페이지를 새로고침 후 다시 시도하세요.");
                return;
            }
        }

        chain.doFilter(request, response);
    }
}
