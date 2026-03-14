package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import dao.ProfileRepository;
import dao.UserRepository;
import dto.Profile;
import dto.User;
import util.PasswordUtil;

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

        String year  = request.getParameter("birthyy");
        String month = request.getParameter("birthmm");
        String day   = request.getParameter("birthdd");

        String birth;
        try {
            birth = String.format("%s-%02d-%02d", year,
                    Integer.parseInt(month), Integer.parseInt(day));
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/member/addMember.jsp?error=birth");
            return;
        }

        String mail1      = request.getParameter("mail1");
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
        user.setUserPassword(PasswordUtil.hash(password));  // 해싱 후 저장
        user.setUserName(name);
        user.setUserGender(gender);
        user.setUserEmail(email);
        user.setUserPhone(phone);
        user.setUserAddress(address);
        user.setUserBirth(java.time.LocalDate.parse(birth));
        user.setCreatedAt(java.time.LocalDate.now());

        boolean success = UserRepository.addUser(user);

        if (!success) {
            // 아이디 또는 이메일 중복
            response.sendRedirect(request.getContextPath() + "/member/addMember.jsp?error=dup");
            return;
        }

        // 회원가입 성공 시 빈 profile row 자동 생성
        Profile profile = new Profile();
        profile.setUserId(id);
        ProfileRepository.upsertProfile(profile);

        response.sendRedirect(request.getContextPath() + "/member/resultMember.jsp?msg=1");
    }
}
