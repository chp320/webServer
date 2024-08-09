package lecture01;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class WebServer {
    public static void main(String[] args) {
        try {
            ServerSocket server = new ServerSocket(8001);
            File recvFile = new File("server_recv.txt");    // client -> server : server 로 전송된 데이터를 저장
            File sendFile = new File("server_send.txt");    // client <- server : server 가 client 로 전송할 데이터

            FileOutputStream fos = new FileOutputStream(recvFile);   // 파일 생성을 위한 output stream
            FileInputStream fis = new FileInputStream(sendFile);     // 파일 전송 위한 input stream

            System.out.println("클라이언트로부터의 접속을 기다리고 있습니다.");

            Socket socket = server.accept();
            System.out.println("클라이언트 접속");

            int ch;
            // 클라이언트로부터 수신한 내용을 server_recv.txt 에 출력
            InputStream input = socket.getInputStream();
            // 클라이언트는 "종료" 마크로 0을 송신함
            while ((ch = input.read()) != 0) {
                fos.write(ch);
            }

            // server_send.txt 내용을 클라이언트에 송신
            OutputStream output = socket.getOutputStream();
            while ((ch = fis.read()) != -1) {
                output.write(ch);
            }

            socket.close();
            System.out.println("통신이 종료되었습니다.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
