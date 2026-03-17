package util;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * PBKDF2WithHmacSHA256 기반 비밀번호 해싱 유틸.
 * 외부 라이브러리 없이 Java 표준 라이브러리만 사용.
 *
 * 주의: 기존 평문 비밀번호가 DB에 저장된 경우 로그인 불가.
 * 사용 전 users 테이블을 초기화하거나 기존 비밀번호를 마이그레이션해야 함.
 */
public class PasswordUtil {

    private static final int ITERATIONS = 65536;
    private static final int KEY_LENGTH = 256;
    private static final String ALGORITHM = "PBKDF2WithHmacSHA256";

    /** 비밀번호 해시 생성. "salt:hash" 형태 문자열 반환. */
    public static String hash(String password) {
        byte[] salt = new byte[16];
        new SecureRandom().nextBytes(salt);
        byte[] hash = pbkdf2(password.toCharArray(), salt);
        return Base64.getEncoder().encodeToString(salt)
                + ":" + Base64.getEncoder().encodeToString(hash);
    }

    /** 입력된 비밀번호가 저장된 해시와 일치하는지 검증. */
    public static boolean verify(String password, String stored) {
        if (stored == null || !stored.contains(":")) return false;
        String[] parts = stored.split(":", 2);
        byte[] salt     = Base64.getDecoder().decode(parts[0]);
        byte[] expected = Base64.getDecoder().decode(parts[1]);
        byte[] actual   = pbkdf2(password.toCharArray(), salt);
        if (actual.length != expected.length) return false;
        // 타이밍 공격 방지를 위한 constant-time 비교
        int diff = 0;
        for (int i = 0; i < actual.length; i++) diff |= actual[i] ^ expected[i];
        return diff == 0;
    }

    private static byte[] pbkdf2(char[] password, byte[] salt) {
        try {
            PBEKeySpec spec = new PBEKeySpec(password, salt, ITERATIONS, KEY_LENGTH);
            return SecretKeyFactory.getInstance(ALGORITHM).generateSecret(spec).getEncoded();
        } catch (Exception e) {
            throw new RuntimeException("비밀번호 해싱 오류", e);
        }
    }
}
