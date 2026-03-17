package controller;

import dao.CarRepository;
import dto.Car;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import util.FileUtil;

import java.io.IOException;

@WebServlet("/processAddCar")
@MultipartConfig(maxFileSize = 5 * 1024 * 1024)
public class AddCarServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);
        String userId = (session != null) ? (String) session.getAttribute("userId") : null;

        if (userId == null) {
            response.sendRedirect(request.getContextPath() + "/member/loginMember.jsp");
            return;
        }

        String carBrand   = request.getParameter("carBrand");
        String carModel   = request.getParameter("carModel");
        String carYearStr = request.getParameter("carYear");

        int carYear = 0;
        try { carYear = Integer.parseInt(carYearStr); } catch (NumberFormatException ignored) {}

        String savedFileName = "";
        Part filePart = request.getPart("carImage");
        if (filePart != null && filePart.getSize() > 0) {
            String uploadDir = getServletContext().getRealPath("/resources/images");
            if (uploadDir == null) {
                response.sendRedirect(request.getContextPath()
                        + "/profile/myProfile.jsp?error=upload");
                return;
            }
            try {
                savedFileName = FileUtil.saveImage(filePart, uploadDir);
            } catch (IllegalArgumentException e) {
                response.sendRedirect(request.getContextPath()
                        + "/profile/myProfile.jsp?error=filetype");
                return;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        Car car = new Car();
        car.setUserId(userId);
        car.setCarBrand(carBrand);
        car.setCarModel(carModel);
        car.setCarYear(carYear);
        car.setCarImage(savedFileName);

        CarRepository.insertCar(car);

        response.sendRedirect(request.getContextPath() + "/profile/myProfile.jsp");
    }
}
