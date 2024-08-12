package lecture03;

import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;

public class Util {

    /**
     * InputStream 으로 전달된 바이트열을 '행단위'로 읽는 유틸리티
     * - 줄바꿈(\n) 문자 전까지 입력된 문자열을 반환
     * @param input
     * @return ret
     * @throws Exception
     */
    static String readLine(InputStream input) throws Exception {
        int ch;
        String ret = "";

        while ((ch = input.read()) != -1) {
            if (ch == '\r') {
                // nothing
            } else if (ch == '\n') {
                break;
            } else {
                ret += (char)ch;
            }
        }

        if (ch == -1) {
            return null;
        } else {
            return ret;
        }
    }

    /**
     * response 하기 위해 한 행의 문자열을 바이트열로 OutputStream 에 쓰는 유틸리티
     * @param output
     * @param str
     * @throws Exception
     */
    static void writeLine(OutputStream output, String str) throws Exception {
        for (char ch : str.toCharArray()) {
            output.write((int)ch);
        }
        output.write((int)'\r');
        output.write((int)'\n');
    }

    /**
     * HTTP 표준에 맞춘 날짜문자열 반환하는 유틸리티
     * @return dateFormat
     */
    static String getDateStringUtc() {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        DateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss", Locale.US);
        dateFormat.setTimeZone(calendar.getTimeZone());
        return dateFormat.format(calendar.getTime()) + " GMT";
    }

    /**
     * 파일 확장자와 Content-Type 대응표
     */
    static final HashMap<String,String> contentTypeMap = new HashMap<String,String>(){
        {
            put("html", "text/html");
            put("htm", "text/html");
            put("txt", "text/plain");
            put("css", "text/css");
            put("png", "image/png");
            put("jpg", "image/jpeg");
            put("jpeg", "image/jpeg");
            put("gif", "image/gif");
        }
    };

    /**
     * 파일 확장자에 따른 Content-Type 반환
     * @param ext
     * @return ret (Content-Type)
     */
    static String getContentType(String ext) {
        String ret = contentTypeMap.get(ext.toLowerCase());     // 파라미터 ext 를 소문자로 변환 후 HashMap 에서 key로 조회
        if (ret == null) {
            return "application/octect-stream";
        } else {
            return ret;
        }
    }

}
