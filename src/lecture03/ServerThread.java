package lecture03;

import java.io.*;
import java.net.Socket;
import java.nio.file.*;

public class ServerThread implements Runnable {

    private static final String DOCUMENT_ROOT = "/usr/local/var/www";
    private static final String ERROR_DOCUMENT = "/usr/local/var/www/error";
    private static final String SERVER_NAME = "localhost:8001";
    private Socket socket;

    @Override
    public void run() {
        OutputStream output = null;
        try {
            InputStream input = socket.getInputStream();

            String line;
            String path = null;
            String ext = null;
            String host = null;

            while ((line = Util.readLine(input)) != null) {
                if (line.equals("")) {
                    break;
                }
                if (line.startsWith("GET")) {
                    path = MyURLDecoder.decode(line.split(" ")[1], "UTF-8");
                    System.out.println(">>>>> path: " + path);
                    String[] tmp = path.split("\\.");       // request line 의 path 에서 확장자 구하기 위해 (.) 기준으로 자름(split) - ex) a.jpg -> tmp[0]=a, tmp[1]=jpg
                    ext = tmp[tmp.length - 1];                    // 확장자만 추출
                } else if (line.startsWith("Host:")) {
                    host = line.substring("Host: ".length());
                    System.out.println(">>>>> host: " + host);
                }
            }

            if (path == null) {
                return;
            }

            if (path.endsWith("/")) {
                // path 가 확장자 없이 알파벳(디렉토리명)으로 들어온 경우, default로 index.html 파일을 붙혀준다.
                path += "index.html";
                ext = "html";

                /* (참고) http://example.com 과 같이 도메인까지만 작성한 경우, 브라우저가 http://example.com/ 과 같이 말미에 '/' 자동으로 붙혀서 보내줌 */
            }

            output = new BufferedOutputStream(socket.getOutputStream());

            FileSystem fs = FileSystems.getDefault();
            Path pathObj = fs.getPath(DOCUMENT_ROOT + path);
            Path realPath;
            try {
                realPath = pathObj.toRealPath();
                System.out.println(">>>>> realPath: " + realPath.toString());
            } catch (NoSuchFileException ex) {
                SendResponse.sendNotFoundResponse(output, ERROR_DOCUMENT);
                return;
            }

            if(!realPath.startsWith(DOCUMENT_ROOT)) {
                SendResponse.sendNotFoundResponse(output, ERROR_DOCUMENT);
                return;
            } else if (Files.isDirectory(realPath)) {
                String location = "http://" + ((host != null) ? host : SERVER_NAME) + path + "/";
                SendResponse.sendMovePermanentlyResponse(output, location);
                return;
            }

            try (InputStream fis = new BufferedInputStream(Files.newInputStream(realPath))) {
                SendResponse.sendOkResponse(output, fis, ext);
            } catch (FileNotFoundException ex) {
                SendResponse.sendNotFoundResponse(output, ERROR_DOCUMENT);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (output != null) {
                    output.close();
                }
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
