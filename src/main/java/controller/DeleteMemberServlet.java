package controller;

import dao.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/processDeleteMember")
public class DeleteMemberServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);
        String userId = (session != null) ? (String) session.getAttribute("userId") : null;

        if (userId == null) {
            response.sendRedirect(request.getContextPath() + "/member/loginMember.jsp");
            return;
        }

        boolean deleted = UserRepository.deleteUser(userId);

        if (deleted) {
            session.invalidate(); // 세션 완전 삭제
            response.sendRedirect(request.getContextPath() + "/member/resultMember.jsp?msg=3");
        } else {
            response.sendRedirect(request.getContextPath() + "/member/updateMember.jsp?error=delete");
        }
    }
}
