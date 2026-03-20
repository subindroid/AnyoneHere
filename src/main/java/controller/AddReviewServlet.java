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

        int spotId;
        try {
            spotId = Integer.parseInt(request.getParameter("spotId"));
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/spot/spots.jsp");
            return;
        }

        if (ReviewRepository.hasReviewed(userId, spotId)) {
            response.sendRedirect(request.getContextPath()
                    + "/review/reviews.jsp?spotId=" + spotId + "&error=duplicate");
            return;
        }

        double rating;
        try {
            rating = Double.parseDouble(request.getParameter("rating"));
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath()
                    + "/review/addReview.jsp?spotId=" + spotId + "&error=invalid");
            return;
        }
        if (rating < 0 || rating > 5) {
            response.sendRedirect(request.getContextPath()
                    + "/review/addReview.jsp?spotId=" + spotId + "&error=invalid");
            return;
        }

        String content = request.getParameter("content");
        if (content == null || content.isBlank()) {
            response.sendRedirect(request.getContextPath()
                    + "/review/addReview.jsp?spotId=" + spotId + "&error=empty");
            return;
        }

        try {
            ReviewRepository.addReview(userId, spotId, content.trim(), rating);
            response.sendRedirect(request.getContextPath() + "/review/reviews.jsp?spotId=" + spotId);
        } catch (Exception e) {
            // 동시 요청으로 인한 중복 키 예외 처리
            e.printStackTrace();
            response.sendRedirect(request.getContextPath()
                    + "/review/reviews.jsp?spotId=" + spotId + "&error=duplicate");
        }
    }
}
