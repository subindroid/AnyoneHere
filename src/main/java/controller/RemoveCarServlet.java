package controller;

import dao.CarRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/processRemoveCar")
public class RemoveCarServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        String userId = (session != null) ? (String) session.getAttribute("userId") : null;

        if (userId == null) {
            response.sendRedirect(request.getContextPath() + "/member/loginMember.jsp");
            return;
        }

        String carIdStr = request.getParameter("carId");
        if (carIdStr == null) {
            response.sendRedirect(request.getContextPath() + "/profile/myProfile.jsp");
            return;
        }

        int carId;
        try {
            carId = Integer.parseInt(carIdStr);
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/profile/myProfile.jsp");
            return;
        }
        CarRepository.deleteCar(carId, userId); // userId 검증 포함

        response.sendRedirect(request.getContextPath() + "/profile/myProfile.jsp");
    }
}
