package com.demo;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        String res = URLConnectionUtil.request("http://localhost:8888/", "GET", "2000");
        System.out.println(res);
    }
}
