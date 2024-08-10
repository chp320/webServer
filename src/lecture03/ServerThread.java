package lecture03;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;

public class ServerThread implements Runnable {

    private static final String DOCUMENT_ROOT = "/usr/local/var/www";
    private Socket socket;

    /**
     * InputStream 으로 전달된 바이트열을 '행단위'로 읽는 유틸리티
     * - 줄바꿈(\n) 문자 전까지 입력된 문자열을 반환
     * @param input
     * @return ret
     * @throws Exception
     */
    private static String readLine(InputStream input) throws Exception {
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
    private static void writeLine(OutputStream output, String str) throws Exception {
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
    private static String getDateStringUtc() {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        DateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss", Locale.US);
        dateFormat.setTimeZone(calendar.getTimeZone());
        return dateFormat.format(calendar.getTime()) + " GMT";
    }

    /**
     * 파일 확장자와 Content-Type 대응표
     */
    private static final HashMap<String,String> contentTypeMap = new HashMap<String,String>(){
        {
            put("html", "text/html");
            put("html", "text/html");
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
    private static String getContentType(String ext) {
        String ret = contentTypeMap.get(ext.toLowerCase());     // 파라미터 ext 를 소문자로 변환 후 HashMap 에서 key로 조회
        if (ret == null) {
            return "application/octect-stream";
        } else {
            return ret;
        }
    }

    @Override
    public void run() {
        OutputStream output;
        try {
            InputStream input = socket.getInputStream();

            String line;
            String path = null;
            String ext = null;

            while ((line = readLine(input)) != null) {
                if (line == "") {
                    break;
                }
                if (line.startsWith("GET")) {
                    path = line.split(" ")[1];
                    String[] tmp = path.split("\\.");       // request line 의 path 에서 확장자 구하기 위해 (.) 기준으로 자름(split) - ex) a.jpg -> tmp[0]=a, tmp[1]=jpg
                    ext = tmp[tmp.length - 1];                    // 확장자만 추출
                }
            }

            output = socket.getOutputStream();

            // response header 반환
            writeLine(output, "HTTP/1.1 200 OK");
            writeLine(output, "Date: " + getDateStringUtc());
            writeLine(output, "Server: SmallCat/0.1");
            writeLine(output, "Connection: close");
            writeLine(output, "Content-Type: " + getContentType(ext));  // request line 에서 추출한 확장자로 Content-Type 을 반환
            writeLine(output, "");      // header 끝.

            // response body 반환
            try (FileInputStream fis1 = new FileInputStream(DOCUMENT_ROOT + path);) {
                // request line 에 요청했던 파일(데이터)를 읽어 InputStream 에 로딩

                int ch;
                while ((ch = fis1.read()) != -1) {
                    // 파일을 1 바이트씩 읽어서 OutputStream 에 출력
                    output.write(ch);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    ServerThread(Socket socket) {
        this.socket = socket;
    }
}
