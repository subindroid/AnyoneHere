package controller;

import dao.ReviewRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/processDeleteReview")
public class DeleteReviewServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);
        String userId = (session != null) ? (String) session.getAttribute("userId") : null;

        if (userId == null) {
            response.sendRedirect(request.getContextPath() + "/member/loginMember.jsp");
            return;
        }

        String reviewIdStr = request.getParameter("reviewId");
        String spotIdStr   = request.getParameter("spotId");

        int spotId;
        try {
            spotId = Integer.parseInt(spotIdStr);
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/spot/spots.jsp");
            return;
        }

        int reviewId;
        try {
            reviewId = Integer.parseInt(reviewIdStr);
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath()
                    + "/review/reviews.jsp?spotId=" + spotId);
            return;
        }

        ReviewRepository.deleteReview(reviewId, userId);
        response.sendRedirect(request.getContextPath()
                + "/review/reviews.jsp?spotId=" + spotId);
    }
}
