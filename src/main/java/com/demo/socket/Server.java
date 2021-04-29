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

                // this will trigger a normal shutdown, and execute hooks
                // System.exit(0);
                break;
            }

            writer.write("ok: " + s + "\n");
            writer.flush();

        }
    }
}
