package controller;

import dao.RemoveSpotApplicationRepository;
import dto.RemoveSpotApplication;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/processRemoveSpot")
public class RemoveSpotServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);
        String userId = (session != null) ? (String) session.getAttribute("userId") : null;

        if (userId == null) {
            response.sendRedirect(request.getContextPath() + "/member/loginMember.jsp");
            return;
        }

        String spotIdStr = request.getParameter("spotId");
        String removeReason = request.getParameter("remove_reason");

        if (spotIdStr == null || removeReason == null || removeReason.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/exceptionPages/exceptionInvalidInput.jsp");
            return;
        }
        if (removeReason.length() > 500) {
            response.sendRedirect(request.getContextPath() + "/exceptionPages/exceptionInvalidInput.jsp");
            return;
        }

        int spotId;
        try {
            spotId = Integer.parseInt(spotIdStr);
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/exceptionPages/exceptionInvalidInput.jsp");
            return;
        }

        RemoveSpotApplication app = new RemoveSpotApplication();
        app.setUserId(userId);
        app.setSpotId(spotId);
        app.setRemoveReason(removeReason.trim());
        app.setStatus("PENDING");

        RemoveSpotApplicationRepository.insert(app);

        response.sendRedirect(request.getContextPath() + "/spotApplication/applicationConfirmation.jsp");
    }
}
