package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import dao.UserRepository;
import dto.User;

@WebServlet("/processLoginMember")
public class LoginMemberServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String id       = request.getParameter("id");
        String password = request.getParameter("password");

        if (id == null || id.isBlank() || password == null || password.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/member/loginMember.jsp?error=true");
            return;
        }

        // validateUser가 User 객체를 반환 — DB 조회 1회로 해결
        User user = UserRepository.validateUser(id, password);

        if (user != null) {
            // 세션 고정 공격 방지: 기존 세션 무효화 후 새 세션 발급
            HttpSession oldSession = request.getSession(false);
            if (oldSession != null) oldSession.invalidate();

            HttpSession session = request.getSession(true);
            session.setAttribute("userId", user.getUserId());
            session.setAttribute("userRole",
                    user.getUserRole() != null ? user.getUserRole() : "USER");

            response.sendRedirect(request.getContextPath() + "/member/resultMember.jsp?msg=2");
        } else {
            response.sendRedirect(request.getContextPath() + "/member/loginMember.jsp?error=true");
        }
    }
}
