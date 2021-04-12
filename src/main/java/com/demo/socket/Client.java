package com.demo.socket;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * Created by lenovo on 2021/4/12.
 */
public class Client {

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("127.0.0.1", 18181);
        try (InputStream input = socket.getInputStream()) {
            try (OutputStream output = socket.getOutputStream()) {
                handle(input, output);
            }
        }
        socket.close();
        System.out.println("disconnected");
    }

    private static void handle(InputStream input, OutputStream output) throws IOException {
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(output, StandardCharsets.UTF_8));
        BufferedReader reader = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8));

        Scanner scanner = new Scanner(System.in);
        System.out.println("[server] " + reader.readLine());
        for (; ; ) {
            // 打印提示
            System.out.print(">>> ");
            // 读取一行输入
            String s = scanner.nextLine();
            writer.write(s);
            writer.newLine();
            writer.flush();
            String resp = reader.readLine();
            System.out.println("<<< " + resp);
            if (resp.equals("bye")) {
                break;
            }
        }
    }

//    int main(int argc, char **argv) {
//        if (argc != 2) {
//            error(1, 0, "usage: reliable_client01 <IPaddress>");
//        }
//        int socket_fd = tcp_client(argv[1], SERV_PORT);
//        char buf[128];
//        int len;
//        int rc;
//        while (fgets(buf, sizeof(buf), stdin) != NULL) {
//            len = strlen(buf);
//            rc = send(socket_fd, buf, len, 0);
//            if (rc < 0)
//                error(1, errno, "write failed");
//            rc = read(socket_fd, buf, sizeof(buf));
//            if (rc < 0)
//                error(1, errno, "read failed");
//            else if (rc == 0)
//                error(1, 0, "peer connection closed\n");
//            else
//                fputs(buf, stdout);
//        }
//        exit(0);
//    }
}
