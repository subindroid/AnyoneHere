package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDate;

import dao.UserRepository;
import dto.User;
import util.PasswordUtil;

@WebServlet("/processUpdateMember")
public class UpdateMemberServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        // 세션에서 userId를 가져옴 (파라미터 id는 폼 조작으로 위변조 가능)
        HttpSession session = request.getSession(false);
        String id = (session != null) ? (String) session.getAttribute("userId") : null;

        if (id == null) {
            response.sendRedirect(request.getContextPath() + "/member/loginMember.jsp");
            return;
        }

        String password = request.getParameter("password");
        String name     = request.getParameter("name");
        String gender   = request.getParameter("gender");

        String year  = request.getParameter("birthyy");
        String month = request.getParameter("birthmm");
        String day   = request.getParameter("birthdd");

        String birth;
        try {
            birth = String.format("%s-%02d-%02d", year,
                    Integer.parseInt(month), Integer.parseInt(day));
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/member/updateMember.jsp?error=birth");
            return;
        }

        String mail1 = request.getParameter("mail1");
        String mail2 = request.getParameter("mail2");
        if (mail1 == null || mail1.isBlank() || mail2 == null || mail2.isBlank()) {
            response.sendRedirect(request.getContextPath() + "/member/updateMember.jsp?error=email");
            return;
        }
        String email = mail1.trim() + "@" + mail2.trim();

        String p1 = request.getParameter("phone1");
        String p2 = request.getParameter("phone2");
        String p3 = request.getParameter("phone3");
        String phone = (p1 != null ? p1 : "") + "-"
                     + (p2 != null ? p2 : "") + "-"
                     + (p3 != null ? p3 : "");
        String address = request.getParameter("address");

        // 비밀번호 처리: 빈 값이면 기존 해시 유지, 새 값이면 최소 길이 확인 후 해싱
        String hashedPassword;
        if (password == null || password.isEmpty()) {
            User existing = UserRepository.getUserById(id);
            if (existing == null) {
                response.sendRedirect(request.getContextPath() + "/member/loginMember.jsp");
                return;
            }
            hashedPassword = existing.getUserPassword();
        } else {
            if (password.length() < 8) {
                response.sendRedirect(request.getContextPath() + "/member/updateMember.jsp?error=password");
                return;
            }
            hashedPassword = PasswordUtil.hash(password);
        }

        User user = new User();
        user.setUserId(id);
        user.setUserPassword(hashedPassword);
        user.setUserName(name);
        user.setUserGender(gender);
        user.setUserEmail(email);
        user.setUserPhone(phone);
        user.setUserAddress(address);
        user.setUserBirth(LocalDate.parse(birth));

        UserRepository.updateUser(user);

        response.sendRedirect(request.getContextPath() + "/member/resultMember.jsp?msg=0");
    }
}
