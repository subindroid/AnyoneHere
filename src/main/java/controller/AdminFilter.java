package controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;

import java.io.IOException;

/** /admin/* 경로에 대한 관리자 인증 필터 (web.xml에서 등록) */
public class AdminFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest  req = (HttpServletRequest)  request;
        HttpServletResponse res = (HttpServletResponse) response;

        HttpSession session = req.getSession(false);
        String userRole = (session != null) ? (String) session.getAttribute("userRole") : null;

        if (!"ADMIN".equals(userRole)) {
            res.sendRedirect(req.getContextPath() + "/index.jsp");
            return;
        }

        chain.doFilter(request, response);
    }
}
