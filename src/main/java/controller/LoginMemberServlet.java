package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import dao.UserRepository;

@WebServlet("/processLoginMember")
public class LoginMemberServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String id = request.getParameter("id");
        String password = request.getParameter("password");

        boolean valid = UserRepository.validateUser(id, password);

        if (valid) {
            HttpSession session = request.getSession();
            session.setAttribute("userId", id);
            response.sendRedirect("resultMember.jsp?msg=2");
        } else {
            response.sendRedirect("loginMember.jsp?error=true");
        }
    }
}