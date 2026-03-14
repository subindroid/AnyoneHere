package controller;

import dao.AddSpotApplicationRepository;
import dto.AddSpotApplication;
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

@WebServlet("/processAddSpot")
@MultipartConfig(maxFileSize = 5 * 1024 * 1024)
public class AddSpotServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);
        String userId = (session != null) ? (String) session.getAttribute("userId") : null;

        if (userId == null) {
            response.sendRedirect(request.getContextPath() + "/member/loginMember.jsp");
            return;
        }

        String spotName        = request.getParameter("spotName");
        String spotLocation    = request.getParameter("spot_location");
        String spotDescription = request.getParameter("spot_description");
        String category        = request.getParameter("category");
        String spotAddress     = request.getParameter("spot_address");

        double latitude = 0.0, longitude = 0.0;
        if (spotLocation != null && spotLocation.contains(",")) {
            try {
                String[] parts = spotLocation.split(",");
                latitude  = Double.parseDouble(parts[0].trim());
                longitude = Double.parseDouble(parts[1].trim());
            } catch (NumberFormatException e) {
                // 좌표 파싱 실패 시 0.0 유지
            }
        }

        // 이미지 저장 (확장자 검증 + UUID 파일명)
        String savedFileName = "";
        Part filePart = request.getPart("spotImage");
        if (filePart != null && filePart.getSize() > 0) {
            String uploadDir = getServletContext().getRealPath("/resources/images");
            try {
                savedFileName = FileUtil.saveImage(filePart, uploadDir);
            } catch (IllegalArgumentException e) {
                response.sendRedirect(request.getContextPath()
                        + "/spotApplication/spotAddApplication.jsp?error=filetype");
                return;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        AddSpotApplication app = new AddSpotApplication();
        app.setUserId(userId);
        app.setSpotName(spotName);
        app.setSpotLatitude(latitude);
        app.setSpotLongitude(longitude);
        app.setSpotDescription(spotDescription);
        app.setSpotCategory(category != null ? category : "1");
        app.setSpotImage(savedFileName);
        app.setSpotAddress(spotAddress != null ? spotAddress : "");

        AddSpotApplicationRepository.insert(app);

        response.sendRedirect(request.getContextPath() + "/spotApplication/applicationConfirmation.jsp");
    }
}
