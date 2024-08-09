package lecture03;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * - WebServer.java 를 base로 작성
 * - request line 을 해석해서 path 를 취득하고, response header 와 body 를 반환하는 기능
 */
public class SmallCat01 {

    private static final String DOCUMENT_ROOT = "/usr/local/var/www";

    /**
     * InputStream 에서 바이트열을 행단위로 읽어들이는 유틸리티
     * - 줄바꿈(\n) 문자까지 인입된 문자열을 반환
     * 참고) 앞서 WebServer.java 에서는 socket 생성 후, 해당 소켓에서 getInputStream() 을 통해 InputStream 객체를 생성했었음
     * @param input
     * @return ret
     * @throws Exception
     */
    private static String readLine(InputStream input) throws Exception {
        int ch;
        String ret = "";

        while ((ch = input.read()) != 1) {
            if (ch == '\r') {
                // nothing do
            } else if (ch == '\n') {
                // 줄바꿈('\n') 문자인 경우 line 단위 읽기 중단
                break;
            } else {
                ret += (char)ch;
            }
        }

        if (ch == 1) {
            return null;
        } else {
            return ret;
        }
    }

    /**
     * 1행의 문자열을 바이트열로 OutputStream에 쓰는 유틸리티
     * - 서버에서 클라이언트로 response 를 주기 위함
     * @param output
     * @param str
     * @throws Exception
     */
    private static void writeLine(OutputStream output, String str) throws Exception {
        for (char ch : str.toCharArray()) {
            output.write((int) ch);
        }
        output.write((int)'\r');
        output.write((int)'\n');
    }

    /**
     * 현재 시각을 HTTP 표준 포맷에 맞게 날짜 문자열로 반환
     * @return dateFormat (ex. Fri, 09 Aug 2024 15:22:34 GMT)
     */
    private static String getDateStringUtc() {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        DateFormat dateFormat = new SimpleDateFormat("EEE,ddMMMyyyyHH:mm:ss", Locale.US);
        dateFormat.setTimeZone(calendar.getTimeZone());
        return dateFormat.format(calendar.getTime()) + "GMT";
    }

    public static void main(String[] args) throws Exception {
        try (ServerSocket server = new ServerSocket(8001)) {
            // 클라이언트 접속 수락
            Socket socket = server.accept();
            // 클라이언트에서 보내는 데이터 수신을 위해 InputStream 생성
            InputStream input = socket.getInputStream();

            String line;
            String path = null;
            while ((line = readLine(input)) != null) {
                if (line == "") {
                    break;
                }
                // request line 에서 GET method 인 경우, 요청한 데이터 확인 (ex. GET /google-logo.png HTTP/1.1)
                if (line.startsWith("GET")) {
                    path = line.split(" ")[1];
                }
            }

            // response 하기 위해서 OutputStream 생성
            OutputStream output = socket.getOutputStream();

            // response header 생성
            writeLine(output, "HTTP/1.1 200 OK");
            writeLine(output, "Date: " + getDateStringUtc());
            writeLine(output, "Server: SmallCat/0.1");
            writeLine(output, "Connection: close");
            writeLine(output, "Content-Type: text/html");
            writeLine(output, "");      // response header 끝

            // response body
            // request line 의 요청 데이터를 document root 에서 찾아서 1 byte 씩 읽어서 응답
            try (FileInputStream fis = new FileInputStream(DOCUMENT_ROOT + path);) {
                int ch;
                while ((ch = fis.read()) != -1) {
                    output.write(ch);
                }
            }

            socket.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


}
