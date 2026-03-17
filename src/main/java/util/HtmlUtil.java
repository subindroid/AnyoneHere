package util;

public class HtmlUtil {

    /** HTML 특수문자를 엔티티로 이스케이프하여 XSS를 방지합니다. */
    public static String escape(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#x27;");
    }
}
