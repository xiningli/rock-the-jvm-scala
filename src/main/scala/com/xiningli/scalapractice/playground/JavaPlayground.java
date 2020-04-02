package com.xiningli.scalapractice.playground;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class JavaPlayground {
    public static void main(String[] args) throws FileNotFoundException {
        System.out.println("Hello Java");
        PrintWriter out = new PrintWriter("filename.txt");
    }
}
