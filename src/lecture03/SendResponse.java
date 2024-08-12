package lecture03;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class SendResponse {

    /**
     * 200 OK 응답
     * @param output
     * @param fis
     * @param ext
     * @throws Exception
     */
    static void sendOkResponse(OutputStream output, InputStream input, String ext) throws Exception {
        Util.writeLine(output, "HTTP/1.1 200 OK");
        Util.writeLine(output, "Date: " + Util.getDateStringUtc());
        Util.writeLine(output, "Server: SmallCat/0.2");
        Util.writeLine(output, "Connection: close");
        Util.writeLine(output, "Content-Type: " + Util.getContentType(ext));
        Util.writeLine(output, "");

        int ch;
        while ((ch = input.read()) != -1) {
            output.write(ch);
        }
    }

    /**
     * 404 not found 응답
     * @param output
     * @param errorDocumentRoot
     * @throws Exception
     */
    static void sendNotFoundResponse(OutputStream output, String errorDocumentRoot) throws Exception {
        Util.writeLine(output, "HTTP/1.1 404 Not Found");
        Util.writeLine(output, "Date: " + Util.getDateStringUtc());
        Util.writeLine(output, "Server: SmallCat/0.2");
        Util.writeLine(output, "Connection: close");
        Util.writeLine(output, "Content-Type: text/html");
        Util.writeLine(output, "");

        try (InputStream fis = new BufferedInputStream(new FileInputStream(errorDocumentRoot + "/404.html"))) {
            int ch;
            while ((ch = fis.read()) != -1) {
                output.write(ch);
            }
        }
    }

    /**
     * 301 redirect 응답
     * @param output
     * @param location
     */
    static void sendMovePermanentlyResponse(OutputStream output, String location) throws Exception {
        Util.writeLine(output, "HTTP/1.1 301 Moved Permanently");
        Util.writeLine(output, "Date: " + Util.getDateStringUtc());
        Util.writeLine(output, "Server: SmallCat/0.2");
        Util.writeLine(output, "Location: " + location);
        Util.writeLine(output, "Connection: close");
        Util.writeLine(output, "");
    }



}
