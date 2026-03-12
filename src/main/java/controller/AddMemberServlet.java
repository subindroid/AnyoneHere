package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import dao.UserRepository;
import dto.User;

@WebServlet("/processAddMember")
public class AddMemberServlet extends HttpServlet {

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
        String mail2Custom = request.getParameter("mail2");
        String mail2Select = request.getParameter("mail2_select");
        String mail2 = (mail2Custom != null && !mail2Custom.isEmpty()) ? mail2Custom : mail2Select;
        String email = mail1 + "@" + mail2;

        String phone = request.getParameter("phone1") + "-"
                     + request.getParameter("phone2") + "-"
                     + request.getParameter("phone3");
        String address = request.getParameter("address");

        User user = new User();

        user.setUserId(id);
        user.setUserPassword(password);
        user.setUserName(name);
        user.setUserGender(gender);
        user.setUserEmail(email);
        user.setUserPhone(phone);
        user.setUserAddress(address);
        user.setUserBirth(java.time.LocalDate.parse(birth));
        user.setCreatedAt(java.time.LocalDate.now());

        UserRepository.addUser(user);

        response.sendRedirect(request.getContextPath() + "/member/resultMember.jsp?msg=1");
    }
}