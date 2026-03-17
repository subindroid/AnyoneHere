package controller;

import dao.PostReportRepository;
import dto.PostReport;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/processReportPost")
public class ReportPostServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);
        String userId = (session != null) ? (String) session.getAttribute("userId") : null;

        if (userId == null) {
            response.sendRedirect(request.getContextPath() + "/member/loginMember.jsp");
            return;
        }

        String postIdStr = request.getParameter("postId");
        String reason    = request.getParameter("reason");

        int postId;
        try {
            postId = Integer.parseInt(postIdStr);
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/community/board.jsp");
            return;
        }

        if (reason == null || reason.isBlank()) {
            response.sendRedirect(request.getContextPath() + "/community/post.jsp?postId=" + postId + "&error=report_empty");
            return;
        }

        PostReport report = new PostReport();
        report.setPostId(postId);
        report.setReporterId(userId);
        report.setReason(reason.trim());

        boolean success = PostReportRepository.insert(report);
        String resultParam = success ? "reported=true" : "error=already_reported";
        response.sendRedirect(request.getContextPath()
                + "/community/post.jsp?postId=" + postId + "&" + resultParam);
    }
}
