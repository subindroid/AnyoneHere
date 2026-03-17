package controller;

import dao.RemoveSpotApplicationRepository;
import dao.SpotRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import util.DBUtil;

import java.io.IOException;
import java.sql.Connection;

@WebServlet("/admin/approveRemoveSpot")
public class ApproveRemoveSpotServlet extends HttpServlet {

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
        String spotIdStr        = request.getParameter("spotId");
        String action           = request.getParameter("action");

        if (applicationIdStr == null || spotIdStr == null || action == null) {
            response.sendRedirect(request.getContextPath() + "/admin/spotApplicationAdmin.jsp");
            return;
        }

        int applicationId, spotId;
        try {
            applicationId = Integer.parseInt(applicationIdStr);
            spotId        = Integer.parseInt(spotIdStr);
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/admin/spotApplicationAdmin.jsp");
            return;
        }

        if ("approve".equals(action)) {
            boolean success = false;
            try (Connection conn = DBUtil.getConnection()) {
                conn.setAutoCommit(false);
                try {
                    SpotRepository.deleteSpot(spotId, conn);
                    RemoveSpotApplicationRepository.updateStatus(applicationId, "APPROVED", conn);
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
        } else if ("reject".equals(action)) {
            String rejectReason = request.getParameter("rejectReason");
            if (rejectReason == null) rejectReason = "";
            RemoveSpotApplicationRepository.updateStatus(applicationId, "REJECTED", rejectReason);
        }

        response.sendRedirect(request.getContextPath() + "/admin/spotApplicationAdmin.jsp");
    }
}
