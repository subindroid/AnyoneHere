package controller;

import dao.AddSpotApplicationRepository;
import dao.SpotRepository;
import dto.AddSpotApplication;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import util.DBUtil;

import java.io.IOException;
import java.sql.Connection;

@WebServlet("/admin/approveAddSpot")
public class ApproveAddSpotServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);
        String userRole = (session != null) ? (String) session.getAttribute("userRole") : null;

        if (!"ADMIN".equals(userRole)) {
            response.sendRedirect(request.getContextPath() + "/index.jsp");
            return;
        }

        String applicationIdStr = request.getParameter("applicationId");
        String action           = request.getParameter("action");

        if (applicationIdStr == null || action == null) {
            response.sendRedirect(request.getContextPath() + "/admin/spotApplicationAdmin.jsp");
            return;
        }

        int applicationId;
        try {
            applicationId = Integer.parseInt(applicationIdStr);
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/admin/spotApplicationAdmin.jsp");
            return;
        }

        if ("approve".equals(action)) {
            AddSpotApplication app = AddSpotApplicationRepository.getAddApplicationById(applicationId);
            if (app != null) {
                boolean success = false;
                try (Connection conn = DBUtil.getConnection()) {
                    conn.setAutoCommit(false);
                    try {
                        SpotRepository.insertSpot(app, conn);
                        AddSpotApplicationRepository.updateStatus(applicationId, "APPROVED", conn);
                        conn.commit();
                        success = true;
                    } catch (Exception e) {
                        conn.rollback();
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (!success) {
                    response.sendRedirect(request.getContextPath() + "/admin/spotApplicationAdmin.jsp?error=failed");
                    return;
                }
            }
        } else if ("reject".equals(action)) {
            String rejectReason = request.getParameter("rejectReason");
            if (rejectReason == null) rejectReason = "";
            AddSpotApplicationRepository.updateStatus(applicationId, "REJECTED", rejectReason);
        }

        response.sendRedirect(request.getContextPath() + "/admin/spotApplicationAdmin.jsp");
    }
}
