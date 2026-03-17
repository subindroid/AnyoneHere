package controller;

import dao.PostLikeRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/processToggleLike")
public class ToggleLikeServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        String userId = (session != null) ? (String) session.getAttribute("userId") : null;

        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();

        if (userId == null) {
            out.print("{\"error\":\"login_required\"}");
            return;
        }

        String postIdStr = request.getParameter("postId");
        int postId;
        try {
            postId = Integer.parseInt(postIdStr);
        } catch (NumberFormatException e) {
            out.print("{\"error\":\"invalid_id\"}");
            return;
        }

        boolean liked     = PostLikeRepository.toggle(postId, userId);
        int likeCount     = PostLikeRepository.getLikeCount(postId);

        out.print(String.format("{\"liked\":%b,\"likeCount\":%d}", liked, likeCount));
    }
}
