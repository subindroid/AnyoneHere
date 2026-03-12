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

@WebServlet("/processUpdateMember")
public class UpdateMemberServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String id = request.getParameter("id");
        String password = request.getParameter("password");
        String name = request.getParameter("name");
        String gender = request.getParameter("gender");

        String year = request.getParameter("birthyy");
        String month = request.getParameter("birthmm");
        String day = request.getParameter("birthdd");
        String birth = year + "-" + month + "-" + day;

        String mail1 = request.getParameter("mail1");
        String mail2 = request.getParameter("mail2");
        String email = mail1 + "@" + mail2;

        String phone = request.getParameter("phone1") + "-"
                     + request.getParameter("phone2") + "-"
                     + request.getParameter("phone3");
        String address = request.getParameter("address");

        // 비밀번호가 빈 값이면 기존 비밀번호 유지
        if (password == null || password.isEmpty()) {
            User existing = UserRepository.getUserById(id);
            if (existing != null) {
                password = existing.getUserPassword();
            }
        }

        User user = new User();

        user.setUserId(id);
        user.setUserPassword(password);
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
