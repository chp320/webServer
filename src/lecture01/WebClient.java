package lecture01;

import java.io.*;
import java.net.Socket;

public class WebClient {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("127.0.0.1", 8001);

            File recvFile = new File("client_recv.txt");
            File sendFile = new File("client_send.txt");

            FileInputStream fis = new FileInputStream(sendFile);
            FileOutputStream fos = new FileOutputStream(recvFile);

            int ch;
            // client_send.txt 내용을 서버에 송신
            OutputStream output = socket.getOutputStream();

            // fis(client_send.txt)의 내용을 1byte 씩 읽어서 socket 의 output stream 에 출력
            while ((ch = fis.read()) != -1) {
                output.write(ch);
            }

            // 종료 마크로 0 을 송신 -> tcp, http 규칙이 아닌 server-client 간 규칙임
            output.write(0);

            // 서버로부터의 리턴을 client_recv.txt 에 출력
            InputStream input = socket.getInputStream();
            while ((ch = input.read()) != -1) {
                fos.write(ch);
            }

//            socket.close();       -> 이 경우, server 와의 소켓이 닫혀버리기 때문에 server 로 부터의 수신도 불가하여 작성하지 않음
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
