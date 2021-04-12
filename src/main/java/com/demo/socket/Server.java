package com.demo.socket;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * Created by lenovo on 2021/4/12.
 */
public class Server {

    public static void main(String[] args) throws IOException {

        ServerSocket ss = new ServerSocket(18181);
        System.out.println("Server is running...");
        for (;;) {
            Socket socket = ss.accept();
//            socket.setKeepAlive(true);
//            socket.setSoLinger();
            System.out.println("connected from " + socket.getRemoteSocketAddress());
            Thread t = new Handler(socket);
            t.start();

            Thread shutdownHook = new Thread(() -> {
                try {
                    System.out.println("shutdown hook works");
                    socket.close();
                } catch (IOException e) {
                    System.out.println("shutdown exception");
                    e.printStackTrace();
                }
            });
            Runtime.getRuntime().addShutdownHook(shutdownHook);

        }

    }

//    int main(int argc, char **argv) {
//        int connfd;
//        char buf[1024];
//        connfd = tcp_server(SERV_PORT);
//        for (;;) {
//            int n = read(connfd, buf, 1024);
//            if (n < 0) {
//                error(1, errno, "error read");
//            } else if (n == 0) {
//                error(1, 0, "client closed \n");
//            }
//            sleep(5);
//            int write_nc = send(connfd, buf, n, 0);
//            printf("send bytes: %zu \n", write_nc);
//            if (write_nc < 0) {
//                error(1, errno, "error write");
//            }
//        }
//        exit(0);
//    }
}

class Handler extends Thread {
    private final Socket socket;

    Handler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {

        try(InputStream input = socket.getInputStream()) {
            try (OutputStream output =  socket.getOutputStream()) {
                handle(input, output);
            }
        } catch (IOException e) {
            try {
                socket.close();
            } catch (IOException e1) {
                //e1.printStackTrace();
            }

            System.out.println("client disconnected" + e.getMessage());
        }

    }

    private void handle(InputStream input, OutputStream output) throws IOException {
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(output, StandardCharsets.UTF_8));
        BufferedReader reader = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8));
        writer.write("hello\n");
        writer.flush();

        for (;;) {
            String s = reader.readLine();
            if(s.equals("bye")) {
                writer.write("bye\n");
                writer.flush();
                break;
            }
            writer.write("ok: " + s + "\n");
            writer.flush();



        }
    }
}
