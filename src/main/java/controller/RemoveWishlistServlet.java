package controller;

import dao.WishlistRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/processRemoveWishlist")
public class RemoveWishlistServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        String userId = (session != null) ? (String) session.getAttribute("userId") : null;

        if (userId == null) {
            response.sendRedirect(request.getContextPath() + "/member/loginMember.jsp");
            return;
        }

        int spotId;
        try {
            spotId = Integer.parseInt(request.getParameter("spotId"));
        } catch (Exception e) {
            response.sendRedirect(request.getContextPath() + "/wishlist/wishlist.jsp");
            return;
        }

        WishlistRepository.removeWishlist(userId, spotId);
        response.sendRedirect(request.getContextPath() + "/wishlist/wishlist.jsp");
    }
}
