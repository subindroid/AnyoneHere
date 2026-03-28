package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import util.LocationUtil;

import java.io.IOException;

@WebServlet("/processLogoutMember")
public class LogoutMemberServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session != null) {
            String userId = (String) session.getAttribute("userId");
            if (userId != null) {
                LocationUtil.clearLocationAndRecalculate(userId);
            }
            session.invalidate();
        }
        response.sendRedirect(request.getContextPath() + "/member/loginMember.jsp");
    }
}
