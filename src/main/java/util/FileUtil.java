package util;

import jakarta.servlet.http.Part;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Set;
import java.util.UUID;

public class FileUtil {

    private static final Set<String> ALLOWED_EXTENSIONS = Set.of("jpg", "jpeg", "png", "gif", "webp");

    /**
     * 이미지 파일을 검증하고 UUID 파일명으로 저장.
     *
     * @param filePart  업로드된 파일 Part
     * @param uploadDir 저장할 디렉토리 경로
     * @return 저장된 파일명 (UUID.확장자)
     * @throws IllegalArgumentException 허용되지 않는 확장자인 경우
     * @throws Exception                파일 저장 실패 시
     */
    public static String saveImage(Part filePart, String uploadDir) throws Exception {
        String original = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
        String ext = "";
        int dotIdx = original.lastIndexOf('.');
        if (dotIdx >= 0) {
            ext = original.substring(dotIdx + 1).toLowerCase();
        }

        if (!ALLOWED_EXTENSIONS.contains(ext)) {
            throw new IllegalArgumentException("허용되지 않는 파일 형식입니다: " + ext
                    + " (허용: jpg, jpeg, png, gif, webp)");
        }

        String savedName = UUID.randomUUID().toString() + "." + ext;
        try (InputStream input = filePart.getInputStream()) {
            Files.copy(input, Paths.get(uploadDir, savedName), StandardCopyOption.REPLACE_EXISTING);
        }
        return savedName;
    }
}
