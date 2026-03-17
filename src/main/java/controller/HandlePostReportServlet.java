package controller;

import dao.PostReportRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/admin/handlePostReport")
public class HandlePostReportServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);
        String userRole = (session != null) ? (String) session.getAttribute("userRole") : null;
        if (!"ADMIN".equals(userRole)) {
            response.sendRedirect(request.getContextPath() + "/index.jsp");
            return;
        }

        String reportIdStr = request.getParameter("reportId");
        String action      = request.getParameter("action"); // reviewed / dismissed

        if (reportIdStr == null || action == null) {
            response.sendRedirect(request.getContextPath() + "/admin/spotApplicationAdmin.jsp");
            return;
        }

        try {
            int reportId = Integer.parseInt(reportIdStr);
            if ("reviewed".equals(action)) {
                PostReportRepository.updateStatus(reportId, "REVIEWED");
            } else if ("dismissed".equals(action)) {
                PostReportRepository.updateStatus(reportId, "DISMISSED");
            }
        } catch (NumberFormatException e) {
            // 무시
        }

        response.sendRedirect(request.getContextPath() + "/admin/spotApplicationAdmin.jsp?tab=report");
    }
}
