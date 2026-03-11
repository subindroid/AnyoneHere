package controller;

import dao.ReviewRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/processAddReview")
public class AddReviewServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);
        String userId = (session != null) ? (String) session.getAttribute("userId") : null;

        if (userId == null) {
            response.sendRedirect(request.getContextPath() + "/member/loginMember.jsp");
            return;
        }

        int spotId = Integer.parseInt(request.getParameter("spotId"));
        double rating = Double.parseDouble(request.getParameter("rating"));
        String content = request.getParameter("content");

        ReviewRepository.addReview(userId, spotId, content, rating);

        response.sendRedirect(request.getContextPath() + "/spot/spot.jsp?spotId=" + spotId);
    }
}
