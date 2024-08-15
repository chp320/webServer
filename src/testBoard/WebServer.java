package testBoard;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class WebServer {
    public static void main(String[] args) {
        try (ServerSocket server = new ServerSocket(8001);
             FileOutputStream fos = new FileOutputStream("server_recv.txt")) {
            System.out.println("클라이언트 접속을 기다리고 있습니다.");
            Socket socket = server.accept();
            System.out.println("클라이언트 접속");

            int ch;
            // 클라이언트에서 송신한 내용을 server_recv.txt에 출력
            InputStream input = socket.getInputStream();
            while ((ch = input.read()) != -1) {
                fos.write(ch);
            }
            socket.close();
            System.out.println("통신이 종료되었습니다.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
